package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class InstantConverter {
    @TypeConverter
    fun fromString(value: String?): Instant? {
        return value?.let { Instant.parse(it) } // Parses UTC ISO 8601
    }

    @TypeConverter
    fun instantToString(instant: Instant?): String? {
        return instant?.toString() // Converts to UTC ISO 8601 string
    }
}