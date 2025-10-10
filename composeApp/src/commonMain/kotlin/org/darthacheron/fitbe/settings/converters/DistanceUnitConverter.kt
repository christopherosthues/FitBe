package org.darthacheron.fitbe.settings.converters

import org.darthacheron.fitbe.settings.DistanceUnit
import org.darthacheron.fitbe.utils.roundToDecimals

class DistanceUnitConverter {
    fun convert(
        value: Double,
        from: DistanceUnit,
        to: DistanceUnit
    ): Double =
        when (to) {
            DistanceUnit.KM -> from.toKilometer(value)
            DistanceUnit.MILES -> from.toMiles(value)
        }.roundToDecimals(1)
}