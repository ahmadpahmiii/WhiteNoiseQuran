package com.whitenoisequran.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.CardDarkFrosted
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealDark
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
    val context = LocalContext.current

    val borderColor by animateColorAsState(
        targetValue = if (isActive) TealPrimary.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.08f),
        label = "card_border_color"
    )

    val shadowElevation = if (isActive) 10.dp else 0.dp

    // Subtle breathing pulsation when track is actively playing
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isActive) 1.06f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val iconResId = remember(sound.iconDrawableName) {
        if (sound.iconDrawableName.isNotEmpty()) {
            context.resources.getIdentifier(sound.iconDrawableName, "drawable", context.packageName)
        } else 0
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(22.dp),
                ambientColor = TealGlow,
                spotColor = TealPrimary
            )
            .clip(RoundedCornerShape(22.dp))
            .background(CardDarkFrosted)
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Sound Icon Badge & Copywriting
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Visual Icon Badge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                Brush.radialGradient(
                                    colors = listOf(TealPrimary.copy(alpha = 0.35f), Color(0xFF0F1526))
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(Color.White.copy(alpha = 0.06f), Color(0xFF0F1526))
                                )
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isActive) TealLight.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconResId != 0) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = sound.name,
                            modifier = Modifier.size(28.dp),
                            colorFilter = if (isActive) ColorFilter.tint(TealLight) else ColorFilter.tint(TextSecondary)
                        )
                    } else {
                        Text(
                            text = sound.iconEmoji,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // UX Title
                Text(
                    text = sound.name,
                    style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isActive) TextPrimary else TextSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // UX Subtitle / Mood Microcopy
                if (sound.subtitle.isNotEmpty()) {
                    Text(
                        text = sound.subtitle,
                        style = AppTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = if (isActive) GoldLight.copy(alpha = 0.9f) else TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

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
                    .width(38.dp)
                    .height(90.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Volume Percentage Label
            Text(
                text = "${(sound.volume * 100).roundToInt()}%",
                style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = if (isActive) TealLight else TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tactile ON/OFF Toggle Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isActive) {
                            Brush.horizontalGradient(listOf(TealPrimary, TealDark))
                        } else {
                            Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.06f), Color.White.copy(alpha = 0.04f)))
                        }
                    )
                    .clickable { onToggle(!isActive) }
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "OFF",
                    style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                    color = if (isActive) Color(0xFF071B18) else TextMuted
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
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(19.dp))
            .background(Color(0xFF0C101D))
            .border(
                width = 1.dp,
                color = if (isActive) TealPrimary.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(19.dp)
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
                            colors = listOf(TextMuted.copy(alpha = 0.35f), TextMuted.copy(alpha = 0.15f))
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
                    .size(width = 18.dp, height = 3.5.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color.White else Color.White.copy(alpha = 0.5f))
            )
        }
    }
}
