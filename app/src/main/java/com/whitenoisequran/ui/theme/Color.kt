package com.whitenoisequran.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Theme Palette
val BackgroundNavy = Color(0xFF0A0E1F)
val SurfaceDark = Color(0xFF111827)
val CardDark = Color(0xFF1A2235)
val CardDarkFrosted = Color(0xD91A2235) // 85% opacity

// Accents
val GoldPrimary = Color(0xFFC9A84C)
val GoldLight = Color(0xFFE8C97A)
val GoldDark = Color(0xFF997D2F)
val GoldGlow = Color(0x40C9A84C)

val TealPrimary = Color(0xFF2DD4BF)
val TealLight = Color(0xFF5EEAD4)
val TealDark = Color(0xFF0F766E)
val TealGlow = Color(0x332DD4BF)

// Neutrals & Text
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Borders
val BorderSubtle = Color(0x12FFFFFF) // ~7% white
val BorderGold = Color(0x66C9A84C)
val BorderTeal = Color(0x662DD4BF)

// Functional
val ErrorRed = Color(0xFFF87171)
val SuccessGreen = Color(0xFF34D399)

// Gradients
val GoldGradient = Brush.horizontalGradient(
    colors = listOf(GoldLight, GoldPrimary)
)

val TealGradient = Brush.horizontalGradient(
    colors = listOf(TealLight, TealPrimary)
)

val ProgressGradient = Brush.horizontalGradient(
    colors = listOf(TealPrimary, GoldPrimary)
)

val CardGlowGradient = Brush.radialGradient(
    colors = listOf(GoldGlow, Color.Transparent)
)

val PlayerArtGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0D1530),
        Color(0xFF1A1A3E),
        Color(0xFF0A0E1F)
    )
)

@Immutable
data class CustomAppColors(
    val background: Color = BackgroundNavy,
    val surface: Color = SurfaceDark,
    val card: Color = CardDark,
    val cardFrosted: Color = CardDarkFrosted,
    val gold: Color = GoldPrimary,
    val goldLight: Color = GoldLight,
    val goldGlow: Color = GoldGlow,
    val teal: Color = TealPrimary,
    val tealLight: Color = TealLight,
    val tealGlow: Color = TealGlow,
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val textMuted: Color = TextMuted,
    val borderSubtle: Color = BorderSubtle,
    val borderGold: Color = BorderGold,
    val borderTeal: Color = BorderTeal,
    val error: Color = ErrorRed,
    val success: Color = SuccessGreen
)

val LocalAppColors = staticCompositionLocalOf { CustomAppColors() }
