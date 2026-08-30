package com.whitenoisequran.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.domain.model.DownloadState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

@HiltWorker
class BulkDownloadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val surahDao: SurahDao,
    private val okHttpClient: OkHttpClient
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_RECITER_ID = "key_reciter_id"
        const val KEY_RECITER_SLUG = "key_reciter_slug"
        const val KEY_COMPLETED_COUNT = "key_completed_count"
        const val KEY_CURRENT_SURAH = "key_current_surah"
        const val WORK_NAME_PREFIX = "bulk_download_"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val reciterId = inputData.getInt(KEY_RECITER_ID, -1)
        val reciterSlug = inputData.getString(KEY_RECITER_SLUG)

        if (reciterId == -1 || reciterSlug.isNullOrEmpty()) {
            return@withContext Result.failure()
        }

        val audioDir = File(appContext.filesDir, "audio/$reciterSlug").apply {
            if (!exists()) mkdirs()
        }

        var completedCount = 0

        for (surahNum in 1..114) {
            if (isStopped) return@withContext Result.retry()

            val fileName = String.format(Locale.US, "%03d.mp3", surahNum)
            val destinationFile = File(audioDir, fileName)

            // Check if already downloaded and valid
            if (destinationFile.exists() && destinationFile.length() > 50_000) {
                completedCount++
                surahDao.updateDownloadState(
                    number = surahNum,
                    reciterId = reciterId,
                    state = DownloadState.DONE,
                    localPath = destinationFile.absolutePath
                )
                setProgress(
                    workDataOf(
                        KEY_COMPLETED_COUNT to completedCount,
                        KEY_CURRENT_SURAH to surahNum
                    )
                )
                continue
            }

            // Set state to DOWNLOADING
            surahDao.updateDownloadState(
                number = surahNum,
                reciterId = reciterId,
                state = DownloadState.DOWNLOADING,
                localPath = null
            )

            val cdnUrl = "https://cdn.equran.id/audio-full/$reciterSlug/$fileName"
            val success = downloadAudioFile(cdnUrl, destinationFile)

            if (success && destinationFile.exists() && destinationFile.length() > 50_000) {
                completedCount++
                surahDao.updateDownloadState(
                    number = surahNum,
                    reciterId = reciterId,
                    state = DownloadState.DONE,
                    localPath = destinationFile.absolutePath
                )
            } else {
                surahDao.updateDownloadState(
                    number = surahNum,
                    reciterId = reciterId,
                    state = DownloadState.FAILED,
                    localPath = null
                )
            }

            setProgress(
                workDataOf(
                    KEY_COMPLETED_COUNT to completedCount,
                    KEY_CURRENT_SURAH to surahNum
                )
            )
        }

        Result.success()
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
