package com.whitenoisequran.data.repository

import com.whitenoisequran.data.local.dao.AmbientSoundDao
import com.whitenoisequran.data.local.entity.AmbientSoundEntity
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.repository.AmbientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmbientRepositoryImpl @Inject constructor(
    private val ambientSoundDao: AmbientSoundDao
) : AmbientRepository {

    override fun getAmbientSoundsFlow(): Flow<List<AmbientSound>> {
        return ambientSoundDao.getAllSounds().map { entities ->
            if (entities.isEmpty()) {
                AmbientSound.DefaultSounds
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun updateSoundVolume(soundId: String, volume: Float) {
        seedIfEmpty()
        ambientSoundDao.updateVolume(soundId, volume)
    }

    override suspend fun toggleSound(soundId: String, isEnabled: Boolean) {
        seedIfEmpty()
        ambientSoundDao.updateEnabled(soundId, isEnabled)
    }

    override suspend fun resetAllSounds() {
        ambientSoundDao.disableAll()
    }

    private suspend fun seedIfEmpty() {
        if (ambientSoundDao.getSoundCount() == 0) {
            val entities = AmbientSound.DefaultSounds.map { it.toEntity() }
            ambientSoundDao.insertSounds(entities)
        }
    }

    private fun AmbientSoundEntity.toDomain(): AmbientSound = AmbientSound(
        id = id,
        name = name,
        iconEmoji = iconEmoji,
        rawResName = rawResName,
        volume = volume,
        isEnabled = isEnabled,
        sortOrder = sortOrder
    )

    private fun AmbientSound.toEntity(): AmbientSoundEntity = AmbientSoundEntity(
        id = id,
        name = name,
        iconEmoji = iconEmoji,
        rawResName = rawResName,
        volume = volume,
        isEnabled = isEnabled,
        sortOrder = sortOrder
    )
}
