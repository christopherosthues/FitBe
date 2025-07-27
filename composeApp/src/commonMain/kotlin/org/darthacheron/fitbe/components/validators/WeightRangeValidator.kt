package org.darthacheron.fitbe.components.validators

import org.darthacheron.fitbe.settings.WeightUnit

class WeightRangeValidator {
    fun validate(value: Double?, weightUnit: WeightUnit): Boolean {
        return value != null &&
                when (weightUnit) {
                    WeightUnit.KG -> (value < 0.0 || value > 350.0)
                    WeightUnit.POUND -> (value < 0.0 || value > 771.618)
                }
    }
}