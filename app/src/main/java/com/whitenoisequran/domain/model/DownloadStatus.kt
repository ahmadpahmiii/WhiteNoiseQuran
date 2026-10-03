package com.whitenoisequran.domain.model

data class BulkDownloadProgress(
    val totalSurahs: Int = 114,
    val completedCount: Int = 0,
    val failedCount: Int = 0,
    val isFinished: Boolean = false,
    val isRunning: Boolean = false,
    /** Queued but offline: it starts by itself once the connection is back. */
    val isWaitingForNetwork: Boolean = false,
    /** Live percent (0-100) of surahs downloading right now, by surah number. */
    val surahPercent: Map<Int, Int> = emptyMap()
) {
    // Counts partly downloaded files too, so the bar moves during long surahs.
    val progressFraction: Float
        get() = if (totalSurahs > 0) ((completedCount + surahPercent.values.sum() / 100f) / totalSurahs).coerceAtMost(
            1f
        ) else 0f
}
