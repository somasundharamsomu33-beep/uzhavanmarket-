package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val UzhavanColorScheme = lightColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AgriGreenContainer,
    onPrimaryContainer = AgriGreenOnContainer,
    secondary = AgriGreenDark,
    onSecondary = Color.White,
    secondaryContainer = AgriSurfaceVariant,
    onSecondaryContainer = AgriGreenOnContainer,
    tertiary = AgriGold,
    onTertiary = Color.White,
    tertiaryContainer = AgriGoldContainer,
    onTertiaryContainer = Color(0xFF713F12),
    background = AgriBackground,
    onBackground = AgriTextPrimary,
    surface = AgriSurface,
    onSurface = AgriTextPrimary,
    surfaceVariant = AgriSurfaceVariant,
    onSurfaceVariant = AgriTextSecondary,
    outline = AgriOutline,
    error = AgriError,
    onError = Color.White
)

@Composable
fun UzhavanMarketTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UzhavanColorScheme,
        typography = Typography,
        content = content
    )
}
