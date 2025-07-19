package org.darthacheron.fitbe.settings

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Data class representing user settings.
 */
@OptIn(ExperimentalUuidApi::class)
data class Settings(
    val weightUnit: WeightUnit = WeightUnit.KG,
    val distanceUnit: DistanceUnit = DistanceUnit.KM,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val selectedProfileId: Uuid? = null
)

