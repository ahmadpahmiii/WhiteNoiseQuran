package com.whitenoisequran.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whitenoisequran.ui.components.IslamicBackgroundPattern
import com.whitenoisequran.ui.components.ReciterCard
import com.whitenoisequran.ui.theme.AppTheme
import com.whitenoisequran.ui.theme.ArabicSubtitleStyle
import com.whitenoisequran.ui.theme.BackgroundNavy
import com.whitenoisequran.ui.theme.GoldGlow
import com.whitenoisequran.ui.theme.GoldPrimary
import com.whitenoisequran.ui.theme.SurfaceDark
import com.whitenoisequran.ui.theme.TextMuted
import com.whitenoisequran.ui.theme.TextPrimary
import com.whitenoisequran.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    onNavigateToDownload: (reciterId: Int) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BackgroundNavy
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Subtle night starry background
            IslamicBackgroundPattern()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                // App Brand Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Nightlight,
                        contentDescription = "Logo",
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "White Noise Quran",
                        style = AppTheme.typography.titleLarge,
                        color = GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Arabic Title
                Text(
                    text = "اختر القارئ",
                    style = ArabicSubtitleStyle.copy(fontSize = 30.sp),
                    color = TextPrimary
                )

                // Subtitle
                Text(
                    text = "Choose Your Reciter",
                    style = AppTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Reciters List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.reciters,
                        key = { it.id }
                    ) { reciter ->
                        val isSelected = uiState.selectedReciter?.id == reciter.id
                        ReciterCard(
                            reciter = reciter,
                            isSelected = isSelected,
                            onSelect = { viewModel.selectReciter(reciter) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // Pinned Bottom Continue Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(BackgroundNavy.copy(alpha = 0.92f))
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.completeOnboarding { reciterId ->
                            onNavigateToDownload(reciterId)
                        }
                    },
                    enabled = uiState.selectedReciter != null,
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = SurfaceDark,
                        disabledContainerColor = SurfaceDark,
                        disabledContentColor = TextMuted
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(
                            elevation = if (uiState.selectedReciter != null) 12.dp else 0.dp,
                            shape = RoundedCornerShape(26.dp),
                            ambientColor = GoldGlow,
                            spotColor = GoldPrimary
                        )
                ) {
                    Text(
                        text = "Continue",
                        style = AppTheme.typography.labelLarge.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
