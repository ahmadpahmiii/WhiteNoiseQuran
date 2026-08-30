package com.whitenoisequran.domain.repository

import com.whitenoisequran.domain.model.AmbientSound
import kotlinx.coroutines.flow.Flow

interface AmbientRepository {
    fun getAmbientSoundsFlow(): Flow<List<AmbientSound>>
    suspend fun updateSoundVolume(soundId: String, volume: Float)
    suspend fun toggleSound(soundId: String, isEnabled: Boolean)
    suspend fun resetAllSounds()
}
