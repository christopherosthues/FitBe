package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_theme_dark
import fitbe.composeapp.generated.resources.settings_theme_light
import fitbe.composeapp.generated.resources.settings_theme_system
import org.jetbrains.compose.resources.StringResource

/**
 * Enum for theme selection.
 */
enum class ThemeMode {
    LIGHT, DARK, SYSTEM;

    fun toStringResource(): StringResource {
        return when(this) {
            LIGHT -> Res.string.settings_theme_light
            DARK -> Res.string.settings_theme_dark
            SYSTEM -> Res.string.settings_theme_system
        }
    }
}