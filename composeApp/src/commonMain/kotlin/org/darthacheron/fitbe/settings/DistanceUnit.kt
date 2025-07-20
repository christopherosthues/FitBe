package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_km
import fitbe.composeapp.generated.resources.settings_miles
import org.jetbrains.compose.resources.StringResource

/**
 * Enum for distance units.
 */
enum class DistanceUnit {
    KM, MILES;

    fun localizedString(): StringResource {
        return when(this) {
            KM -> Res.string.settings_km
            MILES -> Res.string.settings_miles
        }
    }
}