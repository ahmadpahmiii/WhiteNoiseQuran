package com.whitenoisequran.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.GoldGlow
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isShuffle: Boolean,
    sleepTimerText: String?,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToggleShuffle: () -> Unit,
    onOpenSleepTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playButtonScale by animateFloatAsState(
        targetValue = if (isPlaying) 1.0f else 0.95f,
        label = "play_btn_scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle Toggle
        IconButton(
            onClick = onToggleShuffle,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                tint = if (isShuffle) GoldPrimary else TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        // Previous Track
        IconButton(
            onClick = onPrevious,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous Surah",
                tint = TextPrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        // Central Gold Play/Pause Button with Soft Glow
        Box(
            modifier = Modifier
                .size(72.dp)
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
                modifier = Modifier.size(38.dp)
            )
        }

        // Next Track
        IconButton(
            onClick = onNext,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next Surah",
                tint = TextPrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        // Sleep Timer Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (sleepTimerText != null) GoldPrimary.copy(alpha = 0.15f) else CardDark)
                .border(
                    width = 1.dp,
                    color = if (sleepTimerText != null) GoldPrimary.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onOpenSleepTimer() }
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "Sleep Timer",
                    tint = if (sleepTimerText != null) GoldPrimary else TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                if (sleepTimerText != null) {
                    Text(
                        text = sleepTimerText,
                        style = AppTheme.typography.labelSmall,
                        color = GoldLight
                    )
                }
            }
        }
    }
}
