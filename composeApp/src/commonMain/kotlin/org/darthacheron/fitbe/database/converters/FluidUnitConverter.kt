package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.health.beverages.FluidUnit

class FluidUnitConverter {
    @TypeConverter
    fun fromFluidUnit(value: FluidUnit): String {
        return value.toString()
    }

    @TypeConverter
    fun toFluidUnit(value: String): FluidUnit {
        return FluidUnit.valueOf(value)
    }
}