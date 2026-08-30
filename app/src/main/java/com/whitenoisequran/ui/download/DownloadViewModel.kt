package com.whitenoisequran.ui.download

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.domain.repository.DownloadRepository
import com.whitenoisequran.domain.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DownloadUiState(
    val reciter: Reciter? = null,
    val surahs: List<Surah> = emptyList(),
    val progress: BulkDownloadProgress = BulkDownloadProgress(),
    val isReadyToPlay: Boolean = false
)

@HiltViewModel
class DownloadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quranRepository: QuranRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val reciterId: Int = checkNotNull(savedStateHandle["reciterId"]).toString().toIntOrNull() ?: 5
    private val _reciter = MutableStateFlow<Reciter?>(null)

    val uiState: StateFlow<DownloadUiState> = combine(
        _reciter,
        quranRepository.getSurahsFlow(reciterId),
        downloadRepository.getDownloadProgressFlow(reciterId)
    ) { reciter, surahs, progress ->
        val ready = progress.completedCount > 0
        DownloadUiState(
            reciter = reciter,
            surahs = surahs,
            progress = progress,
            isReadyToPlay = ready
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DownloadUiState()
    )

    init {
        loadReciterAndStartDownload()
    }

    private fun loadReciterAndStartDownload() {
        viewModelScope.launch {
            val reciter = quranRepository.getSelectedReciter()
            _reciter.value = reciter
            downloadRepository.startBulkDownload(reciter.id, reciter.slug)
        }
    }
}
