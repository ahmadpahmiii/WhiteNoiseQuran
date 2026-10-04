package com.whitenoisequran.service

import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor.AudioFormat
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecitationPauseDetectorTest {

    // 1 kHz mono: one frame per millisecond, so a 300 ms pause is 300 quiet samples
    private fun detector() = RecitationPauseDetector().apply {
        configure(AudioFormat(1000, 1, C.ENCODING_PCM_16BIT))
        flush()
    }

    private fun RecitationPauseDetector.process(samples: ShortArray): ShortArray {
        val input = ByteBuffer.allocateDirect(samples.size * 2).order(ByteOrder.LITTLE_ENDIAN)
        samples.forEach { input.putShort(it) }
        input.flip()
        queueInput(input)
        val out = output.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        return ShortArray(out.remaining()).also { out.get(it) }
    }

    private fun loud(ms: Int) = ShortArray(ms) { if (it % 2 == 0) 8000 else -8000 }
    private fun quiet(ms: Int) = ShortArray(ms) { 50 }

    @Test
    fun passesAudioThroughWhenNotArmed() {
        val samples = loud(100) + quiet(400) + loud(100)
        assertArrayEquals(samples, detector().process(samples))
    }

    @Test
    fun mutesEverythingAfterTheFirstPauseOnceArmed() {
        val detector = detector()
        var pauses = 0
        detector.arm { pauses++ }
        // A breath of 299 ms isn't a pause yet; the 300 ms gap is, and the next ayah must stay silent
        val samples = loud(100) + quiet(299) + loud(1) + quiet(300) + loud(200)
        val out = detector.process(samples)

        assertEquals(1, pauses)
        assertArrayEquals(samples.copyOf(700), out.copyOf(700))
        assertArrayEquals(ShortArray(200), out.copyOfRange(700, 900))

        detector.disarm() // e.g. after the player paused: back to normal
        assertArrayEquals(loud(50), detector.process(loud(50)))
    }

    @Test
    fun quietIsRelativeToTheRecordingSoReverbStillCountsAsAPause() {
        val detector = detector()
        var pauses = 0
        detector.arm { pauses++ }
        val speech = ShortArray(100) { if (it % 2 == 0) 20_000 else -20_000 }
        val reverbTail = ShortArray(300) { 3_000 } // far from silent, but under 1/5 of the peak
        detector.process(speech + reverbTail)
        assertEquals(1, pauses)
    }
}
