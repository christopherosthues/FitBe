package org.darthacheron.fitbe.settings.converters

import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.utils.roundToDecimals

class WeightUnitConverter {
    fun convert(value: Double, from: WeightUnit, to: WeightUnit): Double {
        return when(to) {
            WeightUnit.KG -> from.toKilogram(value)
            WeightUnit.POUND -> from.toPound(value)
        }.roundToDecimals(1)
    }
}