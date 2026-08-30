package com.whitenoisequran.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicItemStyle
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
fun ReciterCard(
    reciter: Reciter,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) GoldPrimary else Color.White.copy(alpha = 0.06f),
        label = "reciter_card_border"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CardDark.copy(alpha = 0.95f) else CardDark,
        label = "reciter_card_bg"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = GoldGlow,
                spotColor = GoldPrimary
            )
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Avatar initial in gold circular container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) GoldPrimary.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) GoldPrimary else Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reciter.avatarInitial,
                    style = ArabicItemStyle.copy(fontSize = 22.sp),
                    color = if (isSelected) GoldPrimary else GoldLight
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Center: Reciter Name & Arabic Name
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = reciter.name,
                        style = AppTheme.typography.titleMedium,
                        color = TextPrimary
                    )

                    if (reciter.isPopular) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TealPrimary.copy(alpha = 0.2f))
                                .border(1.dp, TealPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Popular",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                        }
                    }
                }

                Text(
                    text = reciter.nameArabic,
                    style = AppTheme.typography.bodySmall,
                    color = if (isSelected) GoldLight else TextMuted
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right: Radio Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) GoldPrimary else Color.Transparent)
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) GoldPrimary else TextMuted.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = BackgroundNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
