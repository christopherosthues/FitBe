package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import org.darthacheron.fitbe.health.beverages.FluidUnit

class FluidUnitConverter {
    @TypeConverter
    fun fromFluidUnit(value: FluidUnit): String = value.name

    @TypeConverter
    fun toFluidUnit(value: String): FluidUnit = FluidUnit.valueOf(value)
}