package com.whitenoisequran.service

import com.whitenoisequran.service.SleepTimerController.Phase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SleepTimerControllerTest {

    private fun TestScope.controller() =
        SleepTimerController(CoroutineScope(StandardTestDispatcher(testScheduler)))

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

    @Test
    fun quranStopsAtPauseThenAmbientFadesOut() = runTest {
        val controller = controller()
        var quranTimeUp = false
        var ambientFinished = false
        val fades = mutableListOf<Float>()
        controller.setCallbacks(
            onQuranTimeUp = { quranTimeUp = true },
            onAmbientFade = { fades += it },
            onAmbientFinished = { ambientFinished = true },
            onCancelled = {}
        )

        controller.startTimer(minutes = 1, ambientAfterMinutes = 2)
        testScheduler.advanceTimeBy(60_001)
        assertTrue(quranTimeUp)
        assertEquals(Phase.STOPPING_QURAN, controller.phase.value) // until the reciter's pause

        controller.quranStopped(ambientPlaying = true)
        assertEquals(Phase.AMBIENT, controller.phase.value)
        assertEquals(120, controller.remainingSeconds.value)

        testScheduler.advanceTimeBy(120_001)
        assertTrue(ambientFinished)
        assertEquals(Phase.OFF, controller.phase.value)
        assertEquals(61, fades.size) // only the last minute fades, 1.0 down to 0.0
        assertEquals(1f, fades.first())
        assertEquals(0f, fades.last())
    }

    @Test
    fun timerEndsWithQuranWhenAmbientIsOffOrAllNight() = runTest {
        val controller = controller()
        var ambientFinished = false
        controller.setCallbacks({}, {}, { ambientFinished = true }, {})

        controller.startTimer(
            minutes = 1,
            ambientAfterMinutes = SleepTimerController.AMBIENT_ALL_NIGHT
        )
        testScheduler.advanceTimeBy(60_001)
        controller.quranStopped(ambientPlaying = true)
        assertEquals(Phase.OFF, controller.phase.value)
        assertFalse(ambientFinished) // all night: the mix keeps playing

        controller.startTimer(minutes = 1, ambientAfterMinutes = 15)
        testScheduler.advanceTimeBy(60_001)
        controller.quranStopped(ambientPlaying = false)
        assertEquals(Phase.OFF, controller.phase.value)
    }

    @Test
    fun cancelWhileWaitingForThePauseNotifies() = runTest {
        val controller = controller()
        var cancelled = false
        controller.setCallbacks({}, {}, {}, { cancelled = true })

        controller.startTimer(minutes = 1, ambientAfterMinutes = 0)
        testScheduler.advanceTimeBy(60_001)
        controller.cancelTimer()
        assertTrue(cancelled)
        assertEquals(Phase.OFF, controller.phase.value)

        controller.quranStopped(ambientPlaying = true) // a late pause after cancel changes nothing
        assertEquals(Phase.OFF, controller.phase.value)
    }
}
