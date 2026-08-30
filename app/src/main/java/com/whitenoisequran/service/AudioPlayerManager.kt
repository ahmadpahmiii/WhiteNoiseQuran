package com.whitenoisequran.service

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var globalSleepFadeMultiplier: Float = 1.0f
    private var playlist: List<Surah> = emptyList()

    init {
        setupPlayerListeners()
        setupSleepTimerCallbacks()
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

            override fun onPlayerError(error: PlaybackException) {
                error.printStackTrace()
                _isPlaying.value = false
            }
        })
    }

    private fun setupSleepTimerCallbacks() {
        sleepTimerController.setCallbacks(
            onFinish = {
                pause()
                ambientSoundMixer.stopAll()
            },
            onFade = { multiplier ->
                globalSleepFadeMultiplier = multiplier
                exoPlayer.volume = (_quranVolume.value * multiplier).coerceIn(0f, 1f)
                ambientSoundMixer.fadeVolumeMultiplier(multiplier)
            }
        )
    }

    fun setQuranVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _quranVolume.value = clamped
        exoPlayer.volume = (clamped * globalSleepFadeMultiplier).coerceIn(0f, 1f)
    }

    fun updatePlaylist(surahs: List<Surah>, reciter: Reciter) {
        this.playlist = surahs
        this._currentReciter.value = reciter
        if (_currentSurah.value == null && surahs.isNotEmpty()) {
            _currentSurah.value = surahs.first()
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
            if (_currentSurah.value == null && playlist.isNotEmpty()) {
                playSurah(playlist.first())
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

    fun playNext() {
        if (playlist.isEmpty()) return
        val currentIndex = playlist.indexOfFirst { it.number == _currentSurah.value?.number }
        val nextIndex = (currentIndex + 1) % playlist.size
        playSurah(playlist[nextIndex])
    }

    fun playPrevious() {
        if (playlist.isEmpty()) return
        val currentIndex = playlist.indexOfFirst { it.number == _currentSurah.value?.number }
        val prevIndex = if (currentIndex <= 0) playlist.size - 1 else currentIndex - 1
        playSurah(playlist[prevIndex])
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
        return "https://cdn.equran.id/audio-full/$reciterSlug/${String.format("%03d", surah.number)}.mp3"
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
