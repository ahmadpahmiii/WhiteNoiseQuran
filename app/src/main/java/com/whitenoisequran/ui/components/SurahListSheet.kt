package com.whitenoisequran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicItemStyle
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.GoldGlow
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.SurfaceDark
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListSheet(
    surahs: List<Surah>,
    currentSurah: Surah?,
    onSelectSurah: (Surah) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredSurahs = remember(surahs, searchQuery) {
        if (searchQuery.isBlank()) {
            surahs
        } else {
            val query = searchQuery.trim().lowercase()
            surahs.filter {
                it.number.toString() == query ||
                it.nameLatin.lowercase().contains(query) ||
                it.nameArabic.contains(query) ||
                it.translationId.lowercase().contains(query)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 38.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 20.dp)
        ) {
            // Header: Title + Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Surah",
                    style = AppTheme.typography.headlineMedium,
                    color = TextPrimary
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search by number or name…",
                        style = AppTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GoldPrimary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark,
                    focusedIndicatorColor = GoldPrimary,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.08f),
                    cursorColor = GoldPrimary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Surah Count Subtitle
            Text(
                text = "${filteredSurahs.size} Surahs",
                style = AppTheme.typography.labelMedium,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Surah List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = filteredSurahs,
                    key = { it.number }
                ) { surah ->
                    val isPlaying = currentSurah?.number == surah.number

                    SurahListItem(
                        surah = surah,
                        isPlaying = isPlaying,
                        onClick = {
                            onSelectSurah(surah)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SurahListItem(
    surah: Surah,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isPlaying) GoldPrimary.copy(alpha = 0.12f) else Color.Transparent)
            .border(
                width = if (isPlaying) 1.dp else 0.dp,
                color = if (isPlaying) GoldPrimary.copy(alpha = 0.4f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Number Badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isPlaying) GoldPrimary else CardDark)
                    .border(
                        1.dp,
                        if (isPlaying) GoldPrimary else Color.White.copy(alpha = 0.08f),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Playing",
                        tint = SurfaceDark,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "${surah.number}",
                        style = AppTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Center: Latin Name & Ayat Count
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = surah.nameLatin,
                    style = AppTheme.typography.titleMedium,
                    color = if (isPlaying) GoldLight else TextPrimary
                )
                Text(
                    text = "${surah.numberOfAyah} Ayat · ${surah.translationId}",
                    style = AppTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            // Right: Arabic Scripture Name
            Text(
                text = surah.nameArabic,
                style = ArabicItemStyle.copy(fontSize = 20.sp),
                color = if (isPlaying) GoldLight else TextPrimary
            )
        }
    }
}
