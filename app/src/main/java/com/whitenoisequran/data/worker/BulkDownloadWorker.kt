package com.whitenoisequran.data.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.whitenoisequran.MainActivity
import com.whitenoisequran.data.isOnline
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.data.remote.QuranMetadataRegistry
import com.whitenoisequran.domain.model.DownloadState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

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

        /** Optional: download only this surah instead of all 114. */
        const val KEY_SURAH_NUMBER = "key_surah_number"
        const val WORK_NAME_PREFIX = "bulk_download_"
        const val SINGLE_WORK_NAME_PREFIX = "single_download_"
        const val TAG_RECITER_PREFIX = "download_reciter_"

        private const val PARALLEL_DOWNLOADS = 5

        /** Runs per failed surah: first try + WorkManager retries (LINEAR backoff). */
        private const val MAX_ATTEMPTS = 3
        private const val MIN_AUDIO_BYTES = 10_000L
        private const val CHANNEL_ID = "quran_download_channel"
        private const val NOTIFICATION_ID_BASE = 2000
        private const val RESULT_NOTIFICATION_TAG = "download_result"

        // One download per file at a time: a single-surah download and the bulk run may reach the same surah.
        private val fileLocks = ConcurrentHashMap<String, Mutex>()

        private val _liveProgress = MutableStateFlow<Map<Int, Map<Int, Int>>>(emptyMap())

        /** Percent (0-100) of files downloading right now: reciterId -> (surah number -> percent).
         *  In memory only: workers run in the app process, and a restarted worker reports again. */
        val liveProgress: StateFlow<Map<Int, Map<Int, Int>>> = _liveProgress.asStateFlow()

        // Running surah downloads by (reciterId, surah number), so one can be cancelled while the rest continue.
        private val surahJobs = ConcurrentHashMap<Pair<Int, Int>, Job>()

        /** Cancels one surah's download, whether it runs on its own or as part of the bulk download. */
        fun cancelSurah(reciterId: Int, surahNum: Int) {
            surahJobs[reciterId to surahNum]?.cancel()
        }
    }

    private val reciterId = inputData.getInt(KEY_RECITER_ID, -1)
    private val reciterSlug = inputData.getString(KEY_RECITER_SLUG).orEmpty()
    private val singleSurah = inputData.getInt(KEY_SURAH_NUMBER, 0)
    private val isBulk = singleSurah !in 1..114
    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    // One id per download, so a bulk and a single download don't replace each other's notification
    private val notificationId = NOTIFICATION_ID_BASE + reciterId * 1000 + singleSurah

    /** Set when a download failed because the connection dropped: the run stops and retries once it's back. */
    private val wentOffline = AtomicBoolean(false)

    /** How a surah that will be tried again looks: in a bulk run it looks not started yet, a single download stays queued. */
    private val retryState = if (isBulk) DownloadState.NONE else DownloadState.DOWNLOADING

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (reciterId == -1 || reciterSlug.isEmpty()) {
            return@withContext Result.failure()
        }

        val audioDir = File(appContext.filesDir, "audio/$reciterSlug").apply {
            if (!exists()) mkdirs()
        }
        val surahRange = if (isBulk) 1..114 else singleSurah..singleSurah

        if (isBulk) {
            notificationManager.cancel(RESULT_NOTIFICATION_TAG, reciterId)
            try {
                // A foreground service keeps a long download going past background job time limits
                setForeground(getForegroundInfo())
            } catch (e: IllegalStateException) {
                // Android 12+ refuses while the app is in the background; the work still runs and notify() shows progress
            }
        }

        val permits = Semaphore(PARALLEL_DOWNLOADS)
        val completedCount = AtomicInteger(0)
        val failedCount = AtomicInteger(0)

        try {
            coroutineScope {
                for (surahNum in surahRange) {
                    permits.acquire() // start surahs in order, at most PARALLEL_DOWNLOADS at a time
                    if (wentOffline.get()) { // the rest waits for the connection (retry below)
                        permits.release()
                        break
                    }
                    launch {
                        try {
                            if (downloadSurah(surahNum, audioDir)) {
                                completedCount.incrementAndGet()
                            } else {
                                failedCount.incrementAndGet()
                            }
                            if (isBulk) notificationManager.notify(
                                notificationId,
                                progressNotification(completedCount.get())
                            )
                        } finally {
                            permits.release()
                        }
                    }
                }
            }
        } finally {
            notificationManager.cancel(notificationId)
        }

        when {
            // Constraints hold the retry until the network is back; attempts aren't used up meanwhile
            wentOffline.get() -> Result.retry()
            failedCount.get() == 0 -> {
                if (isBulk && completedCount.get() == 114) {
                    notifyResult("Download complete", "All 114 surahs are available offline")
                }
                Result.success()
            }
            // Downloaded files are skipped on the next run, so only the failed surahs are retried.
            // ponytail: runAttemptCount also counts system stops (job time limits), so a long download
            // may get fewer automatic retries; track failures per surah if that matters.
            runAttemptCount + 1 < MAX_ATTEMPTS -> Result.retry()
            // Out of attempts: FAILED surahs stay retryable from the UI.
            else -> {
                val failed = failedCount.get()
                if (isBulk) notifyResult(
                    "$failed ${if (failed == 1) "surah" else "surahs"} couldn't be downloaded",
                    "Tap to retry"
                )
                Result.success()
            }
        }
    }

    /** Returns true when the surah's audio is on disk (already there or freshly downloaded). */
    private suspend fun downloadSurah(surahNum: Int, audioDir: File): Boolean {
        val fileName = String.format(Locale.US, "%03d.mp3", surahNum)
        val file = File(audioDir, fileName)
        val jobKey = reciterId to surahNum
        val job = currentCoroutineContext().job
        surahJobs[jobKey] = job
        try {
            fileLocks.computeIfAbsent(file.path) { Mutex() }.withLock {
                if (!isValidAudio(file)) {
                    surahDao.updateDownloadState(
                        surahNum,
                        reciterId,
                        DownloadState.DOWNLOADING,
                        null
                    )
                    if (!downloadAudioFile(
                            QuranMetadataRegistry.audioUrl(reciterSlug, surahNum),
                            file
                        ) { setLiveProgress(surahNum, it) }
                    ) {
                        val offline = !appContext.isOnline()
                        if (offline) wentOffline.set(true)
                        // Failed (red) only once retries are used up; a dropped connection doesn't use them
                        val retrying = offline || runAttemptCount + 1 < MAX_ATTEMPTS
                        surahDao.updateDownloadState(
                            surahNum,
                            reciterId,
                            if (retrying) retryState else DownloadState.FAILED,
                            null
                        )
                        return false
                    }
                }
            }
            surahDao.updateDownloadState(surahNum, reciterId, DownloadState.DONE, file.absolutePath)
            return true
        } catch (e: CancellationException) {
            // Paused or stopped by the system: don't leave a stuck "downloading" spinner.
            withContext(NonCancellable) {
                when {
                    isValidAudio(file) ->
                        surahDao.updateDownloadState(
                            surahNum,
                            reciterId,
                            DownloadState.DONE,
                            file.absolutePath
                        )
                    // System stop (e.g. connection lost): WorkManager runs it again later
                    isStopped && stopReason != WorkInfo.STOP_REASON_CANCELLED_BY_APP ->
                        surahDao.updateDownloadState(surahNum, reciterId, retryState, null)

                    else -> surahDao.updateDownloadState(
                        surahNum,
                        reciterId,
                        DownloadState.NONE,
                        null
                    )
                }
            }
            throw e
        } finally {
            surahJobs.remove(jobKey, job)
            setLiveProgress(surahNum, null)
        }
    }

    private fun setLiveProgress(surahNum: Int, percent: Int?) = _liveProgress.update { all ->
        val forReciter = all[reciterId].orEmpty()
        all + (reciterId to if (percent == null) forReciter - surahNum else forReciter + (surahNum to percent))
    }

    private suspend fun downloadAudioFile(
        url: String,
        targetFile: File,
        onPercent: (Int) -> Unit
    ): Boolean {
        // Kept across pause, retries and system stops (job time limits); the next attempt resumes it.
        val partFile = File(targetFile.parentFile, "${targetFile.name}.part")
        val resumeFrom = partFile.length()
        val request = Request.Builder()
            .url(url)
            .apply { if (resumeFrom > 0) header("Range", "bytes=$resumeFrom-") }
            .build()
        try {
            okHttpClient.newCall(request).execute().use { response ->
                if (response.code == 416) { // part file doesn't fit the server file: start over next time
                    partFile.delete()
                    return false
                }
                val body = response.body
                if (!response.isSuccessful || body == null) return false
                // 206 = server continues from resumeFrom; 200 = whole file, so overwrite
                val append = response.code == 206
                var written = if (append) resumeFrom else 0L
                val total = body.contentLength().takeIf { it > 0 }
                    ?.plus(written) // unknown size: no percent
                var lastPercent = -1
                FileOutputStream(partFile, append).use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        while (true) {
                            currentCoroutineContext().ensureActive() // stop promptly on pause
                            val read = input.read(buffer)
                            if (read == -1) break
                            output.write(buffer, 0, read)
                            written += read
                            val percent = total?.let { (written * 100 / it).toInt() } ?: continue
                            if (percent != lastPercent) {
                                lastPercent = percent
                                onPercent(percent)
                            }
                        }
                    }
                }
            }
            if (isValidAudio(partFile) && partFile.renameTo(targetFile)) return true
            partFile.delete() // complete but unusable: start over next time
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false // keep the part file; the next attempt resumes it
        }
    }

    private fun isValidAudio(file: File) = file.length() > MIN_AUDIO_BYTES

    // Also used below Android 12, where WorkManager runs expedited work as a foreground service.
    override suspend fun getForegroundInfo() =
        ForegroundInfo(
            notificationId,
            progressNotification(0),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )

    private fun progressNotification(done: Int): Notification = notificationBuilder()
        .setContentTitle("Downloading Quran audio")
        .setContentText(if (isBulk) "$done of 114 surahs" else "Surah $singleSurah")
        .setProgress(114, done, !isBulk)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .addAction(0, "Pause", WorkManager.getInstance(appContext).createCancelPendingIntent(id))
        .build()

    private fun notifyResult(title: String, text: String) = notificationManager.notify(
        RESULT_NOTIFICATION_TAG,
        reciterId,
        notificationBuilder()
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()
    )

    /** Tapping any download notification opens the download screen. */
    private fun notificationBuilder(): NotificationCompat.Builder {
        notificationManager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "Quran Downloads", NotificationManager.IMPORTANCE_LOW)
        )
        val openDownloads = PendingIntent.getActivity(
            appContext,
            reciterId,
            Intent(appContext, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(MainActivity.EXTRA_OPEN_DOWNLOADS_FOR_RECITER, reciterId),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setSubText(reciterSlug.replace('-', ' ')) // e.g. "Misyari Rasyid Al Afasi"
            .setContentIntent(openDownloads)
    }
}
