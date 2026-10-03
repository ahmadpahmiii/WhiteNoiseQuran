package com.whitenoisequran.domain.repository

import com.whitenoisequran.domain.model.BulkDownloadProgress
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getDownloadProgressFlow(reciterId: Int): Flow<BulkDownloadProgress>
    suspend fun startBulkDownload(reciterId: Int, reciterSlug: String)
    suspend fun pauseOrCancelDownload(reciterId: Int)
    suspend fun downloadSingleSurah(surahNumber: Int, reciterId: Int, reciterSlug: String)
    suspend fun cancelSurahDownload(surahNumber: Int, reciterId: Int)

    /** Downloaded surah count per reciter id (reciters with none are missing). */
    fun getDownloadedCountsFlow(): Flow<Map<Int, Int>>

    /** Disk space used by a reciter's audio, including partly downloaded files. */
    suspend fun getAudioSizeBytes(reciterSlug: String): Long

    /** Size of these surahs on the server, or null when it can't be checked (e.g. offline). */
    suspend fun getDownloadSizeBytes(reciterSlug: String, surahNumbers: List<Int>): Long?
    suspend fun deleteSurahAudio(surahNumber: Int, reciterId: Int, reciterSlug: String)
    suspend fun deleteAllAudio(reciterId: Int, reciterSlug: String)
}
