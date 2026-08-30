package com.whitenoisequran.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainModelsTest {

    @Test
    fun testDefaultReciters() {
        val reciters = Reciter.DefaultReciters
        assertEquals(6, reciters.size)

        val alAfasy = reciters.find { it.slug == "Misyari-Rasyid-Al-Afasi" }
        assertTrue(alAfasy != null)
        assertTrue(alAfasy!!.isPopular)
        assertEquals("م", alAfasy.avatarInitial)
    }

    @Test
    fun testDefaultAmbientSounds() {
        val sounds = AmbientSound.DefaultSounds
        assertEquals(4, sounds.size)

        val whiteNoise = sounds.find { it.id == "white_noise" }
        assertTrue(whiteNoise != null)
        assertEquals("🌬", whiteNoise!!.iconEmoji)
        assertFalse(whiteNoise.isEnabled)
    }

    @Test
    fun testBulkDownloadProgressFraction() {
        val progress = BulkDownloadProgress(
            totalSurahs = 114,
            completedCount = 57
        )
        assertEquals(0.5f, progress.progressFraction, 0.001f)
    }
}
