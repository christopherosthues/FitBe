package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_cm
import fitbe.composeapp.generated.resources.settings_inch
import org.jetbrains.compose.resources.StringResource

enum class BodyMeasurementUnit {
    CM,
    INCH;

    fun localizedString(): StringResource {
        return when(this) {
            CM -> Res.string.settings_cm
            INCH -> Res.string.settings_inch
        }
    }
}