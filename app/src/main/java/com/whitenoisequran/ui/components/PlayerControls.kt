package com.whitenoisequran.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.CardDarkFrosted
import com.whitenoisequran.ui.theme.GoldGlow
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    quranVolume: Float,
    sleepTimerText: String?,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onQuranVolumeChange: (Float) -> Unit,
    onOpenSleepTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVolumeSliderOpen by remember { mutableStateOf(false) }

    val playButtonScale by animateFloatAsState(
        targetValue = if (isPlaying) 1.0f else 0.96f,
        label = "play_btn_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Expandable Quran Recitation Volume Slider Pill
        AnimatedVisibility(
            visible = isVolumeSliderOpen,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardDarkFrosted)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = if (quranVolume > 0.05f) Icons.AutoMirrored.Filled.VolumeDown else Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = "Quran Volume",
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )

                    Slider(
                        value = quranVolume,
                        onValueChange = onQuranVolumeChange,
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldLight,
                            activeTrackColor = GoldPrimary,
                            inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "Quran ${(quranVolume * 100).roundToInt()}%",
                        style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = GoldLight,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Main Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Quran Volume Button (toggles slider)
            IconButton(
                onClick = { isVolumeSliderOpen = !isVolumeSliderOpen },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isVolumeSliderOpen) GoldPrimary.copy(alpha = 0.15f) else CardDark)
                    .border(
                        1.dp,
                        if (isVolumeSliderOpen) GoldPrimary.copy(alpha = 0.5f) else Color.White.copy(
                            alpha = 0.08f
                        ),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = when {
                        quranVolume <= 0.01f -> Icons.AutoMirrored.Filled.VolumeOff
                        quranVolume < 0.5f -> Icons.AutoMirrored.Filled.VolumeDown
                        else -> Icons.AutoMirrored.Filled.VolumeUp
                    },
                    contentDescription = "Quran Volume Control",
                    tint = if (isVolumeSliderOpen) GoldPrimary else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Previous Track
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous Surah",
                    tint = TextPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Central Gold Play/Pause Button
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .scale(playButtonScale)
                    .shadow(
                        elevation = if (isPlaying) 16.dp else 6.dp,
                        shape = CircleShape,
                        ambientColor = GoldGlow,
                        spotColor = GoldPrimary
                    )
                    .clip(CircleShape)
                    .background(GoldPrimary)
                    .clickable { onPlayPause() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = BackgroundNavy,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Next Track
            IconButton(
                onClick = onNext,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next Surah",
                    tint = TextPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Right: Sleep Timer Pill (Timer Icon & Time)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (sleepTimerText != null) GoldPrimary.copy(alpha = 0.16f) else CardDark)
                    .border(
                        width = 1.dp,
                        color = if (sleepTimerText != null) GoldPrimary.copy(alpha = 0.6f) else Color.White.copy(
                            alpha = 0.08f
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onOpenSleepTimer() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Sleep Timer",
                        tint = if (sleepTimerText != null) GoldPrimary else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = sleepTimerText ?: "Timer",
                        style = AppTheme.typography.labelSmall.copy(
                            fontWeight = if (sleepTimerText != null) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        ),
                        color = if (sleepTimerText != null) GoldLight else TextSecondary
                    )
                }
            }
        }
    }
}
