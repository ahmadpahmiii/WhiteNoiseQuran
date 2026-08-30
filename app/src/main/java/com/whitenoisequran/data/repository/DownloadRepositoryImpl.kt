package com.whitenoisequran.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.data.worker.BulkDownloadWorker
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.repository.DownloadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val surahDao: SurahDao,
    private val okHttpClient: OkHttpClient
) : DownloadRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun getDownloadProgressFlow(reciterId: Int): Flow<BulkDownloadProgress> {
        val completedFlow = surahDao.getCompletedCountFlow(reciterId)
        val failedFlow = surahDao.getFailedCountFlow(reciterId)
        val workFlow = workManager.getWorkInfosForUniqueWorkFlow("${BulkDownloadWorker.WORK_NAME_PREFIX}$reciterId")

        return combine(completedFlow, failedFlow, workFlow) { completed, failed, workInfos ->
            val activeWork = workInfos.firstOrNull()
            val isRunning = activeWork?.state == WorkInfo.State.RUNNING
            val isFinished = completed >= 114 || activeWork?.state == WorkInfo.State.SUCCEEDED
            val currentSurah = activeWork?.progress?.getInt(BulkDownloadWorker.KEY_CURRENT_SURAH, completed) ?: completed

            val remaining = (114 - completed).coerceAtLeast(0)
            val estimatedMinutes = (remaining * 1.5 / 60.0).toInt().coerceAtLeast(1)

            BulkDownloadProgress(
                totalSurahs = 114,
                completedCount = completed,
                failedCount = failed,
                currentSurahNumber = currentSurah,
                isFinished = isFinished,
                isRunning = isRunning,
                estimatedMinutesRemaining = estimatedMinutes
            )
        }
    }

    override suspend fun startBulkDownload(reciterId: Int, reciterSlug: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putInt(BulkDownloadWorker.KEY_RECITER_ID, reciterId)
            .putString(BulkDownloadWorker.KEY_RECITER_SLUG, reciterSlug)
            .build()

        val downloadRequest = OneTimeWorkRequestBuilder<BulkDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(
            "${BulkDownloadWorker.WORK_NAME_PREFIX}$reciterId",
            ExistingWorkPolicy.KEEP,
            downloadRequest
        )
    }

    override suspend fun pauseOrCancelDownload(reciterId: Int) {
        workManager.cancelUniqueWork("${BulkDownloadWorker.WORK_NAME_PREFIX}$reciterId")
    }

    override suspend fun updateSurahDownloadState(
        surahNumber: Int,
        reciterId: Int,
        state: DownloadState,
        localPath: String?
    ) {
        surahDao.updateDownloadState(surahNumber, reciterId, state, localPath)
    }

    override suspend fun isReciterAudioDownloaded(reciterId: Int): Boolean {
        val completed = surahDao.getCompletedCount(reciterId)
        return completed >= 114
    }

    override suspend fun downloadSingleSurah(
        surahNumber: Int,
        reciterId: Int,
        reciterSlug: String
    ) = withContext(Dispatchers.IO) {
        val audioDir = File(context.filesDir, "audio/$reciterSlug").apply {
            if (!exists()) mkdirs()
        }
        val fileName = String.format(Locale.US, "%03d.mp3", surahNumber)
        val destinationFile = File(audioDir, fileName)

        surahDao.updateDownloadState(surahNumber, reciterId, DownloadState.DOWNLOADING, null)

        val cdnUrl = "https://cdn.equran.id/audio-full/$reciterSlug/$fileName"
        val success = downloadAudioFile(cdnUrl, destinationFile)

        if (success && destinationFile.exists() && destinationFile.length() > 50_000) {
            surahDao.updateDownloadState(
                surahNumber,
                reciterId,
                DownloadState.DONE,
                destinationFile.absolutePath
            )
        } else {
            surahDao.updateDownloadState(surahNumber, reciterId, DownloadState.FAILED, null)
        }
    }

    override suspend fun deleteSurahAudio(surahNumber: Int, reciterId: Int, reciterSlug: String) =
        withContext(Dispatchers.IO) {
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
            pauseOrCancelDownload(reciterId)
            val audioDir = File(context.filesDir, "audio/$reciterSlug")
            if (audioDir.exists()) {
                audioDir.deleteRecursively()
            }
            surahDao.resetAllDownloadStates(reciterId)
        }

    private fun downloadAudioFile(url: String, targetFile: File): Boolean {
        return try {
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful || response.body == null) {
                return false
            }

            val tempFile = File(targetFile.parentFile, "${targetFile.name}.tmp")
            response.body!!.byteStream().use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            if (tempFile.exists() && tempFile.length() > 10_000) {
                if (targetFile.exists()) targetFile.delete()
                tempFile.renameTo(targetFile)
                true
            } else {
                tempFile.delete()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
