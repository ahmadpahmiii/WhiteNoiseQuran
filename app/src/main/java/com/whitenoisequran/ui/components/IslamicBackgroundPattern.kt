package com.whitenoisequran.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Renders a subtle starry night texture with faint geometric accents at ~4-6% opacity.
 * Minimalist, elegant, non-distracting background element.
 */
@Composable
fun IslamicBackgroundPattern(
    modifier: Modifier = Modifier,
    starCount: Int = 45,
    starColor: Color = Color.White.copy(alpha = 0.08f)
) {
    // Generate deterministic star positions based on a fixed seed
    val starPoints = remember(starCount) {
        val random = Random(42)
        List(starCount) {
            Triple(
                random.nextFloat(), // x fraction (0..1)
                random.nextFloat(), // y fraction (0..1)
                random.nextFloat() * 1.5f + 0.8f // radius (0.8..2.3f)
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        starPoints.forEach { (xFraction, yFraction, radius) ->
            drawCircle(
                color = starColor,
                radius = radius,
                center = Offset(xFraction * width, yFraction * height)
            )
        }
    }
}
