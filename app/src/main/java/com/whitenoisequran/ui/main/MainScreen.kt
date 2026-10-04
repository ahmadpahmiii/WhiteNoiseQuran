@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.whitenoisequran.ui.main

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.R
import com.whitenoisequran.ui.components.AmbientMixerSection
import com.whitenoisequran.ui.components.IslamicBackgroundPattern
import com.whitenoisequran.ui.components.PlayerArtwork
import com.whitenoisequran.ui.components.PlayerControls
import com.whitenoisequran.ui.components.QuranSeekBar
import com.whitenoisequran.ui.components.ReciterSheet
import com.whitenoisequran.ui.components.SleepTimerSheet
import com.whitenoisequran.ui.components.SurahListSheet
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicTitleStyle
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.ErrorRed
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.SurfaceDark
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun MainScreen(
    onNavigateToDownload: (reciterId: Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val openDownloads = {
        uiState.currentReciter?.let {
            viewModel.closeSurahSheet() // the sheet's window would otherwise linger over the next screen
            onNavigateToDownload(it.id)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        containerColor = BackgroundNavy,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(snackbarData = data, containerColor = CardDark, contentColor = TextPrimary)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Subtle Islamic night background texture
            IslamicBackgroundPattern()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Nightlight,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = stringResource(R.string.app_name),
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        IconButton(onClick = { viewModel.openSurahSheet() }) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered,
                                contentDescription = stringResource(R.string.cd_surah_list),
                                tint = TextSecondary
                            )
                        }
                    }
                }

                // Section 1: Hero Artwork Canvas
                item {
                    PlayerArtwork(
                        isPlaying = uiState.isPlaying,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Surah Metadata (Arabic + Latin + Verses)
                item {
                    val surah = uiState.currentSurah
                    val arabicName = surah?.nameArabic ?: "الفاتحة"
                    val latinName = surah?.nameLatin ?: "Al-Fatihah"
                    val surahNumber = surah?.number ?: 1
                    val ayatCount = surah?.numberOfAyah ?: 7

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Surah title opens the Surah Index (like the reciter pill below opens the reciter picker)
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable(onClickLabel = stringResource(R.string.cd_choose_surah)) { viewModel.openSurahSheet() }
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Large Arabic Name
                            Text(
                                text = arabicName,
                                style = ArabicTitleStyle.copy(fontSize = 34.sp),
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Latin Name and Surah details
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(
                                        R.string.surah_info,
                                        latinName,
                                        surahNumber,
                                        ayatCount
                                    ),
                                    style = AppTheme.typography.titleSmall,
                                    color = TextSecondary
                                )
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reciter Pill: opens the reciter picker
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardDark)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.08f),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.openReciterSheet() }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = uiState.currentReciter?.avatarInitial ?: "م",
                                        fontSize = 10.sp,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = uiState.currentReciter?.name ?: "Mishary Al-Afasy",
                                    style = AppTheme.typography.labelSmall,
                                    color = GoldLight
                                )

                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = stringResource(R.string.cd_change_reciter),
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                // Seekbar
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    QuranSeekBar(
                        positionMs = uiState.playbackPositionMs,
                        durationMs = uiState.playbackDurationMs,
                        onSeek = { targetMs -> viewModel.onSeek(targetMs) }
                    )
                }

                // Player Controls (Prev, Play/Pause, Next, Timer)
                item {
                    PlayerControls(
                        isPlaying = uiState.isPlaying,
                        isBuffering = uiState.isBuffering,
                        quranVolume = uiState.quranVolume,
                        sleepTimerText = uiState.sleepTimerRemainingText,
                        onPlayPause = { viewModel.onPlayPause() },
                        onPrevious = { viewModel.onPrevious() },
                        onNext = { viewModel.onNext() },
                        onQuranVolumeChange = { vol -> viewModel.onQuranVolumeChange(vol) },
                        onOpenSleepTimer = { viewModel.openSleepTimerSheet() }
                    )
                }

                // Subtle Divider
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.05f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Section 2: Ambient Sound Mixer (2x2 Grid)
                item {
                    AmbientMixerSection(
                        sounds = uiState.ambientSounds,
                        isMixPlaying = uiState.isAmbientPlaying,
                        onToggleMix = { viewModel.onToggleAmbientMix() },
                        onVolumeChange = { soundId, vol -> viewModel.onUpdateSoundVolume(soundId, vol) },
                        onToggleSound = { soundId, isEn -> viewModel.onToggleSound(soundId, isEn) },
                        onResetAll = { viewModel.onResetAllSounds() }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Surah Picker Bottom Sheet Modal
            if (uiState.isSurahSheetOpen) {
                SurahListSheet(
                    surahs = uiState.surahs,
                    currentSurah = uiState.currentSurah,
                    isLoading = uiState.isLoadingSurahs,
                    downloadProgress = uiState.downloadProgress,
                    onSelectSurah = { surah -> viewModel.onSelectSurah(surah) },
                    onDownloadSingleSurah = { surah -> viewModel.onDownloadSingleSurah(surah) },
                    onDeleteSurahAudio = { surah -> viewModel.onDeleteSurahAudio(surah) },
                    onCancelDownload = { surah -> viewModel.onCancelSurahDownload(surah) },
                    onOpenDownloads = {
                        // Starting a download asks first (size); watching a running one doesn't
                        if (uiState.downloadProgress.isRunning) openDownloads() else viewModel.onRequestDownloadAll()
                    },
                    onDeleteAllAudio = { viewModel.onRequestDeleteAllAudio() },
                    onDismiss = { viewModel.closeSurahSheet() }
                )
            }

            if (uiState.isReciterSheetOpen) {
                ReciterSheet(
                    reciters = uiState.reciters,
                    currentReciterId = uiState.currentReciter?.id,
                    downloadCounts = uiState.reciterDownloadCounts,
                    onSelect = { reciter -> viewModel.onSelectReciter(reciter) },
                    onDismiss = { viewModel.closeReciterSheet() }
                )
            }

            uiState.deleteAllConfirmBytes?.let { bytes ->
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDeleteAllAudio() },
                    title = { Text(stringResource(R.string.delete_all_title)) },
                    text = {
                        Text(
                            listOfNotNull(
                                // The size includes partly downloaded files, so it isn't tied to a surah count
                                stringResource(
                                    R.string.delete_all_body,
                                    Formatter.formatShortFileSize(context, bytes),
                                    uiState.currentReciter?.name
                                        ?: stringResource(R.string.this_reciter)
                                ),
                                if (uiState.downloadProgress.isRunning) stringResource(R.string.delete_all_running) else null,
                                stringResource(R.string.delete_all_later)
                            ).joinToString(" ")
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.onDeleteAllAudio() }) {
                            Text(stringResource(R.string.delete), color = ErrorRed)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissDeleteAllAudio() }) {
                            Text(stringResource(R.string.cancel), color = TextSecondary)
                        }
                    },
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary,
                    textContentColor = TextSecondary
                )
            }

            uiState.downloadConfirm?.let { confirm ->
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = { viewModel.dismissDownloadAll() },
                    title = {
                        Text(
                            if (confirm.surahCount == 114) {
                                stringResource(R.string.download_all_title)
                            } else {
                                pluralStringResource(
                                    R.plurals.download_some_title,
                                    confirm.surahCount,
                                    confirm.surahCount
                                )
                            }
                        )
                    },
                    text = {
                        Text(
                            listOfNotNull(
                                confirm.bytes?.let {
                                    stringResource(
                                        R.string.download_size_about,
                                        Formatter.formatShortFileSize(context, it)
                                    )
                                },
                                stringResource(R.string.download_wifi_note)
                            ).joinToString(" ")
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.dismissDownloadAll()
                            openDownloads() // the download screen starts it
                        }) {
                            Text(stringResource(R.string.download), color = GoldPrimary)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissDownloadAll() }) {
                            Text(stringResource(R.string.cancel), color = TextSecondary)
                        }
                    },
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary,
                    textContentColor = TextSecondary
                )
            }

            // Sleep Timer Bottom Sheet Modal
            if (uiState.isSleepTimerSheetOpen) {
                SleepTimerSheet(
                    phase = uiState.sleepTimerPhase,
                    remainingFormatted = uiState.sleepTimerRemainingText,
                    initialAmbientAfterMinutes = viewModel.sleepAmbientAfterMinutes,
                    onSetTimer = { mins, ambientAfter ->
                        viewModel.onSetSleepTimer(
                            mins,
                            ambientAfter
                        )
                    },
                    onCancelTimer = { viewModel.onCancelSleepTimer() },
                    onDismiss = { viewModel.closeSleepTimerSheet() }
                )
            }
        }
    }
}
