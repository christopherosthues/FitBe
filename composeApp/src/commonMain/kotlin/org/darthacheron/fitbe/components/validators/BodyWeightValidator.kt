package org.darthacheron.fitbe.components.validators

import org.darthacheron.fitbe.settings.WeightUnit

class BodyWeightValidator {
    fun validate(
        value: Double?,
        weightUnit: WeightUnit
    ): Boolean =
        value == null ||
            when (weightUnit) {
                WeightUnit.KG -> (value >= 0.0 && value <= 600.0)
                WeightUnit.POUND -> (value >= 0.0 && value <= 1322.77)
            }
}