package org.darthacheron.fitbe.settings

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_kg
import fitbe.composeapp.generated.resources.settings_pound
import org.jetbrains.compose.resources.StringResource

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
}