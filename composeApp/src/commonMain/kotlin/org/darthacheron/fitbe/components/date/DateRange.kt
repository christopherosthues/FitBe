package org.darthacheron.fitbe.components.date

import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DateRange(
    val startDate: Instant,
    val endDate: Instant,
    val dateUnit: DateUnit,
    val timeZone: TimeZone = TimeZone.currentSystemDefault()
)