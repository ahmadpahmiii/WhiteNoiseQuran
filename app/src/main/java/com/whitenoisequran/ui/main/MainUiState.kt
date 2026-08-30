package com.whitenoisequran.ui.main

import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah

data class MainUiState(
    val currentSurah: Surah? = null,
    val currentReciter: Reciter? = null,
    val surahs: List<Surah> = emptyList(),
    val ambientSounds: List<AmbientSound> = emptyList(),
    val isPlaying: Boolean = false,
    val quranVolume: Float = 1.0f,
    val playbackPositionMs: Long = 0L,
    val playbackDurationMs: Long = 1L,
    val sleepTimerRemainingText: String? = null,
    val isSleepTimerActive: Boolean = false,
    val isSurahSheetOpen: Boolean = false,
    val isSleepTimerSheetOpen: Boolean = false,
    val isReciterSheetOpen: Boolean = false,
    val isLoadingSurahs: Boolean = false,
    val downloadProgress: BulkDownloadProgress = BulkDownloadProgress()
)
