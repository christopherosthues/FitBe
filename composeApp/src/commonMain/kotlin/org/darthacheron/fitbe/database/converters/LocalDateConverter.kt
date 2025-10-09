package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.ExperimentalTime

/**
 * Room type converter for kotlinx.datetime.LocalTime
 * Stores LocalTime as String in format HH:MM:SS
 */
@OptIn(ExperimentalTime::class)
class LocalDateConverter {
    @TypeConverter
    fun fromLocalTime(time: LocalDate): String = time.toString()

    @TypeConverter
    fun toLocalTime(timeString: String): LocalDate = LocalDate.parse(timeString)
}