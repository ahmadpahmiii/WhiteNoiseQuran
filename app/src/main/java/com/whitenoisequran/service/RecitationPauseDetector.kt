package com.whitenoisequran.service

import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor.AudioFormat
import androidx.media3.common.audio.AudioProcessor.UnhandledAudioFormatException
import androidx.media3.common.audio.BaseAudioProcessor
import androidx.media3.common.util.UnstableApi
import java.nio.ByteBuffer
import kotlin.math.abs

/**
 * In the Quran player's audio path. Once armed it waits for the reciter's next pause (a quiet
 * stretch, as between ayat), reports it once and outputs silence from there on. The muting happens
 * before the audio buffers, so the ayah before the pause always plays to its end.
 */
@UnstableApi
class RecitationPauseDetector : BaseAudioProcessor() {

    @Volatile
    private var onPause: (() -> Unit)? = null

    @Volatile
    private var muted = false

    private var quietFrames = 0
    private var peak = 0

    /** Calls [onPause] once, on the playback thread, at the next pause; silent after it until [disarm]. */
    fun arm(onPause: () -> Unit) {
        this.onPause = onPause
    }

    fun disarm() {
        onPause = null
        muted = false
    }

    override fun onConfigure(inputAudioFormat: AudioFormat): AudioFormat {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
            throw UnhandledAudioFormatException(inputAudioFormat)
        }
        return inputAudioFormat
    }

    override fun queueInput(inputBuffer: ByteBuffer) {
        val start = inputBuffer.position()
        val end = inputBuffer.limit()
        if (start == end) return
        val output = replaceOutputBuffer(end - start)
        val muteFrom = if (muted) start else scan(inputBuffer, start, end)
        inputBuffer.limit(muteFrom)
        output.put(inputBuffer)
        inputBuffer.limit(end)
        repeat(end - muteFrom) { output.put(0) }
        inputBuffer.position(end)
        output.flip()
    }

    /**
     * Tracks the recording's loudest sample. Once armed, returns where the first quiet stretch long
     * enough to be a pause ends, or [end] when there's none.
     */
    private fun scan(buffer: ByteBuffer, start: Int, end: Int): Int {
        val armed = onPause != null
        if (!armed) quietFrames = 0
        val frameSize = inputAudioFormat.bytesPerFrame
        val pauseFrames = inputAudioFormat.sampleRate * MIN_PAUSE_MS / 1000
        var frame = start
        while (frame + frameSize <= end) {
            var frameMax = 0
            var i = frame
            while (i < frame + frameSize) {
                // 16-bit PCM is little-endian
                val sample =
                    abs((buffer.get(i + 1).toInt() shl 8) or (buffer.get(i).toInt() and 0xFF))
                if (sample > frameMax) frameMax = sample
                i += 2
            }
            frame += frameSize
            if (frameMax > peak) peak = frameMax
            if (!armed) continue
            // Quiet relative to the recording: reverb and room noise keep real pauses well above silence
            quietFrames = if (frameMax * QUIET_DIVISOR <= peak) quietFrames + 1 else 0
            if (quietFrames >= pauseFrames) {
                muted = true
                onPause?.invoke()
                onPause = null
                return frame
            }
        }
        return end
    }

    override fun onFlush() {
        quietFrames = 0
        peak = 0
    }

    override fun onReset() {
        onFlush()
        disarm()
    }

    private companion object {
        // Calibrated on Alafasy and Sudais (Al-Fatihah): a pause is ≥ 300 ms below 1/5 of the
        // recording's peak (-14 dB). That finds the gaps between ayat but not breaths inside one.
        const val QUIET_DIVISOR = 5
        const val MIN_PAUSE_MS = 300
    }
}
