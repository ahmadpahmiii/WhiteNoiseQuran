package com.whitenoisequran.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.QuranRepository
import com.whitenoisequran.domain.usecase.ManageAmbientSoundsUseCase
import com.whitenoisequran.service.AmbientSoundMixer
import com.whitenoisequran.service.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val manageAmbientSoundsUseCase: ManageAmbientSoundsUseCase,
    val audioPlayerManager: AudioPlayerManager,
    private val ambientSoundMixer: AmbientSoundMixer,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainUiState(
            ambientSounds = AmbientSound.DefaultSounds
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observePlayerState()
    }

    private fun loadData() {
        viewModelScope.launch {
            val reciter = quranRepository.getSelectedReciter()
            _uiState.update { it.copy(currentReciter = reciter) }

            quranRepository.getSurahsFlow(reciter.id).collect { surahs ->
                _uiState.update { current ->
                    current.copy(
                        surahs = surahs,
                        currentSurah = current.currentSurah ?: surahs.firstOrNull()
                    )
                }
                audioPlayerManager.updatePlaylist(surahs, reciter)
            }
        }

        // Ambient Sounds observation
        viewModelScope.launch {
            manageAmbientSoundsUseCase.getAmbientSounds().collect { sounds ->
                _uiState.update { it.copy(ambientSounds = sounds.ifEmpty { AmbientSound.DefaultSounds }) }
                sounds.forEach { sound ->
                    ambientSoundMixer.setSoundActive(sound.id, sound.isEnabled, sound.volume)
                }
            }
        }
    }

    private fun observePlayerState() {
        viewModelScope.launch {
            audioPlayerManager.isPlaying.collect { isPlaying ->
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.currentSurah.collect { surah ->
                if (surah != null) {
                    _uiState.update { it.copy(currentSurah = surah) }
                }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.currentPositionMs.collect { pos ->
                _uiState.update { it.copy(playbackPositionMs = pos) }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.durationMs.collect { dur ->
                _uiState.update { it.copy(playbackDurationMs = dur) }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.isShuffle.collect { shuffle ->
                _uiState.update { it.copy(isShuffle = shuffle) }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.sleepTimerController.isTimerActive.collect { active ->
                val remaining = if (active) {
                    audioPlayerManager.sleepTimerController.formatRemainingTime()
                } else null
                _uiState.update {
                    it.copy(
                        isSleepTimerActive = active,
                        sleepTimerRemainingText = remaining
                    )
                }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.sleepTimerController.remainingSeconds.collect {
                if (audioPlayerManager.sleepTimerController.isTimerActive.value) {
                    _uiState.update {
                        it.copy(sleepTimerRemainingText = audioPlayerManager.sleepTimerController.formatRemainingTime())
                    }
                }
            }
        }
    }

    fun onPlayPause() {
        audioPlayerManager.togglePlayPause()
    }

    fun onPrevious() {
        audioPlayerManager.playPrevious()
    }

    fun onNext() {
        audioPlayerManager.playNext()
    }

    fun onSeek(positionMs: Long) {
        audioPlayerManager.seekTo(positionMs)
    }

    fun onToggleShuffle() {
        audioPlayerManager.toggleShuffle()
    }

    fun onSelectSurah(surah: Surah) {
        audioPlayerManager.playSurah(surah)
    }

    fun onUpdateSoundVolume(soundId: String, volume: Float) {
        viewModelScope.launch {
            manageAmbientSoundsUseCase.updateVolume(soundId, volume)
            ambientSoundMixer.setSoundVolume(soundId, volume)
        }
    }

    fun onToggleSound(soundId: String, isEnabled: Boolean) {
        viewModelScope.launch {
            manageAmbientSoundsUseCase.toggleSound(soundId, isEnabled)
            val sound = uiState.value.ambientSounds.find { it.id == soundId }
            ambientSoundMixer.setSoundActive(soundId, isEnabled, sound?.volume ?: 0.5f)
        }
    }

    fun onResetAllSounds() {
        viewModelScope.launch {
            manageAmbientSoundsUseCase.resetAll()
            ambientSoundMixer.stopAll()
        }
    }

    fun onSetSleepTimer(minutes: Int) {
        audioPlayerManager.sleepTimerController.startTimer(minutes)
    }

    fun onCancelSleepTimer() {
        audioPlayerManager.sleepTimerController.cancelTimer()
    }

    fun openSurahSheet() {
        _uiState.update { it.copy(isSurahSheetOpen = true) }
    }

    fun closeSurahSheet() {
        _uiState.update { it.copy(isSurahSheetOpen = false) }
    }

    fun openSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetOpen = true) }
    }

    fun closeSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetOpen = false) }
    }

    fun openReciterSheet() {
        _uiState.update { it.copy(isReciterSheetOpen = true) }
    }

    fun closeReciterSheet() {
        _uiState.update { it.copy(isReciterSheetOpen = false) }
    }
}
