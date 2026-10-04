package com.whitenoisequran.ui.main

import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.service.SleepTimerController

data class MainUiState(
    val currentSurah: Surah? = null,
    val currentReciter: Reciter? = null,
    val surahs: List<Surah> = emptyList(),
    val ambientSounds: List<AmbientSound> = emptyList(),
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    /** The ambient mix is audible (it can play without the Quran). */
    val isAmbientPlaying: Boolean = false,
    val quranVolume: Float = 1.0f,
    val playbackPositionMs: Long = 0L,
    val playbackDurationMs: Long = 1L,
    val sleepTimerRemainingText: String? = null,
    val isSleepTimerActive: Boolean = false,
    val sleepTimerPhase: SleepTimerController.Phase = SleepTimerController.Phase.OFF,
    val isSurahSheetOpen: Boolean = false,
    val isSleepTimerSheetOpen: Boolean = false,
    val isReciterSheetOpen: Boolean = false,
    val isLoadingSurahs: Boolean = false,
    val downloadProgress: BulkDownloadProgress = BulkDownloadProgress(),
    val reciters: List<Reciter> = emptyList(),
    /** Downloaded surah count per reciter id. */
    val reciterDownloadCounts: Map<Int, Int> = emptyMap(),
    /** Set while "Free Space" waits for confirmation: the bytes it would free. */
    val deleteAllConfirmBytes: Long? = null,
    /** Set while "Download All" waits for confirmation. */
    val downloadConfirm: DownloadConfirm? = null
)

data class DownloadConfirm(
    val surahCount: Int,
    /** Estimated size; null when unknown. */
    val bytes: Long?
)
