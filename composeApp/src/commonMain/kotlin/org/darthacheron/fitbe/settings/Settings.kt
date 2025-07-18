package org.darthacheron.fitbe.settings

/**
 * Enum for weight units.
 */
enum class WeightUnit {
    KG, POUND
}

/**
 * Enum for distance units.
 */
enum class DistanceUnit {
    KM, MILES
}

/**
 * Enum for theme selection.
 */
enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

/**
 * Data class representing user settings.
 */
data class Settings(
    val weightUnit: WeightUnit = WeightUnit.KG,
    val distanceUnit: DistanceUnit = DistanceUnit.KM,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

