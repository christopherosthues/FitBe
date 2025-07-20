package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime
import kotlin.time.ExperimentalTime

/**
 * Room type converter for kotlinx.datetime.LocalTime
 * Stores LocalTime as String in format HH:MM:SS
 */
@OptIn(ExperimentalTime::class)
class LocalTimeConverter {
    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime {
        return LocalTime.parse(timeString)
    }
}

