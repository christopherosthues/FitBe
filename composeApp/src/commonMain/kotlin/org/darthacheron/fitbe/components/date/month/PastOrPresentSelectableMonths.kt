package org.darthacheron.fitbe.components.date.month

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object PastOrPresentSelectableMonths : SelectableMonths {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    override fun isMonthSelectable(yearMonth: YearMonth): Boolean =
        today.year > yearMonth.year || today.year == yearMonth.year && today.monthNumber >= yearMonth.month
}