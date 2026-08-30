package com.whitenoisequran.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AmbientSoundsValidationTest {

    @Test
    fun testAllSixAmbientSoundsConfigured() {
        val sounds = AmbientSound.DefaultSounds
        assertEquals(6, sounds.size)

        val expectedIds = listOf(
            "calming_rain",
            "soft_rain",
            "rain_bird",
            "forest",
            "ocean",
            "train"
        )
        assertEquals(expectedIds, sounds.map { it.id })
    }

    @Test
    fun testAmbientSoundsUXCopywriting() {
        val sounds = AmbientSound.DefaultSounds

        val calmingRain = sounds.first { it.id == "calming_rain" }
        assertEquals("Steady Rain", calmingRain.name)
        assertEquals("Deep, soothing downpour", calmingRain.subtitle)
        assertEquals("Rain", calmingRain.category)
        assertEquals("ic_sound_calming_rain", calmingRain.iconDrawableName)

        val softRain = sounds.first { it.id == "soft_rain" }
        assertEquals("Gentle Drizzle", softRain.name)
        assertEquals("Light, peaceful droplets", softRain.subtitle)
        assertEquals("Rain", softRain.category)
        assertEquals("ic_sound_soft_rain", softRain.iconDrawableName)

        val rainBird = sounds.first { it.id == "rain_bird" }
        assertEquals("Rain & Songbirds", rainBird.name)
        assertEquals("Morning drizzle with birds", rainBird.subtitle)
        assertEquals("Nature", rainBird.category)
        assertEquals("ic_sound_rain_bird", rainBird.iconDrawableName)

        val forest = sounds.first { it.id == "forest" }
        assertEquals("Lush Forest", forest.name)
        assertEquals("Woodland breeze & trees", forest.subtitle)
        assertEquals("Nature", forest.category)

        val ocean = sounds.first { it.id == "ocean" }
        assertEquals("Ocean Waves", ocean.name)
        assertEquals("Rhythmic tide & calm surf", ocean.subtitle)
        assertEquals("Water", ocean.category)

        val train = sounds.first { it.id == "train" }
        assertEquals("Night Train", train.name)
        assertEquals("Rhythmic tracks for sleep", train.subtitle)
        assertEquals("Ambient", train.category)
        assertEquals(0.70f, train.volume, 0.01f)
    }

    @Test
    fun testDefaultAmbientSoundsDisabledInitially() {
        AmbientSound.DefaultSounds.forEach { sound ->
            assertFalse("Sound ${sound.id} should be disabled initially", sound.isEnabled)
            assertTrue("Sound ${sound.id} should have valid volume > 0", sound.volume > 0f)
            assertTrue(
                "Sound ${sound.id} should have non-empty subtitle",
                sound.subtitle.isNotEmpty()
            )
            assertTrue(
                "Sound ${sound.id} should have non-empty rawResName",
                sound.rawResName.isNotEmpty()
            )
            assertTrue(
                "Sound ${sound.id} should have non-empty iconDrawableName",
                sound.iconDrawableName.isNotEmpty()
            )
        }
    }
}
