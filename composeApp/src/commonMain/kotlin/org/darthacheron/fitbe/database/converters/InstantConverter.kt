package org.darthacheron.fitbe.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

/**
 * Converts [Instant] to and from a string using UTC ISO 8601.
 */
@OptIn(ExperimentalTime::class)
class InstantConverter {
    @TypeConverter
    fun fromString(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun instantToString(instant: Instant?): String? {
        return instant?.toString()
    }
}