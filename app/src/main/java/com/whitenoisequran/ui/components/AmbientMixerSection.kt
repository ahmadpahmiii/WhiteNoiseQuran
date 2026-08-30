package com.whitenoisequran.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun AmbientMixerSection(
    sounds: List<AmbientSound>,
    onVolumeChange: (String, Float) -> Unit,
    onToggleSound: (String, Boolean) -> Unit,
    onResetAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val anyActive = sounds.any { it.isEnabled }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🎵",
                    style = AppTheme.typography.titleMedium
                )
                Text(
                    text = "Ambient Mix",
                    style = AppTheme.typography.headlineMedium,
                    color = GoldPrimary
                )
            }

            if (anyActive) {
                TextButton(onClick = onResetAll) {
                    Text(
                        text = "Reset All",
                        style = AppTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Sound Grid (rendered as 2 rows of 2 columns)
        val chunkedSounds = sounds.chunked(2)
        chunkedSounds.forEach { rowSounds ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowSounds.forEach { sound ->
                    AmbientSoundCard(
                        sound = sound,
                        onVolumeChange = { vol -> onVolumeChange(sound.id, vol) },
                        onToggle = { isEn -> onToggleSound(sound.id, isEn) },
                        modifier = Modifier
                            .weight(1f)
                            .height(230.dp)
                    )
                }
                // If odd number of items, fill spacer
                if (rowSounds.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Teaser label
        Text(
            text = "✨ More sounds coming soon",
            style = AppTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = TextMuted,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp)
        )
    }
}
