package com.whitenoisequran.domain.model

data class BulkDownloadProgress(
    val totalSurahs: Int = 114,
    val completedCount: Int = 0,
    val failedCount: Int = 0,
    val currentSurahNumber: Int = 0,
    val isFinished: Boolean = false,
    val isRunning: Boolean = false,
    val estimatedMinutesRemaining: Int = 3
) {
    val progressFraction: Float
        get() = if (totalSurahs > 0) completedCount.toFloat() / totalSurahs.toFloat() else 0f
}
