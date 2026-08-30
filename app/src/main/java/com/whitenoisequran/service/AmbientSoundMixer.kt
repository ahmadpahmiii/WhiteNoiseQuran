package com.whitenoisequran.service

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.whitenoisequran.domain.model.AmbientSound
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    // Set: soundIds that are enabled and should be playing
    private val activeSoundIds = ConcurrentHashMap.newKeySet<String>()

    private var globalFadeMultiplier: Float = 1.0f

    private fun getOrCreatePlayer(soundId: String): ExoPlayer? {
        val existing = players[soundId]
        if (existing != null) return existing

        val sound = AmbientSound.DefaultSounds.find { it.id == soundId } ?: return null
        val resId = context.resources.getIdentifier(sound.rawResName, "raw", context.packageName)
        if (resId == 0) return null

        val rawUri = "android.resource://${context.packageName}/$resId"
        val player = ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            val mediaItem = MediaItem.fromUri(rawUri)
            setMediaItem(mediaItem)
            prepare()
        }
        players[soundId] = player
        return player
    }

    fun setSoundActive(soundId: String, isEnabled: Boolean, volume: Float) {
        soundVolumes[soundId] = volume
        scope.launch {
            val player = getOrCreatePlayer(soundId) ?: return@launch
            if (isEnabled) {
                activeSoundIds.add(soundId)
                player.volume = (volume * globalFadeMultiplier).coerceIn(0f, 1f)
                if (!player.isPlaying) {
                    player.play()
                }
            } else {
                activeSoundIds.remove(soundId)
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
        scope.launch {
            players.values.forEach { player ->
                if (player.isPlaying) {
                    player.pause()
                }
            }
        }
    }

    fun resumeAll() {
        scope.launch {
            activeSoundIds.forEach { soundId ->
                val player = players[soundId]
                val vol = soundVolumes[soundId] ?: 0.5f
                player?.volume = (vol * globalFadeMultiplier).coerceIn(0f, 1f)
                player?.play()
            }
        }
    }

    fun stopAll() {
        activeSoundIds.clear()
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
        activeSoundIds.clear()
        scope.launch {
            players.values.forEach { it.release() }
            players.clear()
        }
    }
}
