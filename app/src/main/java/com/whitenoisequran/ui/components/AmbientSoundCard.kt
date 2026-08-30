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
import androidx.compose.runtime.remember
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
        targetValue = if (isActive) TealPrimary.copy(alpha = 0.85f) else Color.White.copy(alpha = 0.08f),
        label = "card_border_color"
    )

    val shadowElevation = if (isActive) 12.dp else 0.dp

    // Subtle breathing pulse when actively playing
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
            .clickable { onToggle(!isActive) } // Tap anywhere on the card to easily toggle!
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Top Section: Icon & UX Copy
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Circular Glowing Icon Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        TealPrimary.copy(alpha = 0.35f),
                                        Color(0xFF0F1526)
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.05f),
                                        Color(0xFF0F1526)
                                    )
                                )
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isActive) TealLight.copy(alpha = 0.7f) else Color.White.copy(
                                alpha = 0.1f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconResId != 0) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = sound.name,
                            modifier = Modifier.size(26.dp),
                            colorFilter = if (isActive) ColorFilter.tint(TealLight) else ColorFilter.tint(TextSecondary)
                        )
                    } else {
                        Text(
                            text = sound.iconEmoji,
                            fontSize = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title
                Text(
                    text = sound.name,
                    style = AppTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp
                    ),
                    color = if (isActive) TextPrimary else TextSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle / Mood Microcopy
                if (sound.subtitle.isNotEmpty()) {
                    Text(
                        text = sound.subtitle,
                        style = AppTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = if (isActive) GoldLight.copy(alpha = 0.95f) else TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }

            // 2. Center Section: Tactile Volume Slider & Percentage
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
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
                        .height(78.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${(sound.volume * 100).roundToInt()}%",
                    style = AppTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    color = if (isActive) TealLight else TextMuted
                )
            }

            // 3. Bottom Section: Tactile State Chip (Fixed Height & Centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isActive) {
                            Brush.horizontalGradient(listOf(TealPrimary, TealDark))
                        } else {
                            Brush.horizontalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.07f),
                                    Color.White.copy(alpha = 0.04f)
                                )
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "OFF",
                    style = AppTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (isActive) Color(0xFF041815) else TextMuted,
                    textAlign = TextAlign.Center
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
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0C101D))
            .border(
                width = 1.dp,
                color = if (isActive) TealPrimary.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f),
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
                            colors = listOf(
                                TextMuted.copy(alpha = 0.35f),
                                TextMuted.copy(alpha = 0.15f)
                            )
                        )
                    }
                )
        )

        // Custom Slider Thumb Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 3.5.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(width = 16.dp, height = 3.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color.White else Color.White.copy(alpha = 0.4f))
            )
        }
    }
}
