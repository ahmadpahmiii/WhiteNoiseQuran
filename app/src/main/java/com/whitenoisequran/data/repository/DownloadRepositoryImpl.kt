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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val surahDao: SurahDao
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
}
