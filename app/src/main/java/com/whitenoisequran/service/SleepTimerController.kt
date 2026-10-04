package com.whitenoisequran.service

import com.whitenoisequran.service.SleepTimerController.Companion.AMBIENT_ALL_NIGHT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sleep timer in two steps: the Quran plays for the chosen minutes and then stops at the reciter's
 * next pause (never mid-ayah); the ambient mix then plays on for its own minutes and fades out.
 * Without a timer nothing stops.
 */
@Singleton
class SleepTimerController(private val scope: CoroutineScope) {

    @Inject
    constructor() : this(CoroutineScope(SupervisorJob() + Dispatchers.Default))

    enum class Phase { OFF, QURAN, STOPPING_QURAN, AMBIENT }

    companion object {
        /** [ambientAfterMinutes] value: the ambient mix keeps playing after the Quran stops. */
        const val AMBIENT_ALL_NIGHT = -1
        const val FADE_SECONDS = 60
    }

    private var timerJob: Job? = null

    private val _phase = MutableStateFlow(Phase.OFF)
    val phase: StateFlow<Phase> = _phase.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0)

    /** Seconds left in the current step (Quran, then ambient). */
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isTimerActive = MutableStateFlow(false)
    val isTimerActive: StateFlow<Boolean> = _isTimerActive.asStateFlow()

    /** Ambient minutes after the Quran stops: 0 = fade out with it, [AMBIENT_ALL_NIGHT] = keep playing. */
    var ambientAfterMinutes = 0
        private set

    private var onQuranTimeUp: (() -> Unit)? = null
    private var onAmbientFade: ((fadeMultiplier: Float) -> Unit)? = null
    private var onAmbientFinished: (() -> Unit)? = null
    private var onCancelled: (() -> Unit)? = null

    /** Callbacks run off the main thread. */
    fun setCallbacks(
        onQuranTimeUp: () -> Unit,
        onAmbientFade: (Float) -> Unit,
        onAmbientFinished: () -> Unit,
        onCancelled: () -> Unit
    ) {
        this.onQuranTimeUp = onQuranTimeUp
        this.onAmbientFade = onAmbientFade
        this.onAmbientFinished = onAmbientFinished
        this.onCancelled = onCancelled
    }

    fun startTimer(minutes: Int, ambientAfterMinutes: Int = this.ambientAfterMinutes) {
        cancelTimer()
        this.ambientAfterMinutes = ambientAfterMinutes
        setPhase(Phase.QURAN, minutes * 60)
        timerJob = scope.launch {
            countDown(minutes * 60, fade = false)
            setPhase(Phase.STOPPING_QURAN, 0)
            onQuranTimeUp?.invoke() // the player answers with quranStopped() at the next pause
        }
    }

    /** The Quran has stopped for the timer (or wasn't playing); the ambient mix gets its own minutes. */
    fun quranStopped(ambientPlaying: Boolean) {
        if (_phase.value != Phase.STOPPING_QURAN) return
        if (!ambientPlaying || ambientAfterMinutes == AMBIENT_ALL_NIGHT) {
            setPhase(Phase.OFF, 0)
            return
        }
        val seconds = maxOf(ambientAfterMinutes * 60, FADE_SECONDS)
        setPhase(Phase.AMBIENT, seconds)
        timerJob = scope.launch {
            countDown(seconds, fade = true)
            setPhase(Phase.OFF, 0)
            onAmbientFinished?.invoke()
        }
    }

    fun cancelTimer() {
        val wasActive = _phase.value != Phase.OFF
        timerJob?.cancel()
        timerJob = null
        setPhase(Phase.OFF, 0)
        if (wasActive) onCancelled?.invoke()
    }

    private suspend fun countDown(totalSeconds: Int, fade: Boolean) {
        var left = totalSeconds
        while (left > 0) {
            delay(1000L)
            left--
            _remainingSeconds.value = left
            if (fade && left <= FADE_SECONDS) {
                onAmbientFade?.invoke(left.toFloat() / FADE_SECONDS)
            }
        }
    }

    private fun setPhase(phase: Phase, remainingSeconds: Int) {
        _remainingSeconds.value = remainingSeconds
        _phase.value = phase
        _isTimerActive.value = phase != Phase.OFF
    }

    fun formatRemainingTime(): String {
        val totalSec = _remainingSeconds.value
        val mins = totalSec / 60
        val secs = totalSec % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }
}
