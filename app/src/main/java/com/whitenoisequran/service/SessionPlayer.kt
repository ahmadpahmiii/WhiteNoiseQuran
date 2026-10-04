package com.whitenoisequran.service

import androidx.media3.common.ForwardingSimpleBasePlayer
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.google.common.util.concurrent.ListenableFuture

/**
 * The Quran player as the notification, lock screen and headset see it. Their "pause" silences
 * everything, ambient mix included. While only the mix plays it shows as playing (suppressed, so
 * the Quran's position stays put), which gives those controls a Pause for the rain too.
 * The app's own buttons use the Quran player directly.
 */
@UnstableApi
class SessionPlayer(
    player: Player,
    private val ambientSoundMixer: AmbientSoundMixer
) : ForwardingSimpleBasePlayer(player) {

    override fun getState(): State {
        val state = super.getState()
        if (state.playWhenReady || !ambientSoundMixer.isPlaying.value) return state
        return state.buildUpon()
            .setPlayWhenReady(true, PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST)
            .setPlaybackSuppressionReason(PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS)
            .build()
    }

    override fun handleSetPlayWhenReady(playWhenReady: Boolean): ListenableFuture<*> {
        if (!playWhenReady) ambientSoundMixer.pauseAll()
        return super.handleSetPlayWhenReady(playWhenReady)
    }

    /** The ambient mix started or stopped. */
    fun refresh() = invalidateState()
}
