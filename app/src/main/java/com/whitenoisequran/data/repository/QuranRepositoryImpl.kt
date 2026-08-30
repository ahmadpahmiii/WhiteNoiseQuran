package com.whitenoisequran.data.repository

import com.whitenoisequran.data.local.dao.ReciterDao
import com.whitenoisequran.data.local.dao.SurahDao
import com.whitenoisequran.data.local.entity.ReciterEntity
import com.whitenoisequran.data.local.entity.SurahEntity
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.data.remote.QuranMetadataRegistry
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuranRepositoryImpl @Inject constructor(
    private val surahDao: SurahDao,
    private val reciterDao: ReciterDao,
    private val appPreferences: AppPreferences
) : QuranRepository {

    override fun getSurahsFlow(reciterId: Int): Flow<List<Surah>> {
        return surahDao.getSurahsByReciter(reciterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSurahByNumber(number: Int, reciterId: Int): Surah? {
        return surahDao.getSurah(number, reciterId)?.toDomain()
    }

    override fun getRecitersFlow(): Flow<List<Reciter>> {
        return reciterDao.getAllReciters().map { entities ->
            if (entities.isEmpty()) {
                Reciter.DefaultReciters
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun getSelectedReciter(): Reciter {
        val selectedId = appPreferences.selectedReciterIdFlow.first()
        val entity = reciterDao.getReciterById(selectedId)
        return entity?.toDomain() ?: Reciter.DefaultReciters.first { it.id == 5 }
    }

    override suspend fun setSelectedReciter(reciter: Reciter) {
        appPreferences.setSelectedReciterId(reciter.id)
        seedSurahsForReciter(reciter.id)
    }

    override suspend fun seedInitialData() {
        if (reciterDao.getReciterCount() == 0) {
            val entities = Reciter.DefaultReciters.map { it.toEntity() }
            reciterDao.insertReciters(entities)
        }

        val defaultReciterId = 5 // Misyari Al-Afasy
        seedSurahsForReciter(defaultReciterId)
    }

    private suspend fun seedSurahsForReciter(reciterId: Int) {
        if (surahDao.getSurahCount(reciterId) == 0) {
            val surahEntities = QuranMetadataRegistry.allSurahs.map { info ->
                SurahEntity(
                    number = info.number,
                    reciterId = reciterId,
                    nameArabic = info.nameArabic,
                    nameLatin = info.nameLatin,
                    numberOfAyah = info.numberOfAyah,
                    revelationType = info.revelationType,
                    translationId = info.translation,
                    downloadState = DownloadState.NONE,
                    localFilePath = null
                )
            }
            surahDao.insertSurahs(surahEntities)
        }
    }

    private fun SurahEntity.toDomain(): Surah = Surah(
        number = number,
        nameArabic = nameArabic,
        nameLatin = nameLatin,
        numberOfAyah = numberOfAyah,
        revelationType = revelationType,
        translationId = translationId,
        downloadState = downloadState,
        localFilePath = localFilePath,
        audioDurationMs = audioDurationMs
    )

    private fun Reciter.toEntity(): ReciterEntity = ReciterEntity(
        id = id,
        name = name,
        nameArabic = nameArabic,
        slug = slug,
        apiKey = apiKey,
        isPopular = isPopular,
        avatarInitial = avatarInitial
    )

    private fun ReciterEntity.toDomain(): Reciter = Reciter(
        id = id,
        name = name,
        nameArabic = nameArabic,
        slug = slug,
        apiKey = apiKey,
        isPopular = isPopular,
        avatarInitial = avatarInitial
    )
}
