package org.darthacheron.fitbe.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import org.darthacheron.fitbe.settings.ThemeMode

private val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF6200EE), // A vibrant purple for primary actions
//    onPrimary = Color.White,
//    primaryContainer = Color(0xFFBB86FC),
//    onPrimaryContainer = Color.Black,
//    secondary = Color(0xFF03DAC6), // A teal for secondary actions
//    onSecondary = Color.Black,
//    secondaryContainer = Color(0xFF018786),
//    onSecondaryContainer = Color.White,
//    tertiary = Color(0xFF3700B3),
//    onTertiary = Color.White,
//    tertiaryContainer = Color(0xFF6200EE),
//    onTertiaryContainer = Color.White,
//    error = Color(0xFFB00020),
//    onError = Color.White,
//    errorContainer = Color(0xFFFCD8DF),
//    onErrorContainer = Color.Black,
//    background = Color(0xFFFFFFFF), // Clean white background
//    onBackground = Color.Black,
//    surface = Color(0xFFFFFFFF), // Surfaces like cards, sheets
//    onSurface = Color.Black,
//    surfaceVariant = Color(0xFFE0E0E0), // Subtle variations for surfaces
//    onSurfaceVariant = Color.Black,
//    outline = Color(0xFF757575),
//    inverseOnSurface = Color.White,
//    inverseSurface = Color.Black,
//    inversePrimary = Color(0xFFBB86FC),
//    surfaceTint = Color(0xFF6200EE), // Used for elevation overlays
//    outlineVariant = Color(0xFFBDBDBD),
//    scrim = Color.Black,
    primary = Color(0xFFFFC107), // A vibrant, warm yellow (Amber 500)
    onPrimary = Color.Black, // Black text on yellow for good contrast
    primaryContainer = Color(0xFFFFD54F), // Lighter yellow for containers
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF4CAF50), // A complementary green
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8E6C9),
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFF2196F3), // A contrasting blue for accents
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBBDEFB),
    onTertiaryContainer = Color.Black,
    error = Color(0xFFD32F2F), // Standard error red
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color.Black,
    background = Color(0xFFFFFFFF), // A very light, creamy yellow background
    onBackground = Color(0xFF3E2723), // Dark brown/black for text on light background
    surface = Color(0xFFFFFFFF), // White for card surfaces, dialogs etc.
    onSurface = Color(0xFF3E2723),
//    surfaceVariant = Color(0xFFFFFDE7), // Slightly off-white/pale yellow
//    surfaceVariant = Color(0xFFFFDC33), // Slightly off-white/pale yellow
    surfaceVariant = Color(0xFFFBDB65), // Slightly off-white/pale yellow
    onSurfaceVariant = Color(0xFF3E2723),
    outline = Color(0xFFBCAAA4), // A muted brown/gray for outlines
    inverseOnSurface = Color(0xFFFFF8E1),
    inverseSurface = Color(0xFF3E2723),
    inversePrimary = Color(0xFFFFA000), // Darker yellow for inverse primary
    surfaceTint = Color(0xFFFFC107),
    outlineVariant = Color(0xFFD7CCC8),
    scrim = Color.Black,
)

private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFFBB86FC), // A lighter purple for dark theme
//    onPrimary = Color.Black,
//    primaryContainer = Color(0xFF3700B3),
//    onPrimaryContainer = Color.White,
//    secondary = Color(0xFF03DAC6), // Teal remains vibrant
//    onSecondary = Color.Black,
//    secondaryContainer = Color(0xFF018786),
//    onSecondaryContainer = Color.White,
//    tertiary = Color(0xFFBB86FC),
//    onTertiary = Color.Black,
//    tertiaryContainer = Color(0xFF6200EE),
//    onTertiaryContainer = Color.White,
//    error = Color(0xFFCF6679), // A softer red for errors in dark theme
//    onError = Color.Black,
//    errorContainer = Color(0xFFB00020),
//    onErrorContainer = Color.White,
//    background = Color(0xFF121212), // Dark gray for background
//    onBackground = Color.White,
//    surface = Color(0xFF121212), // Surfaces match background in dark themes often
//    onSurface = Color.White,
//    surfaceVariant = Color(0xFF333333), // Subtle variations for surfaces
//    onSurfaceVariant = Color.White,
//    outline = Color(0xFFBDBDBD),
//    inverseOnSurface = Color.Black,
//    inverseSurface = Color.White,
//    inversePrimary = Color(0xFF6200EE),
//    surfaceTint = Color(0xFFBB86FC), // Used for elevation overlays
//    outlineVariant = Color(0xFF757575),
//    scrim = Color.Black,
    primary = Color(0xFFFFD54F), // Lighter, but still warm yellow for dark theme
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFFFA000), // Deeper gold/amber for containers
    onPrimaryContainer = Color.Black, // Or a very dark gray
    secondary = Color(0xFF81C784), // Lighter, less saturated green
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF2E7D32),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF64B5F6), // Lighter blue
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF1976D2),
    onTertiaryContainer = Color.White,
    error = Color(0xFFEF9A9A), // Softer red for errors
    onError = Color.Black,
    errorContainer = Color(0xFFD32F2F),
    onErrorContainer = Color.White,
    background = Color(0xFF212121), // Dark gray background (could go for a very dark brown too)
    onBackground = Color(0xFFFFF8E1), // Light creamy text
    surface = Color(0xFF333333), // Slightly lighter gray for surfaces
    onSurface = Color(0xFFFFF8E1),
    surfaceVariant = Color(0xFF424242), // Darker gray variant
    onSurfaceVariant = Color(0xFFFFF8E1),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF212121),
    inverseSurface = Color(0xFFFFF8E1),
    inversePrimary = Color(0xFFFFC107),
    surfaceTint = Color(0xFFFFD54F),
    outlineVariant = Color(0xFF616161),
    scrim = Color.Black,
)

/**
 * Applies the selected theme mode.
 * System theme detection should be implemented per platform if needed.
 */
@Composable
fun AppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

