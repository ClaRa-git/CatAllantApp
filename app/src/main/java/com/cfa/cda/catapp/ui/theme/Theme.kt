package com.cfa.cda.catapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun CatApiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val catColors = if (darkTheme) DarkCatColors else LightCatColors

    val materialScheme = if (darkTheme) {
        darkColorScheme(
            primary = catColors.primary,
            background = catColors.background,
            surface = catColors.card,
            onPrimary = catColors.card,
            onBackground = catColors.textPrimary,
            onSurface = catColors.textPrimary
        )
    } else {
        lightColorScheme(
            primary = catColors.primary,
            background = catColors.background,
            surface = catColors.card,
            onPrimary = catColors.card,
            onBackground = catColors.textPrimary,
            onSurface = catColors.textPrimary
        )
    }

    CompositionLocalProvider(LocalCatColors provides catColors) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = CatTypography,
            content = content
        )
    }
}