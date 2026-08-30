@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.whitenoisequran.ui.main

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.ui.components.AmbientMixerSection
import com.whitenoisequran.ui.components.IslamicBackgroundPattern
import com.whitenoisequran.ui.components.PlayerArtwork
import com.whitenoisequran.ui.components.PlayerControls
import com.whitenoisequran.ui.components.QuranSeekBar
import com.whitenoisequran.ui.components.SleepTimerSheet
import com.whitenoisequran.ui.components.SurahListSheet
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicTitleStyle
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun MainScreen(
    onNavigateToOnboarding: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BackgroundNavy
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
                                contentDescription = "Logo",
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "White Noise Quran",
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(onClick = { viewModel.openSurahSheet() }) {
                                Icon(
                                    imageVector = Icons.Default.FormatListNumbered,
                                    contentDescription = "Surah List",
                                    tint = TextSecondary
                                )
                            }
                            IconButton(onClick = onNavigateToOnboarding) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings / Reciter",
                                    tint = TextSecondary
                                )
                            }
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
                        // Large Arabic Name
                        Text(
                            text = arabicName,
                            style = ArabicTitleStyle.copy(fontSize = 34.sp),
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Latin Name and Surah details
                        Text(
                            text = "$latinName · Surah $surahNumber · $ayatCount Verses",
                            style = AppTheme.typography.titleSmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reciter Pill (clickable to change)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardDark)
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                                .clickable { viewModel.openSurahSheet() }
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
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Switch",
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

                // Player Controls (Prev, Play/Pause, Next, Shuffle, Timer)
                item {
                    PlayerControls(
                        isPlaying = uiState.isPlaying,
                        isShuffle = uiState.isShuffle,
                        sleepTimerText = uiState.sleepTimerRemainingText,
                        onPlayPause = { viewModel.onPlayPause() },
                        onPrevious = { viewModel.onPrevious() },
                        onNext = { viewModel.onNext() },
                        onToggleShuffle = { viewModel.onToggleShuffle() },
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
                    onSelectSurah = { surah -> viewModel.onSelectSurah(surah) },
                    onDismiss = { viewModel.closeSurahSheet() }
                )
            }

            // Sleep Timer Bottom Sheet Modal
            if (uiState.isSleepTimerSheetOpen) {
                SleepTimerSheet(
                    isTimerActive = uiState.isSleepTimerActive,
                    remainingFormatted = uiState.sleepTimerRemainingText,
                    onSetTimer = { mins -> viewModel.onSetSleepTimer(mins) },
                    onCancelTimer = { viewModel.onCancelSleepTimer() },
                    onDismiss = { viewModel.closeSleepTimerSheet() }
                )
            }
        }
    }
}
