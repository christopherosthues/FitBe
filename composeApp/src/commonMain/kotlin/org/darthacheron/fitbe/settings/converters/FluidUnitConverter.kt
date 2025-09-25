package org.darthacheron.fitbe.settings.converters

import org.darthacheron.fitbe.health.beverages.FluidUnit

class FluidUnitConverter {
    fun toMilliliter(amount: UInt, from: FluidUnit): UInt {
        return from.toMilliliter(amount)
    }
}
