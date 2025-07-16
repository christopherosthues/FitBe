package org.darthacheron.fitbe.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

@OptIn(ExperimentalTime::class)
fun toDateSpan(start: Instant, end: Instant): Pair<Instant, Instant> {
    return Pair(start.toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC), end.toLocalDateTime(
        TimeZone.UTC).date.atTime(23, 59, 59, 0).toInstant(TimeZone.UTC))
}

@OptIn(ExperimentalTime::class)
fun toDateSpan(end: Instant, dateTimeUnit: DateTimeUnit.DateBased, dateInPast: Int): Pair<Instant, Instant> {
    return Pair(end.toLocalDateTime(TimeZone.UTC).date.minus(dateInPast, dateTimeUnit).atStartOfDayIn(
        TimeZone.UTC), end.toLocalDateTime(
        TimeZone.UTC).date.atTime(23, 59, 59, 0).toInstant(TimeZone.UTC))
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
object PastOrPresentSelectableDates : SelectableDates {
    private val timeZone = TimeZone.currentSystemDefault()
    private val today = Clock.System.now().toLocalDateTime(timeZone).date

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(timeZone).date
        return date <= today
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= today.year
    }
}