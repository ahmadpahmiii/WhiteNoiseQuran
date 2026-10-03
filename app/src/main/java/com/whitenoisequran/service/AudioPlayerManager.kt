package com.whitenoisequran.service

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.whitenoisequran.data.isOnline
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.data.remote.QuranMetadataRegistry
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ambientSoundMixer: AmbientSoundMixer,
    val sleepTimerController: SleepTimerController,
    private val appPreferences: AppPreferences
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    val player: ExoPlayer get() = exoPlayer

    private var progressTrackingJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)

    /** Wants to play but is still loading audio (streaming, or a slow connection). */
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentSurah = MutableStateFlow<Surah?>(null)
    val currentSurah: StateFlow<Surah?> = _currentSurah.asStateFlow()

    private val _currentReciter = MutableStateFlow<Reciter?>(null)
    val currentReciter: StateFlow<Reciter?> = _currentReciter.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(1L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _quranVolume = MutableStateFlow(1.0f)
    val quranVolume: StateFlow<Float> = _quranVolume.asStateFlow()

    private val _playbackErrors = MutableSharedFlow<Surah>(extraBufferCapacity = 1)

    /** Surahs that failed to play, e.g. not downloaded while offline. */
    val playbackErrors: SharedFlow<Surah> = _playbackErrors.asSharedFlow()

    private var globalSleepFadeMultiplier: Float = 1.0f
    private var playlist: List<Surah> = emptyList()

    init {
        setupPlayerListeners()
        setupSleepTimerCallbacks()
        restoreQuranVolume()
    }

    private fun setupPlayerListeners() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    startProgressTracker()
                    ambientSoundMixer.resumeAll()
                    ensureForegroundServiceStarted()
                } else {
                    stopProgressTracker()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    val duration = exoPlayer.duration
                    if (duration > 0) {
                        _durationMs.value = duration
                    }
                } else if (playbackState == Player.STATE_ENDED) {
                    playNext()
                }
            }

            override fun onEvents(player: Player, events: Player.Events) {
                _isBuffering.value =
                    player.playbackState == Player.STATE_BUFFERING && player.playWhenReady
            }

            override fun onPlayerError(error: PlaybackException) {
                error.printStackTrace()
                _isPlaying.value = false
                _currentSurah.value?.let { _playbackErrors.tryEmit(it) }
            }
        })
    }

    private fun setupSleepTimerCallbacks() {
        // Timer callbacks fire off the main thread; ExoPlayer must only be touched on main.
        sleepTimerController.setCallbacks(
            onFinish = {
                scope.launch {
                    pause()
                    ambientSoundMixer.stopAll()
                    applySleepFade(1.0f) // restore volume so the next playback isn't silent
                }
            },
            onFade = { multiplier -> scope.launch { applySleepFade(multiplier) } }
        )
    }

    private fun applySleepFade(multiplier: Float) {
        globalSleepFadeMultiplier = multiplier
        exoPlayer.volume = (_quranVolume.value * multiplier).coerceIn(0f, 1f)
        ambientSoundMixer.fadeVolumeMultiplier(multiplier)
    }

    private fun restoreQuranVolume() {
        scope.launch {
            val saved = appPreferences.quranVolumeFlow.first()
            _quranVolume.value = saved
            exoPlayer.volume = (saved * globalSleepFadeMultiplier).coerceIn(0f, 1f)
        }
    }

    fun setQuranVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _quranVolume.value = clamped
        exoPlayer.volume = (clamped * globalSleepFadeMultiplier).coerceIn(0f, 1f)
        scope.launch { appPreferences.setQuranVolume(clamped) }
    }

    fun updatePlaylist(surahs: List<Surah>, reciter: Reciter) {
        val reciterChanged = _currentReciter.value.let { it != null && it.id != reciter.id }
        this.playlist = surahs
        this._currentReciter.value = reciter
        val current = _currentSurah.value
        if (current == null) {
            if (surahs.isNotEmpty()) {
                scope.launch {
                    val lastPlayed = appPreferences.lastPlayedSurahFlow.first()
                    if (_currentSurah.value == null) {
                        _currentSurah.value =
                            surahs.find { it.number == lastPlayed } ?: surahs.first()
                    }
                }
            }
            return
        }
        // Same surah from the new list: fresh download state, and the new reciter's file after a switch
        val fresh = surahs.find { it.number == current.number } ?: return
        _currentSurah.value = fresh
        if (reciterChanged && exoPlayer.mediaItemCount > 0) {
            if (exoPlayer.isPlaying) {
                playSurah(fresh) // hear the chosen reciter right away
            } else {
                exoPlayer.clearMediaItems() // play loads the new reciter's audio
                _currentPositionMs.value = 0L
                _durationMs.value = 1L
            }
        }
    }

    fun playSurah(surah: Surah, reciter: Reciter? = _currentReciter.value) {
        _currentSurah.value = surah
        if (reciter != null) _currentReciter.value = reciter

        val reciterSlug = reciter?.slug ?: "Misyari-Rasyid-Al-Afasi"
        val mediaUri = getAudioUri(surah, reciterSlug)

        val metadata = MediaMetadata.Builder()
            .setTitle("${surah.number}. ${surah.nameLatin} (${surah.nameArabic})")
            .setSubtitle(reciter?.name ?: "White Noise Quran")
            .setArtist(reciter?.name ?: "White Noise Quran")
            .setAlbumTitle("White Noise Quran")
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(mediaUri)
            .setMediaMetadata(metadata)
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _isPlaying.value = true
        ensureForegroundServiceStarted()

        scope.launch {
            appPreferences.setLastPlayedSurah(surah.number)
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            val surah = _currentSurah.value ?: playlist.firstOrNull() ?: return
            if (exoPlayer.mediaItemCount == 0 || exoPlayer.playbackState == Player.STATE_IDLE) {
                // Nothing loaded yet (e.g. restored surah after app restart), or the last load failed: load again
                playSurah(surah)
            } else {
                exoPlayer.play()
                _isPlaying.value = true
                ensureForegroundServiceStarted()
            }
        }
    }

    fun pause() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    fun playNext() = playAdjacent(1)

    fun playPrevious() = playAdjacent(-1)

    private fun playAdjacent(step: Int) {
        if (playlist.isEmpty()) return
        val currentIndex = playlist.indexOfFirst { it.number == _currentSurah.value?.number }
        val candidates = (1..playlist.size).map {
            playlist[Math.floorMod(
                currentIndex + step * it,
                playlist.size
            )]
        }
        // Offline only downloaded surahs can play (e.g. auto-advance at night): skip the others.
        // None downloaded: play the next anyway so its error tells the user why.
        val next = candidates.firstOrNull { it.downloadState == DownloadState.DONE }
            .takeUnless { context.isOnline() } ?: candidates.first()
        playSurah(next)
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        _currentPositionMs.value = positionMs
    }

    private fun ensureForegroundServiceStarted() {
        try {
            val intent = Intent(context, AudioPlaybackService::class.java)
            context.startService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAudioUri(surah: Surah, reciterSlug: String): String {
        // Priority 1: Check if local file exists
        if (!surah.localFilePath.isNullOrEmpty()) {
            val localFile = File(surah.localFilePath)
            if (localFile.exists() && localFile.length() > 10_000) {
                return localFile.toURI().toString()
            }
        }

        // Priority 2: Check standard local cache directory
        val localAudio = File(context.filesDir, "audio/$reciterSlug/${String.format("%03d", surah.number)}.mp3")
        if (localAudio.exists() && localAudio.length() > 10_000) {
            return localAudio.toURI().toString()
        }

        // Priority 3: Fallback to CDN stream URL
        return QuranMetadataRegistry.audioUrl(reciterSlug, surah.number)
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressTrackingJob = scope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    _currentPositionMs.value = exoPlayer.currentPosition
                    val dur = exoPlayer.duration
                    if (dur > 0) _durationMs.value = dur
                }
                delay(300L)
            }
        }
    }

    private fun stopProgressTracker() {
        progressTrackingJob?.cancel()
        progressTrackingJob = null
    }

    fun release() {
        stopProgressTracker()
        exoPlayer.release()
        ambientSoundMixer.release()
    }
}
