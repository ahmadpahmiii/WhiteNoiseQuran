package com.whitenoisequran.domain.repository

import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    fun getSurahsFlow(reciterId: Int): Flow<List<Surah>>
    suspend fun getSurahByNumber(number: Int, reciterId: Int): Surah?
    fun getRecitersFlow(): Flow<List<Reciter>>
    suspend fun getSelectedReciter(): Reciter
    suspend fun setSelectedReciter(reciter: Reciter)
    suspend fun seedInitialData()
}
