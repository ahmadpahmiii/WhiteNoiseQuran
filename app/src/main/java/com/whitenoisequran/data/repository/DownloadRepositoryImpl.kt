package com.whitenoisequran.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.whitenoisequran.data.isOnline
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.data.remote.QuranMetadataRegistry
import com.whitenoisequran.data.worker.BulkDownloadWorker
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.repository.DownloadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.time.Duration
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val surahDao: SurahDao,
    private val okHttpClient: OkHttpClient
) : DownloadRepository {

    private val workManager = WorkManager.getInstance(context)

    // Audio files on the CDN don't change, so each size is asked for once per app run
    private val fileSizes = ConcurrentHashMap<String, Long>()

    override fun getDownloadProgressFlow(reciterId: Int): Flow<BulkDownloadProgress> {
        val completedFlow = surahDao.getCompletedCountFlow(reciterId)
        val failedFlow = surahDao.getFailedCountFlow(reciterId)
        val workFlow = workManager.getWorkInfosForUniqueWorkFlow("${BulkDownloadWorker.WORK_NAME_PREFIX}$reciterId")
        val percentFlow =
            BulkDownloadWorker.liveProgress.map { it[reciterId].orEmpty() }.distinctUntilChanged()

        return combine(
            completedFlow,
            failedFlow,
            workFlow,
            percentFlow
        ) { completed, failed, workInfos, percents ->
            val activeWork = workInfos.firstOrNull()
            // Not finished = running, waiting for network, or waiting to retry
            val isRunning = activeWork?.state?.isFinished == false
            val isFinished = completed >= 114 || activeWork?.state == WorkInfo.State.SUCCEEDED
            BulkDownloadProgress(
                totalSurahs = 114,
                completedCount = completed,
                failedCount = failed,
                isFinished = isFinished,
                isRunning = isRunning,
                // Work state changes (running -> enqueued) when the connection drops, so this re-checks then
                isWaitingForNetwork = activeWork?.state == WorkInfo.State.ENQUEUED && !context.isOnline(),
                surahPercent = percents
            )
        }
    }

    override suspend fun startBulkDownload(reciterId: Int, reciterSlug: String) {
        enqueueDownload("${BulkDownloadWorker.WORK_NAME_PREFIX}$reciterId", reciterId, reciterSlug)
    }

    private fun enqueueDownload(
        workName: String,
        reciterId: Int,
        reciterSlug: String,
        surahNumber: Int = 0 // 0 = all surahs
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putInt(BulkDownloadWorker.KEY_RECITER_ID, reciterId)
            .putString(BulkDownloadWorker.KEY_RECITER_SLUG, reciterSlug)
            .putInt(BulkDownloadWorker.KEY_SURAH_NUMBER, surahNumber)
            .build()

        val downloadRequest = OneTimeWorkRequestBuilder<BulkDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(30))
            .addTag("${BulkDownloadWorker.TAG_RECITER_PREFIX}$reciterId")
            .build()

        workManager.enqueueUniqueWork(workName, ExistingWorkPolicy.KEEP, downloadRequest)
    }

    private fun singleWorkName(surahNumber: Int, reciterId: Int) =
        "${BulkDownloadWorker.SINGLE_WORK_NAME_PREFIX}${reciterId}_$surahNumber"

    // Pause = cancel; finished and partial files are kept, so starting again resumes.
    override suspend fun pauseOrCancelDownload(reciterId: Int) {
        workManager.cancelAllWorkByTag("${BulkDownloadWorker.TAG_RECITER_PREFIX}$reciterId")
        // Queued single downloads never ran, so nothing else clears their "downloading" state.
        surahDao.resetInProgressDownloads(reciterId)
    }

    override suspend fun downloadSingleSurah(
        surahNumber: Int,
        reciterId: Int,
        reciterSlug: String
    ) {
        // Show as downloading right away; WorkManager runs it (survives leaving the screen, waits for network).
        surahDao.updateDownloadState(surahNumber, reciterId, DownloadState.DOWNLOADING, null)
        enqueueDownload(singleWorkName(surahNumber, reciterId), reciterId, reciterSlug, surahNumber)
    }

    override suspend fun cancelSurahDownload(surahNumber: Int, reciterId: Int) {
        workManager.cancelUniqueWork(singleWorkName(surahNumber, reciterId))
        BulkDownloadWorker.cancelSurah(reciterId, surahNumber)
        // A queued download never ran, so nothing else clears its "downloading" state.
        surahDao.resetInProgressDownload(surahNumber, reciterId)
    }

    override fun getDownloadedCountsFlow(): Flow<Map<Int, Int>> = surahDao.getCompletedCountsFlow()

    override suspend fun getAudioSizeBytes(reciterSlug: String): Long =
        withContext(Dispatchers.IO) {
            File(context.filesDir, "audio/$reciterSlug").walk().filter { it.isFile }
                .sumOf { it.length() }
        }

    override suspend fun getDownloadSizeBytes(reciterSlug: String, surahNumbers: List<Int>): Long? =
        withContext(Dispatchers.IO) {
            val permits = Semaphore(16) // HEAD requests are tiny and share one HTTP/2 connection
            val sizes = surahNumbers.map { number ->
                async {
                    val url = QuranMetadataRegistry.audioUrl(reciterSlug, number)
                    fileSizes[url] ?: permits.withPermit {
                        runCatching {
                            okHttpClient.newCall(Request.Builder().url(url).head().build())
                                .execute().use {
                                it.header("Content-Length")?.toLongOrNull()
                                    ?.takeIf { _ -> it.isSuccessful }
                            }
                        }.getOrNull()?.also { size -> fileSizes[url] = size }
                    }
                }
            }.awaitAll()
            if (sizes.any { it == null }) null else sizes.sumOf { it ?: 0L }
        }

    override suspend fun deleteSurahAudio(surahNumber: Int, reciterId: Int, reciterSlug: String) =
        withContext(Dispatchers.IO) {
            workManager.cancelUniqueWork(singleWorkName(surahNumber, reciterId))
            val audioDir = File(context.filesDir, "audio/$reciterSlug")
            val fileName = String.format(Locale.US, "%03d.mp3", surahNumber)
            val file = File(audioDir, fileName)
            if (file.exists()) {
                file.delete()
            }
            surahDao.updateDownloadState(surahNumber, reciterId, DownloadState.NONE, null)
        }

    override suspend fun deleteAllAudio(reciterId: Int, reciterSlug: String) =
        withContext(Dispatchers.IO) {
            workManager.cancelAllWorkByTag("${BulkDownloadWorker.TAG_RECITER_PREFIX}$reciterId")
            val audioDir = File(context.filesDir, "audio/$reciterSlug")
            if (audioDir.exists()) {
                audioDir.deleteRecursively()
            }
            surahDao.resetAllDownloadStates(reciterId)
        }
}
