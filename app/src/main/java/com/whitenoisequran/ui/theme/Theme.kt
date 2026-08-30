package com.whitenoisequran.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = SurfaceDark,
    primaryContainer = CardDark,
    onPrimaryContainer = GoldLight,
    secondary = TealPrimary,
    onSecondary = SurfaceDark,
    secondaryContainer = CardDark,
    onSecondaryContainer = TealLight,
    tertiary = GoldLight,
    background = BackgroundNavy,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = ErrorRed,
    onError = TextPrimary
)

@Composable
fun WhiteNoiseQuranTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = BackgroundNavy.toArgb()
                window.navigationBarColor = BackgroundNavy.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    val customColors = CustomAppColors()

    CompositionLocalProvider(
        LocalAppColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val colors: CustomAppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}
