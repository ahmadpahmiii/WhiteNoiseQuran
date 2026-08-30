package com.whitenoisequran.domain.usecase

import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSurahsUseCase @Inject constructor(
    private val quranRepository: QuranRepository
) {
    operator fun invoke(reciterId: Int): Flow<List<Surah>> {
        return quranRepository.getSurahsFlow(reciterId)
    }
}
