package com.whitenoisequran.domain.usecase

import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartBulkDownloadUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(reciterId: Int, reciterSlug: String) {
        downloadRepository.startBulkDownload(reciterId, reciterSlug)
    }
}

class ObserveDownloadProgressUseCase @Inject constructor(
    private val downloadRepository: DownloadRepository
) {
    operator fun invoke(reciterId: Int): Flow<BulkDownloadProgress> {
        return downloadRepository.getDownloadProgressFlow(reciterId)
    }
}
