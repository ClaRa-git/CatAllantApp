package com.cfa.cda.catapp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CatColorScheme(
    val background: Color,
    val primary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val accent: Color,
    val card: Color,
    val imagePlaceholder: Color,
    val ratingStar: Color
)

val LightCatColors = CatColorScheme(
    background = CatColors.Background,
    primary = CatColors.Primary,
    textPrimary = CatColors.TextPrimary,
    textSecondary = CatColors.TextSecondary,
    border = CatColors.Border,
    accent = CatColors.Accent,
    card = CatColors.Card,
    imagePlaceholder = CatColors.ImagePlaceholder,
    ratingStar = CatColors.RatingStar
)

val DarkCatColors = CatColorScheme(
    background = CatColorsDark.Background,
    primary = CatColorsDark.Primary,
    textPrimary = CatColorsDark.TextPrimary,
    textSecondary = CatColorsDark.TextSecondary,
    border = CatColorsDark.Border,
    accent = CatColorsDark.Accent,
    card = CatColorsDark.Card,
    imagePlaceholder = CatColorsDark.ImagePlaceholder,
    ratingStar = CatColorsDark.RatingStar
)

val LocalCatColors = staticCompositionLocalOf { LightCatColors }