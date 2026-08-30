package com.whitenoisequran.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whitenoisequran.data.preferences.AppPreferences
import com.whitenoisequran.domain.model.Reciter
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

data class OnboardingUiState(
    val reciters: List<Reciter> = emptyList(),
    val selectedReciter: Reciter? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _selectedReciter = MutableStateFlow<Reciter?>(null)

    val uiState: StateFlow<OnboardingUiState> = combine(
        quranRepository.getRecitersFlow(),
        _selectedReciter
    ) { reciters, selected ->
        val effectiveSelected = selected ?: reciters.firstOrNull { it.isPopular } ?: reciters.firstOrNull()
        OnboardingUiState(
            reciters = reciters.ifEmpty { Reciter.DefaultReciters },
            selectedReciter = effectiveSelected,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnboardingUiState(reciters = Reciter.DefaultReciters, selectedReciter = Reciter.DefaultReciters.first { it.isPopular })
    )

    init {
        viewModelScope.launch {
            quranRepository.seedInitialData()
        }
    }

    fun selectReciter(reciter: Reciter) {
        _selectedReciter.value = reciter
    }

    fun completeOnboarding(onSuccess: (reciterId: Int) -> Unit) {
        val selected = uiState.value.selectedReciter ?: return
        viewModelScope.launch {
            quranRepository.setSelectedReciter(selected)
            appPreferences.setOnboardingCompleted(true)
            onSuccess(selected.id)
        }
    }
}
