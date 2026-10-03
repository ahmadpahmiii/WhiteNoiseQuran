package com.whitenoisequran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.whitenoisequran.domain.model.Reciter
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.SurfaceDark
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary

/** Switches the reciter in place; shows how much of each is downloaded, since only that plays offline. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterSheet(
    reciters: List<Reciter>,
    currentReciterId: Int?,
    downloadCounts: Map<Int, Int>,
    onSelect: (Reciter) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Choose Reciter",
                style = AppTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Text(
                text = "Downloaded surahs play without internet; the rest stream.",
                style = AppTheme.typography.bodySmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = reciters, key = { it.id }) { reciter ->
                    ReciterCard(
                        reciter = reciter,
                        isSelected = reciter.id == currentReciterId,
                        downloadedCount = downloadCounts[reciter.id] ?: 0,
                        onSelect = { onSelect(reciter) }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
