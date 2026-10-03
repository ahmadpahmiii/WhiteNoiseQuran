package com.whitenoisequran.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.ErrorRed
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealPrimary
import com.whitenoisequran.ui.theme.TextMuted

@Composable
fun SurahDownloadGrid(
    surahs: List<Surah>,
    surahPercent: Map<Int, Int>,
    onRetry: (Surah) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "download_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "active_pulse_alpha"
    )

    // 6 columns
    val columns = 6
    val chunked = surahs.chunked(columns)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        chunked.forEach { rowSurahs ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rowSurahs.forEach { surah ->
                    SurahGridTile(
                        surah = surah,
                        percent = surahPercent[surah.number],
                        pulseAlpha = pulseAlpha,
                        onRetry = { onRetry(surah) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remainder of row if incomplete
                val remaining = columns - rowSurahs.size
                if (remaining > 0) {
                    for (i in 0 until remaining) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SurahGridTile(
    surah: Surah,
    percent: Int?,
    pulseAlpha: Float,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = surah.downloadState

    val bgColor = when (state) {
        DownloadState.DONE -> GoldPrimary.copy(alpha = 0.20f)
        DownloadState.DOWNLOADING -> TealPrimary.copy(alpha = pulseAlpha * 0.35f)
        DownloadState.FAILED -> ErrorRed.copy(alpha = 0.25f)
        DownloadState.NONE -> CardDark
    }

    val borderColor = when (state) {
        DownloadState.DONE -> GoldPrimary.copy(alpha = 0.7f)
        DownloadState.DOWNLOADING -> TealPrimary.copy(alpha = pulseAlpha)
        DownloadState.FAILED -> ErrorRed.copy(alpha = 0.8f)
        DownloadState.NONE -> Color.White.copy(alpha = 0.05f)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(
                enabled = state == DownloadState.FAILED,
                onClickLabel = "Retry download",
                onClick = onRetry
            ),
        contentAlignment = Alignment.Center
    ) {
        // Tile fills from the bottom as the file downloads
        if (state == DownloadState.DOWNLOADING && percent != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(percent / 100f)
                    .background(TealPrimary.copy(alpha = 0.3f))
            )
        }

        when (state) {
            DownloadState.DONE -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${surah.number}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }
            DownloadState.DOWNLOADING -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${surah.number}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                    if (percent != null) {
                        Text(
                            text = "$percent%",
                            fontSize = 9.sp,
                            color = TealPrimary
                        )
                    }
                }
            }
            DownloadState.FAILED -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${surah.number}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Failed, tap to retry",
                        tint = ErrorRed,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            DownloadState.NONE -> {
                Text(
                    text = "${surah.number}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }
        }
    }
}
