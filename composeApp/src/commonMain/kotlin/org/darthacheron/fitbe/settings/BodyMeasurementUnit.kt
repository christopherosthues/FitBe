package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_cm
import fitbe.composeapp.generated.resources.settings_inch
import org.jetbrains.compose.resources.StringResource

private const val CM_TO_INCH = 0.393701
private const val INCH_TO_CM = 2.54

enum class BodyMeasurementUnit {
    CM,
    INCH;

    fun localizedString(): StringResource {
        return when(this) {
            CM -> Res.string.settings_cm
            INCH -> Res.string.settings_inch
        }
    }

    fun toCentimeters(value: Double): Double {
        return when(this) {
            CM -> value
            INCH -> value * INCH_TO_CM
        }
    }

    fun toInch(value: Double): Double {
        return when(this) {
            CM -> value * CM_TO_INCH
            INCH -> value
        }
    }
}