package org.darthacheron.fitbe.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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