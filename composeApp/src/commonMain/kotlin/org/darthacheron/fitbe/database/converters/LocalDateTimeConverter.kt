package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

/**
 * Converts [LocalDateTime] to and from a String in ISO-8601 format, e.g., "2025-07-17T15:30".
 */
class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? =
        value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}