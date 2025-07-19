package org.darthacheron.fitbe.settings

/**
 * Data class representing user settings.
 */
data class Settings(
    val weightUnit: WeightUnit = WeightUnit.KG,
    val distanceUnit: DistanceUnit = DistanceUnit.KM,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

