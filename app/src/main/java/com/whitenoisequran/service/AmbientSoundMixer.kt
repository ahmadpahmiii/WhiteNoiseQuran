package com.whitenoisequran.service

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.whitenoisequran.domain.model.AmbientSound
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmbientSoundMixer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Map: soundId -> ExoPlayer instance
    private val players = ConcurrentHashMap<String, ExoPlayer>()
    // Map: soundId -> targetVolume (0f..1f)
    private val soundVolumes = ConcurrentHashMap<String, Float>()

    // Set: soundIds in the mix; kept while paused or stopped, so the mix comes back as it was
    private val activeSoundIds = ConcurrentHashMap.newKeySet<String>()

    @Volatile
    private var mixPlaying = false

    private val _isPlaying = MutableStateFlow(false)

    /** True while the mix is audible: playing, with at least one sound in it. */
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var globalFadeMultiplier: Float = 1.0f

    private fun publishIsPlaying() {
        _isPlaying.value = mixPlaying && activeSoundIds.isNotEmpty()
    }

    private fun getOrCreatePlayer(soundId: String): ExoPlayer? {
        val existing = players[soundId]
        if (existing != null) return existing

        val sound = AmbientSound.DefaultSounds.find { it.id == soundId } ?: return null
        val resId = context.resources.getIdentifier(sound.rawResName, "raw", context.packageName)
        if (resId == 0) return null

        val rawUri = "android.resource://${context.packageName}/$resId"
        val player = ExoPlayer.Builder(context)
            .setWakeMode(C.WAKE_MODE_LOCAL) // keeps looping with the screen off
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ALL
                val mediaItem = MediaItem.fromUri(rawUri)
                setMediaItem(mediaItem)
                prepare()
            }
        players[soundId] = player
        return player
    }

    /** Adds a sound to the mix or removes it; an added sound joins in if the mix is playing. */
    fun select(soundId: String, isEnabled: Boolean, volume: Float) {
        soundVolumes[soundId] = volume
        if (isEnabled) activeSoundIds.add(soundId) else activeSoundIds.remove(soundId)
        publishIsPlaying()
        scope.launch {
            if (isEnabled) {
                if (!mixPlaying) return@launch
                val player = getOrCreatePlayer(soundId) ?: return@launch
                player.volume = (volume * globalFadeMultiplier).coerceIn(0f, 1f)
                if (!player.isPlaying) {
                    player.play()
                }
            } else {
                val player = players[soundId] ?: return@launch
                if (player.isPlaying) {
                    player.pause()
                    player.seekTo(0)
                }
            }
        }
    }

    fun setSoundVolume(soundId: String, volume: Float) {
        soundVolumes[soundId] = volume
        scope.launch {
            val player = players[soundId] ?: return@launch
            player.volume = (volume * globalFadeMultiplier).coerceIn(0f, 1f)
        }
    }

    fun pauseAll() {
        mixPlaying = false
        publishIsPlaying()
        scope.launch {
            players.values.forEach { player ->
                if (player.isPlaying) {
                    player.pause()
                }
            }
        }
    }

    fun resumeAll() {
        mixPlaying = true
        publishIsPlaying()
        // Rain alone needs the playback service too, or Android stops it in the background
        if (activeSoundIds.isNotEmpty()) AudioPlaybackService.start(context)
        scope.launch {
            activeSoundIds.forEach { soundId ->
                val player = getOrCreatePlayer(soundId) ?: return@forEach
                val vol = soundVolumes[soundId] ?: 0.5f
                player.volume = (vol * globalFadeMultiplier).coerceIn(0f, 1f)
                player.play()
            }
        }
    }

    /** Stops every sound; the mix stays selected for next time. */
    fun stopAll() {
        mixPlaying = false
        publishIsPlaying()
        scope.launch {
            players.values.forEach { player ->
                player.pause()
                player.seekTo(0)
            }
        }
    }

    fun fadeVolumeMultiplier(multiplier: Float) {
        globalFadeMultiplier = multiplier.coerceIn(0f, 1f)
        scope.launch {
            players.forEach { (soundId, player) ->
                val baseVol = soundVolumes[soundId] ?: 0.5f
                player.volume = (baseVol * globalFadeMultiplier).coerceIn(0f, 1f)
            }
        }
    }

    fun release() {
        mixPlaying = false
        activeSoundIds.clear()
        publishIsPlaying()
        scope.launch {
            players.values.forEach { it.release() }
            players.clear()
        }
    }
}
