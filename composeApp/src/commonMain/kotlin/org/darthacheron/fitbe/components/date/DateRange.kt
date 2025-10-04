package org.darthacheron.fitbe.components.date

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

data class DateRange(
    val startDate: Instant,
    val endDate: Instant,
    val dateUnit: DateUnit,
    val timeZone: TimeZone = TimeZone.currentSystemDefault()
)
