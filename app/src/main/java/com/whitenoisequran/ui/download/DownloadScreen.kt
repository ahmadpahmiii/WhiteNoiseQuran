package com.whitenoisequran.ui.download

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.R
import com.whitenoisequran.ui.components.IslamicBackgroundPattern
import com.whitenoisequran.ui.components.PlayerArtwork
import com.whitenoisequran.ui.components.SurahDownloadGrid
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealLight
import com.whitenoisequran.ui.theme.TealPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun DownloadScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: DownloadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress.progressFraction,
        label = "download_progress_bar"
    )

    Scaffold(
        containerColor = BackgroundNavy
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            IslamicBackgroundPattern()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = TextPrimary
                            )
                        }

                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = stringResource(
                                    R.string.downloading_reciter,
                                    uiState.reciter?.name
                                        ?: stringResource(R.string.reciter_fallback)
                                ),
                                style = AppTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Text(
                                // Reachable any time from the Surah Index, so say where things stand
                                text = when {
                                    uiState.progress.completedCount >= uiState.progress.totalSurahs ->
                                        stringResource(R.string.all_surahs_offline)

                                    uiState.progress.isWaitingForNetwork -> stringResource(R.string.waiting_for_connection)
                                    uiState.progress.isRunning -> stringResource(R.string.preparing_offline)
                                    uiState.progress.failedCount > 0 ->
                                        stringResource(
                                            R.string.failed_resume_to_retry,
                                            uiState.progress.failedCount
                                        )
                                    // Run finished but some were cancelled one by one
                                    uiState.progress.isFinished -> {
                                        val left =
                                            uiState.progress.totalSurahs - uiState.progress.completedCount
                                        pluralStringResource(
                                            R.plurals.not_downloaded_resume,
                                            left,
                                            left
                                        )
                                    }

                                    else -> stringResource(R.string.paused)
                                },
                                style = AppTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Centered Ambient Artwork
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .size(190.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PlayerArtwork(isPlaying = true)
                    }
                }

                // Progress Bar & Stats
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                R.string.surahs_progress,
                                uiState.progress.completedCount
                            ),
                            style = AppTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        if (uiState.progress.isRunning && !uiState.progress.isWaitingForNetwork) {
                            val eta = uiState.etaMinutes
                            Text(
                                text = when {
                                    eta == null -> stringResource(R.string.eta_estimating)
                                    eta <= 1 -> stringResource(R.string.eta_under_minute)
                                    eta < 60 -> stringResource(R.string.eta_minutes, eta)
                                    else -> stringResource(
                                        R.string.eta_hours_minutes,
                                        eta / 60,
                                        eta % 60
                                    )
                                },
                                style = AppTheme.typography.bodySmall,
                                color = TealLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = GoldPrimary,
                        trackColor = CardDark
                    )

                    // Pause / Resume (progress is kept; resume continues where it stopped)
                    if (uiState.progress.completedCount < uiState.progress.totalSurahs) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                if (uiState.progress.isRunning) viewModel.onPauseDownload() else viewModel.onResumeDownload()
                            }
                        ) {
                            Text(
                                text = stringResource(
                                    if (uiState.progress.isRunning) R.string.pause_download else R.string.resume_download
                                ),
                                style = AppTheme.typography.labelLarge,
                                color = TealLight
                            )
                        }
                    }
                }

                // 114 Surah Micro Grid
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SurahDownloadGrid(
                        surahs = uiState.surahs,
                        surahPercent = uiState.progress.surahPercent,
                        onRetry = { surah -> viewModel.onRetrySurah(surah) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // "Play Available Surahs" CTA
                item {
                    OutlinedButton(
                        onClick = onNavigateToMain,
                        shape = RoundedCornerShape(26.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(TealPrimary),
                            width = 1.5.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TealPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = stringResource(
                                if (uiState.progress.isFinished) R.string.start_listening else R.string.play_available
                            ),
                            // Unspecified: follow the button's content color (the theme style would force white)
                            style = AppTheme.typography.labelLarge.copy(
                                fontSize = 15.sp,
                                color = Color.Unspecified
                            ),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.download_once_hint),
                        style = AppTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}
