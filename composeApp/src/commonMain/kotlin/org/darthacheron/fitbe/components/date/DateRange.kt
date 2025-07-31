package org.darthacheron.fitbe.components.date

import kotlinx.datetime.Instant
import org.darthacheron.fitbe.components.DateUnit

data class DateRange(val startDate: Instant, val endDate: Instant, val dateUnit: DateUnit)
