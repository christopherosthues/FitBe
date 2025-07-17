package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString() // ISO-8601 format, e.g., "2025-07-17T15:30"
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
}
