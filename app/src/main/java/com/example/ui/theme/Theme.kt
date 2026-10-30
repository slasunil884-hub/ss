package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalKarenTheme = staticCompositionLocalOf { KarenOsTheme.SPIDERMAN_MIX }

@Composable
fun KarenStarkOSTheme(
    activeTheme: KarenOsTheme = KarenOsTheme.SPIDERMAN_MIX,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = activeTheme.primary,
        onPrimary = Color.Black,
        primaryContainer = activeTheme.primary.copy(alpha = 0.2f),
        onPrimaryContainer = activeTheme.primary,
        secondary = activeTheme.secondary,
        onSecondary = Color.Black,
        secondaryContainer = activeTheme.secondary.copy(alpha = 0.2f),
        onSecondaryContainer = activeTheme.secondary,
        background = activeTheme.background,
        onBackground = CyberTextPrimary,
        surface = activeTheme.surface,
        onSurface = CyberTextPrimary,
        surfaceVariant = CyberDarkSurfaceVariant,
        onSurfaceVariant = CyberTextSecondary,
        outline = activeTheme.borderGlow,
        error = CyberError,
        onError = Color.White
    )

    CompositionLocalProvider(LocalKarenTheme provides activeTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = KarenTypography,
            content = content
        )
    }
}
