package com.whitenoisequran.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.whitenoisequran.ui.theme.GoldGlow
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlayerArtwork(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "player_art_animations")

    // Rotation angle for orbital star ring (slow, soothing rotation)
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    // Pulse animation for glow and crescent breath at ~60 BPM (1 second period)
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isPlaying) 1.06f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = if (isPlaying) 0.50f else 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(240.dp)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width / 2f - 12.dp.toPx()
            val innerArtRadius = outerRadius * 0.85f

            // 1. Soft Radial Glow Background
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GoldGlow.copy(alpha = glowAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = outerRadius * 1.3f * pulseScale
                ),
                radius = outerRadius * 1.3f * pulseScale,
                center = center
            )

            // 2. Deep Circular Art Disk (Midnight Blue / Deep Purple gradient)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1E1E45),
                        Color(0xFF13172E),
                        Color(0xFF0D1124)
                    ),
                    center = center,
                    radius = innerArtRadius
                ),
                radius = innerArtRadius,
                center = center
            )

            // 3. Subtle Inner Border
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = innerArtRadius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // 4. Rotating Outer Orbital Ring & Stars
            rotate(degrees = if (isPlaying) orbitAngle else 45f, pivot = center) {
                // Orbital track
                drawCircle(
                    color = GoldPrimary.copy(alpha = 0.15f),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                // Active glowing arc segment
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Transparent,
                            GoldLight.copy(alpha = 0.6f),
                            GoldPrimary,
                            Color.Transparent
                        ),
                        center = center
                    ),
                    startAngle = 0f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                    size = androidx.compose.ui.geometry.Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Orbital Star Particles
                val starAngles = listOf(0.0, 72.0, 144.0, 216.0, 288.0)
                starAngles.forEachIndexed { index, deg ->
                    val rad = Math.toRadians(deg)
                    val starCenter = Offset(
                        (center.x + outerRadius * cos(rad)).toFloat(),
                        (center.y + outerRadius * sin(rad)).toFloat()
                    )
                    val starSize = if (index % 2 == 0) 3.5.dp.toPx() else 2.0.dp.toPx()
                    drawCircle(
                        color = GoldLight,
                        radius = starSize,
                        center = starCenter
                    )
                }
            }

            // 5. Central Islamic Crescent Moon (Math Path)
            val moonRadius = innerArtRadius * 0.42f * pulseScale
            val moonOffset = moonRadius * 0.40f

            val outerMoonPath = Path().apply {
                addOval(Rect(center.x - moonRadius, center.y - moonRadius, center.x + moonRadius, center.y + moonRadius))
            }
            val innerCutPath = Path().apply {
                addOval(Rect(center.x - moonRadius + moonOffset, center.y - moonRadius - (moonOffset * 0.3f), center.x + moonRadius + moonOffset, center.y + moonRadius - (moonOffset * 0.3f)))
            }

            val crescentPath = Path().apply {
                op(outerMoonPath, innerCutPath, PathOperation.Difference)
            }

            // Crescent glow
            drawPath(
                path = crescentPath,
                brush = Brush.linearGradient(
                    colors = listOf(GoldLight, GoldPrimary),
                    start = Offset(center.x - moonRadius, center.y - moonRadius),
                    end = Offset(center.x + moonRadius, center.y + moonRadius)
                )
            )

            // Small glowing star next to crescent
            val starX = center.x + moonRadius * 0.45f
            val starY = center.y - moonRadius * 0.25f
            drawCircle(
                color = GoldLight,
                radius = 3.5.dp.toPx() * pulseScale,
                center = Offset(starX, starY)
            )
        }
    }
}
