package org.darthacheron.fitbe.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DateConverter {
//    @TypeConverter
//    fun fromDate(value: String): Long {
//        return LocalDate.parse(value).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
//    }
//
//    @TypeConverter
//    fun toDate(value: Long): String {
//        return Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.UTC).date.toString()
//    }

    @TypeConverter
    fun fromString(value: String?): Instant? {
        return value?.let { Instant.parse(it) } // Parses UTC ISO 8601
    }

    @TypeConverter
    fun instantToString(instant: Instant?): String? {
        return instant?.toString() // Converts to UTC ISO 8601 string
    }
}