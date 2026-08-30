package com.whitenoisequran.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SleepTimerControllerTest {

    @Test
    fun testStartAndCancelTimer() {
        val controller = SleepTimerController()

        assertFalse(controller.isTimerActive.value)
        assertEquals(0, controller.remainingSeconds.value)

        controller.startTimer(15)
        assertTrue(controller.isTimerActive.value)
        assertEquals(15 * 60, controller.remainingSeconds.value)
        assertEquals("15:00", controller.formatRemainingTime())

        controller.cancelTimer()
        assertFalse(controller.isTimerActive.value)
        assertEquals(0, controller.remainingSeconds.value)
        assertEquals("00:00", controller.formatRemainingTime())
    }
}
