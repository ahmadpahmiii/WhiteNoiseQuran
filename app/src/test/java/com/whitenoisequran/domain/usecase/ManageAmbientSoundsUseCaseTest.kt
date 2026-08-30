package com.whitenoisequran.domain.usecase

import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.repository.AmbientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ManageAmbientSoundsUseCaseTest {

    private lateinit var fakeRepository: FakeAmbientRepository
    private lateinit var useCase: ManageAmbientSoundsUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeAmbientRepository()
        useCase = ManageAmbientSoundsUseCase(fakeRepository)
    }

    @Test
    fun testGetAmbientSounds() = runTest {
        val sounds = useCase.getAmbientSounds().first()
        assertEquals(6, sounds.size)
    }

    @Test
    fun testToggleSound() = runTest {
        useCase.toggleSound("calming_rain", true)
        val sounds = useCase.getAmbientSounds().first()
        val calmingRain = sounds.first { it.id == "calming_rain" }
        assertTrue(calmingRain.isEnabled)

        useCase.toggleSound("calming_rain", false)
        val updatedSounds = useCase.getAmbientSounds().first()
        assertFalse(updatedSounds.first { it.id == "calming_rain" }.isEnabled)
    }

    @Test
    fun testUpdateVolume() = runTest {
        useCase.updateVolume("ocean", 0.85f)
        val sounds = useCase.getAmbientSounds().first()
        val ocean = sounds.first { it.id == "ocean" }
        assertEquals(0.85f, ocean.volume, 0.01f)
    }

    @Test
    fun testResetAll() = runTest {
        useCase.toggleSound("calming_rain", true)
        useCase.toggleSound("ocean", true)
        var sounds = useCase.getAmbientSounds().first()
        assertEquals(2, sounds.count { it.isEnabled })

        useCase.resetAll()
        sounds = useCase.getAmbientSounds().first()
        assertEquals(0, sounds.count { it.isEnabled })
    }

    private class FakeAmbientRepository : AmbientRepository {
        private val soundsFlow = MutableStateFlow(AmbientSound.DefaultSounds)

        override fun getAmbientSoundsFlow(): Flow<List<AmbientSound>> = soundsFlow

        override suspend fun updateSoundVolume(soundId: String, volume: Float) {
            soundsFlow.value = soundsFlow.value.map {
                if (it.id == soundId) it.copy(volume = volume) else it
            }
        }

        override suspend fun toggleSound(soundId: String, isEnabled: Boolean) {
            soundsFlow.value = soundsFlow.value.map {
                if (it.id == soundId) it.copy(isEnabled = isEnabled) else it
            }
        }

        override suspend fun resetAllSounds() {
            soundsFlow.value = soundsFlow.value.map { it.copy(isEnabled = false) }
        }
    }
}
