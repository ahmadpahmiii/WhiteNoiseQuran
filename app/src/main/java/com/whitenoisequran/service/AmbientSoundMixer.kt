package com.whitenoisequran.service

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
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
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(8)
        .setAudioAttributes(audioAttributes)
        .build()

    // Map: soundId -> soundPoolId
    private val loadedSoundIds = ConcurrentHashMap<String, Int>()
    // Map: soundId -> activeStreamId
    private val activeStreams = ConcurrentHashMap<String, Int>()
    // Map: soundId -> targetVolume
    private val soundVolumes = ConcurrentHashMap<String, Float>()

    init {
        preloadAmbientSounds()
    }

    private fun preloadAmbientSounds() {
        AmbientSound.DefaultSounds.forEach { sound ->
            val resId = context.resources.getIdentifier(sound.rawResName, "raw", context.packageName)
            if (resId != 0) {
                val soundId = soundPool.load(context, resId, 1)
                loadedSoundIds[sound.id] = soundId
            }
        }
    }

    fun setSoundActive(soundId: String, isEnabled: Boolean, volume: Float) {
        soundVolumes[soundId] = volume
        if (isEnabled) {
            startLoopingSound(soundId, volume)
        } else {
            stopSound(soundId)
        }
    }

    fun setSoundVolume(soundId: String, volume: Float) {
        soundVolumes[soundId] = volume
        val streamId = activeStreams[soundId]
        if (streamId != null && streamId != 0) {
            soundPool.setVolume(streamId, volume, volume)
        }
    }

    private fun startLoopingSound(soundId: String, volume: Float) {
        stopSound(soundId)
        val sampleId = loadedSoundIds[soundId]
        if (sampleId != null && sampleId != 0) {
            val streamId = soundPool.play(sampleId, volume, volume, 1, -1, 1.0f)
            if (streamId != 0) {
                activeStreams[soundId] = streamId
            }
        }
    }

    private fun stopSound(soundId: String) {
        val streamId = activeStreams.remove(soundId)
        if (streamId != null && streamId != 0) {
            soundPool.stop(streamId)
        }
    }

    fun pauseAll() {
        soundPool.autoPause()
    }

    fun resumeAll() {
        soundPool.autoResume()
    }

    fun stopAll() {
        activeStreams.forEach { (soundId, streamId) ->
            soundPool.stop(streamId)
        }
        activeStreams.clear()
    }

    fun fadeVolumeMultiplier(multiplier: Float) {
        activeStreams.forEach { (soundId, streamId) ->
            val baseVol = soundVolumes[soundId] ?: 0.5f
            val scaledVol = (baseVol * multiplier).coerceIn(0f, 1f)
            soundPool.setVolume(streamId, scaledVol, scaledVol)
        }
    }

    fun release() {
        stopAll()
        soundPool.release()
    }
}
