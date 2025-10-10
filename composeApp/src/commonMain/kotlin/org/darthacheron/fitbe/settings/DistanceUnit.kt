package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_km
import fitbe.composeapp.generated.resources.settings_miles
import org.jetbrains.compose.resources.StringResource

private const val KM_TO_MILES = 0.621371
private const val MILES_TO_KM = 1.60934

/**
 * Enum for distance units.
 */
enum class DistanceUnit {
    KM,
    MILES;

    fun toResourceString(): StringResource =
        when (this) {
            KM -> Res.string.settings_km
            MILES -> Res.string.settings_miles
        }

    fun toKilometer(value: Double): Double =
        when (this) {
            KM -> value
            MILES -> value * MILES_TO_KM
        }

    fun toMiles(value: Double): Double =
        when (this) {
            KM -> value
            MILES -> value * KM_TO_MILES
        }
}