package com.whitenoisequran.domain.usecase

import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.repository.AmbientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageAmbientSoundsUseCase @Inject constructor(
    private val ambientRepository: AmbientRepository
) {
    fun getAmbientSounds(): Flow<List<AmbientSound>> = ambientRepository.getAmbientSoundsFlow()

    suspend fun updateVolume(soundId: String, volume: Float) {
        ambientRepository.updateSoundVolume(soundId, volume)
    }

    suspend fun toggleSound(soundId: String, isEnabled: Boolean) {
        ambientRepository.toggleSound(soundId, isEnabled)
    }

    suspend fun resetAll() {
        ambientRepository.resetAllSounds()
    }
}
