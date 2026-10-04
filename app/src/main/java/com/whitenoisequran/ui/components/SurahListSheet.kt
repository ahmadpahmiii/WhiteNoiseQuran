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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whitenoisequran.R
import com.whitenoisequran.domain.model.BulkDownloadProgress
import com.whitenoisequran.domain.model.DownloadState
import com.whitenoisequran.domain.model.Surah
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicItemStyle
import com.whitenoisequran.ui.theme.CardDark
import com.whitenoisequran.ui.theme.ErrorRed
import com.whitenoisequran.ui.theme.GoldLight
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.SuccessGreen
import com.whitenoisequran.ui.theme.SurfaceDark
import com.whitenoisequran.ui.theme.TealLight
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListSheet(
    surahs: List<Surah>,
    currentSurah: Surah?,
    isLoading: Boolean = false,
    downloadProgress: BulkDownloadProgress = BulkDownloadProgress(),
    onSelectSurah: (Surah) -> Unit,
    onDownloadSingleSurah: (Surah) -> Unit = {},
    onDeleteSurahAudio: (Surah) -> Unit = {},
    onCancelDownload: (Surah) -> Unit = {},
    onOpenDownloads: () -> Unit = {},
    onDeleteAllAudio: () -> Unit = {},
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var searchQuery by remember { mutableStateOf("") }
    var downloadedOnly by remember { mutableStateOf(false) }
    val meanings = stringArrayResource(R.array.surah_meanings) // in the app's language

    val filteredSurahs = remember(surahs, searchQuery, downloadedOnly, meanings) {
        val query = searchQuery.trim().lowercase()
        surahs.filter {
            (!downloadedOnly || it.downloadState == DownloadState.DONE) && (
                    query.isEmpty() ||
                            it.number.toString().startsWith(query) || // "4" finds 4 and 40–49
                it.nameLatin.lowercase().contains(query) ||
                it.nameArabic.contains(query) ||
                            meanings[it.number - 1].lowercase().contains(query)
                    )
        }
    }

    val downloadedCount = remember(surahs) {
        surahs.count { it.downloadState == DownloadState.DONE }
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
                .fillMaxHeight(0.92f)
                .padding(horizontal = 20.dp)
        ) {
            // Header: Title + Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.surah_index_title),
                        style = AppTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.downloaded_for_offline, downloadedCount),
                        style = AppTheme.typography.bodySmall,
                        color = if (downloadedCount > 0) SuccessGreen else TextMuted
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Storage & Bulk Download Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardDark)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Opens the download screen, which starts or resumes the bulk download and can pause it
                if (downloadedCount < 114) {
                    val tint = if (downloadProgress.isRunning) TealLight else GoldPrimary
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onOpenDownloads() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when {
                                downloadProgress.isWaitingForNetwork ->
                                    stringResource(
                                        R.string.waiting_for_connection_count,
                                        downloadedCount
                                    )

                                downloadProgress.isRunning -> stringResource(
                                    R.string.downloading_count,
                                    downloadedCount
                                )

                                downloadedCount > 0 -> stringResource(
                                    R.string.download_remaining,
                                    114 - downloadedCount
                                )

                                else -> stringResource(R.string.download_all)
                            },
                            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = tint
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Delete All (Free Storage) Button
                if (downloadedCount > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onDeleteAllAudio() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.cd_delete_all_audio),
                            tint = ErrorRed.copy(alpha = 0.85f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.free_space),
                            style = AppTheme.typography.labelSmall,
                            color = ErrorRed.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_surah_hint),
                        style = AppTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
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

            Spacer(modifier = Modifier.height(10.dp))

            // Loading State
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = GoldPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = stringResource(R.string.loading_surahs),
                            style = AppTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                // Surah count + All / Downloaded filter (offline, only downloaded surahs play)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.surah_count,
                            filteredSurahs.size,
                            filteredSurahs.size
                        ),
                        style = AppTheme.typography.labelMedium,
                        color = TextMuted,
                        modifier = Modifier.weight(1f)
                    )
                    listOf(
                        false to stringResource(R.string.filter_all),
                        true to stringResource(R.string.filter_downloaded)
                    ).forEach { (onlyDownloaded, label) ->
                        val selected = downloadedOnly == onlyDownloaded
                        FilterChip(
                            selected = selected,
                            onClick = { downloadedOnly = onlyDownloaded },
                            // Unspecified: follow the chip's (selected) label color
                            label = {
                                Text(
                                    text = label,
                                    style = AppTheme.typography.labelSmall.copy(color = Color.Unspecified)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.Transparent,
                                labelColor = TextSecondary,
                                selectedContainerColor = GoldPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = GoldPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = Color.White.copy(alpha = 0.08f),
                                selectedBorderColor = GoldPrimary.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }

                // Surah List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (filteredSurahs.isEmpty()) {
                        item {
                            Text(
                                text = if (searchQuery.isBlank()) {
                                    stringResource(R.string.nothing_downloaded)
                                } else {
                                    stringResource(R.string.no_surah_match, searchQuery.trim())
                                },
                                style = AppTheme.typography.bodyMedium,
                                color = TextMuted,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        }
                    }
                    items(
                        items = filteredSurahs,
                        key = { it.number }
                    ) { surah ->
                        val isPlaying = currentSurah?.number == surah.number

                        SurahListItem(
                            surah = surah,
                            meaning = meanings[surah.number - 1],
                            isPlaying = isPlaying,
                            downloadPercent = downloadProgress.surahPercent[surah.number],
                            onClick = {
                                onSelectSurah(surah)
                                onDismiss()
                            },
                            onDownload = { onDownloadSingleSurah(surah) },
                            onCancelDownload = { onCancelDownload(surah) },
                            onDelete = { onDeleteSurahAudio(surah) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SurahListItem(
    surah: Surah,
    meaning: String,
    isPlaying: Boolean,
    downloadPercent: Int?,
    onClick: () -> Unit,
    onDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onDelete: () -> Unit
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
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Main clickable body (select Surah)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Number Badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
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
                            contentDescription = stringResource(R.string.cd_playing),
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

                Spacer(modifier = Modifier.width(12.dp))

                // Center: Latin Name & Ayat Count
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = surah.nameLatin,
                        style = AppTheme.typography.titleMedium.copy(fontSize = 15.sp),
                        color = if (isPlaying) GoldLight else TextPrimary
                    )
                    Text(
                        text = stringResource(
                            R.string.surah_ayat_meaning,
                            surah.numberOfAyah,
                            meaning
                        ),
                        style = AppTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Arabic Scripture Name
                Text(
                    text = surah.nameArabic,
                    style = ArabicItemStyle.copy(fontSize = 18.sp),
                    color = if (isPlaying) GoldLight else TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Download Status Icon & Actions (completely independent click)
            when (surah.downloadState) {
                DownloadState.DONE -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.cd_downloaded),
                            tint = SuccessGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = stringResource(R.string.cd_delete_from_storage),
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                DownloadState.DOWNLOADING -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (downloadPercent != null) {
                            Text(
                                text = "$downloadPercent%",
                                style = AppTheme.typography.labelSmall,
                                color = TealLight
                            )
                        }
                        // Ring with a stop mark, like a store download: tap to cancel this surah
                        IconButton(
                            onClick = onCancelDownload,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                // No percent yet = queued or waiting for network: spin instead
                                if (downloadPercent != null) {
                                    CircularProgressIndicator(
                                        progress = { downloadPercent / 100f },
                                        color = TealLight,
                                        trackColor = TealLight.copy(alpha = 0.2f),
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    CircularProgressIndicator(
                                        color = TealLight,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.cd_cancel_download),
                                    tint = TealLight,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                DownloadState.FAILED -> {
                    IconButton(
                        onClick = onDownload,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.cd_download_failed_retry),
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                DownloadState.NONE -> {
                    IconButton(
                        onClick = onDownload,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = stringResource(R.string.cd_download_surah),
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
