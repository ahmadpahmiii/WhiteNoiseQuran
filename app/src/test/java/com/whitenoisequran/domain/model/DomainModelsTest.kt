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
        assertEquals(6, sounds.size)

        val calmingRain = sounds.find { it.id == "calming_rain" }
        assertTrue(calmingRain != null)
        assertEquals("Steady Rain", calmingRain!!.name)
        assertEquals("🌧️", calmingRain.iconEmoji)
        assertEquals("ic_sound_calming_rain", calmingRain.iconDrawableName)
        assertFalse(calmingRain.isEnabled)

        val rainBird = sounds.find { it.id == "rain_bird" }
        assertTrue(rainBird != null)
        assertEquals("Rain & Songbirds", rainBird!!.name)
        assertEquals("Nature", rainBird.category)

        val train = sounds.find { it.id == "train" }
        assertTrue(train != null)
        assertEquals("Night Train", train!!.name)
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
