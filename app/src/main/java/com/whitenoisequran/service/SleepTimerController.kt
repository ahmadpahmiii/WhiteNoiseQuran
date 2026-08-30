package com.whitenoisequran.service

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

@Singleton
class SleepTimerController @Inject constructor() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var timerJob: Job? = null

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isTimerActive = MutableStateFlow(false)
    val isTimerActive: StateFlow<Boolean> = _isTimerActive.asStateFlow()

    private var onTimerFinished: (() -> Unit)? = null
    private var onFadeProgress: ((fadeMultiplier: Float) -> Unit)? = null

    fun setCallbacks(onFinish: () -> Unit, onFade: (Float) -> Unit) {
        this.onTimerFinished = onFinish
        this.onFadeProgress = onFade
    }

    fun startTimer(minutes: Int) {
        cancelTimer()
        val totalSeconds = minutes * 60
        _remainingSeconds.value = totalSeconds
        _isTimerActive.value = true

        timerJob = scope.launch {
            var currentSec = totalSeconds
            val fadeThreshold = 60 // Fade out in last 60 seconds

            while (currentSec > 0) {
                delay(1000L)
                currentSec--
                _remainingSeconds.value = currentSec

                if (currentSec <= fadeThreshold) {
                    val multiplier = (currentSec.toFloat() / fadeThreshold.toFloat()).coerceIn(0f, 1f)
                    onFadeProgress?.invoke(multiplier)
                }
            }

            _isTimerActive.value = false
            _remainingSeconds.value = 0
            onTimerFinished?.invoke()
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        _isTimerActive.value = false
        _remainingSeconds.value = 0
        onFadeProgress?.invoke(1.0f)
    }

    fun formatRemainingTime(): String {
        val totalSec = _remainingSeconds.value
        val mins = totalSec / 60
        val secs = totalSec % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }
}
