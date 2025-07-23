package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_kg
import fitbe.composeapp.generated.resources.settings_pound
import org.jetbrains.compose.resources.StringResource

private const val KG_TO_POUND = 2.20462
private const val POUND_TO_KG = 0.453592

/**
 * Enum for weight units.
 */
enum class WeightUnit {
    KG, POUND;

    fun localizedString(): StringResource {
        return when(this) {
            KG -> Res.string.settings_kg
            POUND -> Res.string.settings_pound
        }
    }

    fun toKilogram(value: Double): Double {
        return when(this) {
            KG -> value
            POUND -> value * POUND_TO_KG
        }
    }

    fun toPound(value: Double): Double {
        return when(this) {
            KG -> value * KG_TO_POUND
            POUND -> value
        }
    }
}