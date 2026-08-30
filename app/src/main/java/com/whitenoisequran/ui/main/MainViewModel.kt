package com.whitenoisequran.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.DownloadRepository
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
    private val downloadRepository: DownloadRepository,
    private val manageAmbientSoundsUseCase: ManageAmbientSoundsUseCase,
    val audioPlayerManager: AudioPlayerManager,
    private val ambientSoundMixer: AmbientSoundMixer,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainUiState(
            ambientSounds = AmbientSound.DefaultSounds,
            isLoadingSurahs = true
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            manageAmbientSoundsUseCase.resetAll()
        }
        loadData()
        observePlayerState()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSurahs = true) }
            val reciter = quranRepository.getSelectedReciter()
            _uiState.update { it.copy(currentReciter = reciter) }

            // Observe Surahs
            launch {
                quranRepository.getSurahsFlow(reciter.id).collect { surahs ->
                    _uiState.update { current ->
                        current.copy(
                            surahs = surahs,
                            currentSurah = current.currentSurah ?: surahs.firstOrNull(),
                            isLoadingSurahs = false
                        )
                    }
                    audioPlayerManager.updatePlaylist(surahs, reciter)
                }
            }

            // Observe Download Progress
            launch {
                downloadRepository.getDownloadProgressFlow(reciter.id).collect { progress ->
                    _uiState.update { it.copy(downloadProgress = progress) }
                }
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
            audioPlayerManager.quranVolume.collect { vol ->
                _uiState.update { it.copy(quranVolume = vol) }
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

    fun onSelectSurah(surah: Surah) {
        audioPlayerManager.playSurah(surah)
    }

    fun onQuranVolumeChange(volume: Float) {
        audioPlayerManager.setQuranVolume(volume)
    }

    fun onDownloadSingleSurah(surah: Surah) {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.downloadSingleSurah(surah.number, reciter.id, reciter.slug)
        }
    }

    fun onDeleteSurahAudio(surah: Surah) {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.deleteSurahAudio(surah.number, reciter.id, reciter.slug)
        }
    }

    fun onDownloadAllSurahs() {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.startBulkDownload(reciter.id, reciter.slug)
        }
    }

    fun onDeleteAllAudio() {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.deleteAllAudio(reciter.id, reciter.slug)
        }
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
