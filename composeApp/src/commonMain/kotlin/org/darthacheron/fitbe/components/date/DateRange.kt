package org.darthacheron.fitbe.components.date

import kotlinx.datetime.Instant

data class DateRange(val startDate: Instant, val endDate: Instant, val dateUnit: DateUnit)
