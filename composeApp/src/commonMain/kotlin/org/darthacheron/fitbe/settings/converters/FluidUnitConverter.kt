package org.darthacheron.fitbe.settings.converters

import org.darthacheron.fitbe.health.beverages.FluidUnit

class FluidUnitConverter {
    fun toMilliliter(
        amount: Double,
        from: FluidUnit
    ): Double = from.toMilliliter(amount)
}