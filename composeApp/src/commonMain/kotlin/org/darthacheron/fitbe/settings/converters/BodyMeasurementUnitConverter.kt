package org.darthacheron.fitbe.settings.converters

import org.darthacheron.fitbe.settings.BodyMeasurementUnit
import org.darthacheron.fitbe.utils.roundToDecimals

class BodyMeasurementUnitConverter {
    fun convert(
        value: Double?,
        from: BodyMeasurementUnit,
        to: BodyMeasurementUnit
    ): Double? =
        if (value == null) {
            null
        } else {
            when (to) {
                BodyMeasurementUnit.CM -> from.toCentimeters(value)
                BodyMeasurementUnit.INCH -> from.toInch(value)
            }.roundToDecimals(1)
        }
}