package com.whitenoisequran.domain.usecase

import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecitersUseCase @Inject constructor(
    private val quranRepository: QuranRepository
) {
    fun getReciters(): Flow<List<Reciter>> = quranRepository.getRecitersFlow()

    suspend fun getSelectedReciter(): Reciter = quranRepository.getSelectedReciter()

    suspend fun selectReciter(reciter: Reciter) {
        quranRepository.setSelectedReciter(reciter)
    }
}
