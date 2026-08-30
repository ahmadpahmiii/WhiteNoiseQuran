package com.whitenoisequran.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.CardDarkFrosted
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealGlow
import com.whitenoisequran.ui.theme.TealLight
import com.whitenoisequran.ui.theme.TealPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun AmbientSoundCard(
    sound: AmbientSound,
    onVolumeChange: (Float) -> Unit,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = sound.isEnabled

    val borderColor by animateColorAsState(
        targetValue = if (isActive) TealPrimary.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.07f),
        label = "card_border_color"
    )

    val shadowElevation = if (isActive) 8.dp else 0.dp

    Box(
        modifier = modifier
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(20.dp),
                ambientColor = TealGlow,
                spotColor = TealPrimary
            )
            .clip(RoundedCornerShape(20.dp))
            .background(CardDarkFrosted)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Sound Icon & Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = sound.iconEmoji,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = sound.name,
                    style = AppTheme.typography.titleSmall,
                    color = if (isActive) TextPrimary else TextSecondary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vertical Volume Slider Track
            VerticalVolumeSlider(
                volume = sound.volume,
                isActive = isActive,
                onVolumeChange = { newVol ->
                    onVolumeChange(newVol)
                    if (!isActive && newVol > 0.05f) {
                        onToggle(true)
                    }
                },
                modifier = Modifier
                    .width(36.dp)
                    .height(96.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Volume Percentage Label
            Text(
                text = "${(sound.volume * 100).roundToInt()}%",
                style = AppTheme.typography.labelSmall,
                color = if (isActive) TealLight else TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tactile ON/OFF Toggle Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isActive) TealPrimary else Color.White.copy(alpha = 0.06f))
                    .clickable { onToggle(!isActive) }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isActive) "ON" else "OFF",
                    style = AppTheme.typography.labelSmall,
                    color = if (isActive) AppTheme.colors.background else TextMuted
                )
            }
        }
    }
}

/**
 * Modern tactile vertical volume slider with smooth touch drag.
 */
@Composable
private fun VerticalVolumeSlider(
    volume: Float,
    isActive: Boolean,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var heightPx by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0F1526))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(18.dp)
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    change.consume()
                    val newFraction = 1f - (change.position.y / size.height)
                    onVolumeChange(newFraction.coerceIn(0f, 1f))
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Active Volume Fill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = volume.coerceIn(0.02f, 1f))
                .background(
                    brush = if (isActive) {
                        Brush.verticalGradient(
                            colors = listOf(GoldPrimary, TealPrimary)
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(TextMuted.copy(alpha = 0.4f), TextMuted.copy(alpha = 0.2f))
                        )
                    }
                )
        )

        // Custom Slider Thumb Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(width = 16.dp, height = 3.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
    }
}
