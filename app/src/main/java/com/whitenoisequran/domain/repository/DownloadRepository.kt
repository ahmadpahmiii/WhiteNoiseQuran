package com.whitenoisequran.domain.repository

import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getDownloadProgressFlow(reciterId: Int): Flow<BulkDownloadProgress>
    suspend fun startBulkDownload(reciterId: Int, reciterSlug: String)
    suspend fun pauseOrCancelDownload(reciterId: Int)
    suspend fun updateSurahDownloadState(surahNumber: Int, reciterId: Int, state: DownloadState, localPath: String?)
    suspend fun isReciterAudioDownloaded(reciterId: Int): Boolean
    suspend fun downloadSingleSurah(surahNumber: Int, reciterId: Int, reciterSlug: String)
    suspend fun deleteSurahAudio(surahNumber: Int, reciterId: Int, reciterSlug: String)
    suspend fun deleteAllAudio(reciterId: Int, reciterSlug: String)
}
