package com.whitenoisequran.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.R
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.DownloadRepository
import com.whitenoisequran.domain.repository.QuranRepository
import com.whitenoisequran.domain.usecase.ManageAmbientSoundsUseCase
import com.whitenoisequran.service.AmbientSoundMixer
import com.whitenoisequran.service.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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

    private val _messages = Channel<String>(Channel.BUFFERED)

    /** One-off messages for a snackbar. */
    val messages: Flow<String> = merge(
        _messages.receiveAsFlow(),
        audioPlayerManager.playbackErrors.map { surah ->
            val downloaded =
                uiState.value.surahs.find { it.number == surah.number }?.downloadState == DownloadState.DONE
            context.getString(
                if (downloaded) R.string.msg_play_failed_downloaded else R.string.msg_play_failed_not_downloaded,
                surah.nameLatin
            )
        }
    )

    init {
        loadData()
        observePlayerState()
    }

    private fun loadData() {
        // Reload whenever the reciter changes; collectLatest drops the previous reciter's flows
        viewModelScope.launch {
            appPreferences.selectedReciterIdFlow.distinctUntilChanged().collectLatest {
                loadReciter(quranRepository.getSelectedReciter())
            }
        }

        viewModelScope.launch {
            combine(
                quranRepository.getRecitersFlow(),
                downloadRepository.getDownloadedCountsFlow(),
                ::Pair
            )
                .collect { (reciters, counts) ->
                    _uiState.update { it.copy(reciters = reciters, reciterDownloadCounts = counts) }
                }
        }

        // Ambient Sounds observation
        viewModelScope.launch {
            manageAmbientSoundsUseCase.getAmbientSounds().collect { sounds ->
                _uiState.update { it.copy(ambientSounds = sounds.ifEmpty { AmbientSound.DefaultSounds }) }
                // Restores the saved mix without playing it
                sounds.forEach { sound ->
                    ambientSoundMixer.select(sound.id, sound.isEnabled, sound.volume)
                }
            }
        }
    }

    private suspend fun loadReciter(reciter: Reciter): Unit = coroutineScope {
        _uiState.update { it.copy(currentReciter = reciter, isLoadingSurahs = true) }

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

    private fun observePlayerState() {
        viewModelScope.launch {
            audioPlayerManager.isPlaying.collect { isPlaying ->
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }
        }

        viewModelScope.launch {
            audioPlayerManager.isBuffering.collect { buffering ->
                _uiState.update { it.copy(isBuffering = buffering) }
            }
        }

        viewModelScope.launch {
            ambientSoundMixer.isPlaying.collect { playing ->
                _uiState.update { it.copy(isAmbientPlaying = playing) }
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

    fun onCancelSurahDownload(surah: Surah) {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.cancelSurahDownload(surah.number, reciter.id)
        }
    }

    /** "Free Space" asks first, showing how much it frees. */
    fun onRequestDeleteAllAudio() {
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            val bytes = downloadRepository.getAudioSizeBytes(reciter.slug)
            _uiState.update { it.copy(deleteAllConfirmBytes = bytes) }
        }
    }

    fun dismissDeleteAllAudio() {
        _uiState.update { it.copy(deleteAllConfirmBytes = null) }
    }

    fun onDeleteAllAudio() {
        dismissDeleteAllAudio()
        val reciter = uiState.value.currentReciter ?: return
        viewModelScope.launch {
            downloadRepository.deleteAllAudio(reciter.id, reciter.slug)
        }
    }

    fun onSelectReciter(reciter: Reciter) {
        closeReciterSheet()
        val previous = uiState.value.currentReciter
        if (previous?.id == reciter.id) return
        val wasDownloading = uiState.value.downloadProgress.isRunning
        viewModelScope.launch {
            // One download at a time, for the reciter in use; the paused one resumes from its Surah Index
            if (previous != null && wasDownloading) {
                downloadRepository.pauseOrCancelDownload(previous.id)
                _messages.send(
                    context.getString(
                        R.string.msg_download_paused_for_reciter,
                        previous.name
                    )
                )
            }
            quranRepository.setSelectedReciter(reciter) // loadData reloads on the change
        }
    }

    /** "Download All" asks first, with the size, since a full reciter is gigabytes. */
    fun onRequestDownloadAll() {
        val reciter = uiState.value.currentReciter ?: return
        val missing = uiState.value.surahs.count { it.downloadState != DownloadState.DONE }
        viewModelScope.launch {
            val bytes = downloadRepository.getRemainingDownloadBytes(reciter.slug)
            _uiState.update {
                it.copy(
                    downloadConfirm = DownloadConfirm(
                        surahCount = missing,
                        bytes = bytes
                    )
                )
            }
        }
    }

    fun dismissDownloadAll() {
        _uiState.update { it.copy(downloadConfirm = null) }
    }

    fun onUpdateSoundVolume(soundId: String, volume: Float) {
        viewModelScope.launch {
            manageAmbientSoundsUseCase.updateVolume(soundId, volume)
            ambientSoundMixer.setSoundVolume(soundId, volume)
        }
    }

    fun onToggleSound(soundId: String, isEnabled: Boolean) {
        val sound = uiState.value.ambientSounds.find { it.id == soundId }
        ambientSoundMixer.select(soundId, isEnabled, sound?.volume ?: 0.5f)
        if (isEnabled) ambientSoundMixer.resumeAll() // turning a sound on plays the mix
        viewModelScope.launch {
            manageAmbientSoundsUseCase.toggleSound(soundId, isEnabled)
        }
    }

    fun onToggleAmbientMix() {
        if (ambientSoundMixer.isPlaying.value) ambientSoundMixer.pauseAll() else ambientSoundMixer.resumeAll()
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
