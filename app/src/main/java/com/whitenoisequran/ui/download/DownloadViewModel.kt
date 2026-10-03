package com.whitenoisequran.ui.download

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.DownloadRepository
import com.whitenoisequran.domain.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

data class DownloadUiState(
    val reciter: Reciter? = null,
    val surahs: List<Surah> = emptyList(),
    val progress: BulkDownloadProgress = BulkDownloadProgress(),
    /** Null until there's enough download speed history to estimate. */
    val etaMinutes: Int? = null
)

@HiltViewModel
class DownloadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quranRepository: QuranRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val reciterId: Int = checkNotNull(savedStateHandle["reciterId"]).toString().toIntOrNull() ?: 5
    private val _reciter = MutableStateFlow<Reciter?>(null)

    // (time, ayahs downloaded) over the last minute, so the estimate follows the current speed
    private val speedSamples = ArrayDeque<Pair<Long, Double>>()

    val uiState: StateFlow<DownloadUiState> = combine(
        _reciter,
        quranRepository.getSurahsFlow(reciterId),
        downloadRepository.getDownloadProgressFlow(reciterId)
    ) { reciter, surahs, progress ->
        DownloadUiState(
            reciter = reciter,
            surahs = surahs,
            progress = progress,
            etaMinutes = estimateMinutesLeft(surahs, progress)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DownloadUiState()
    )

    init {
        loadReciterAndStartDownload()
    }

    fun onPauseDownload() {
        viewModelScope.launch {
            downloadRepository.pauseOrCancelDownload(reciterId)
        }
    }

    fun onResumeDownload() = loadReciterAndStartDownload()

    fun onRetrySurah(surah: Surah) {
        val reciter = _reciter.value ?: return
        viewModelScope.launch {
            downloadRepository.downloadSingleSurah(surah.number, reciter.id, reciter.slug)
        }
    }

    private fun estimateMinutesLeft(surahs: List<Surah>, progress: BulkDownloadProgress): Int? {
        if (!progress.isRunning || progress.isWaitingForNetwork) {
            speedSamples.clear()
            return null
        }
        // Weighted by ayah count, a rough stand-in for audio length: Al-Baqarah takes far longer than Al-Kawthar
        val totalAyahs = surahs.sumOf { it.numberOfAyah }
        val doneAyahs = surahs.sumOf {
            if (it.downloadState == DownloadState.DONE) it.numberOfAyah.toDouble()
            else it.numberOfAyah * (progress.surahPercent[it.number] ?: 0) / 100.0
        }
        val now = SystemClock.elapsedRealtime()
        speedSamples.addLast(now to doneAyahs)
        while (now - speedSamples.first().first > 60_000) speedSamples.removeFirst()
        val (since, doneThen) = speedSamples.first()
        val ayahsPerMs = (doneAyahs - doneThen) / (now - since)
        if (now - since < 5_000 || ayahsPerMs <= 0) return null
        return ceil((totalAyahs - doneAyahs) / ayahsPerMs / 60_000).toInt()
    }

    private fun loadReciterAndStartDownload() {
        viewModelScope.launch {
            // The route's reciter: a download notification can open another reciter than the selected one
            val reciter =
                quranRepository.getRecitersFlow().first().firstOrNull { it.id == reciterId }
                    ?: quranRepository.getSelectedReciter()
            _reciter.value = reciter
            downloadRepository.startBulkDownload(reciter.id, reciter.slug)
        }
    }
}
