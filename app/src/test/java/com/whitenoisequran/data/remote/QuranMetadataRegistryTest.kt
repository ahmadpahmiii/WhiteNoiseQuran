package com.whitenoisequran.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranMetadataRegistryTest {

    @Test
    fun testAll114SurahsPresent() {
        val surahs = QuranMetadataRegistry.allSurahs
        assertEquals(114, surahs.size)

        // Surah 1: Al-Fatihah
        val fatihah = surahs.first()
        assertEquals(1, fatihah.number)
        assertEquals("الفاتحة", fatihah.nameArabic)
        assertEquals("Al-Fatihah", fatihah.nameLatin)
        assertEquals(7, fatihah.numberOfAyah)

        // Surah 114: An-Nas
        val nas = surahs.last()
        assertEquals(114, nas.number)
        assertEquals("الناس", nas.nameArabic)
        assertEquals("An-Nas", nas.nameLatin)
        assertEquals(6, nas.numberOfAyah)

        // Verify sequential numbering
        surahs.forEachIndexed { index, surah ->
            assertEquals(index + 1, surah.number)
            assertTrue(surah.nameArabic.isNotEmpty())
            assertTrue(surah.nameLatin.isNotEmpty())
            assertTrue(surah.numberOfAyah > 0)
        }
    }
}
