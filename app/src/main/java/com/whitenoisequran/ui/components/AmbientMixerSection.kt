package com.whitenoisequran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.R
import com.whitenoisequran.domain.model.AmbientSound
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.TealLight
import com.whitenoisequran.ui.theme.TealPrimary
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun AmbientMixerSection(
    sounds: List<AmbientSound>,
    /** The mix is kept across launches but only plays after Play (here or the Quran's). */
    isMixPlaying: Boolean,
    onToggleMix: () -> Unit,
    onVolumeChange: (String, Float) -> Unit,
    onToggleSound: (String, Boolean) -> Unit,
    onResetAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeCount = sounds.count { it.isEnabled }
    val anyActive = activeCount > 0

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
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = stringResource(R.string.ambient_title),
                    style = AppTheme.typography.headlineMedium,
                    color = GoldPrimary
                )

                // Play/pause for the whole mix, which can play without the Quran
                if (anyActive) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(TealPrimary.copy(alpha = 0.15f))
                            .border(1.dp, TealLight.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable(onClick = onToggleMix)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (isMixPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = stringResource(
                                if (isMixPlaying) R.string.cd_pause_ambient else R.string.cd_play_ambient
                            ),
                            tint = TealLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = pluralStringResource(
                                R.plurals.ambient_sound_count,
                                activeCount,
                                activeCount
                            ),
                            style = AppTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = TealLight
                        )
                    }
                }
            }

            if (anyActive) {
                TextButton(onClick = onResetAll) {
                    Text(
                        text = stringResource(R.string.reset_all),
                        style = AppTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sound Grid (rendered as 2 columns per row)
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
                        isPlaying = sound.isEnabled && isMixPlaying,
                        onVolumeChange = { vol -> onVolumeChange(sound.id, vol) },
                        onToggle = { isEn -> onToggleSound(sound.id, isEn) },
                        modifier = Modifier
                            .weight(1f)
                            .height(264.dp)
                    )
                }
                // If odd number of items, fill spacer
                if (rowSounds.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ambient Footer Note
        Text(
            text = stringResource(R.string.ambient_footer),
            style = AppTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = TextMuted,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )
    }
}
