package org.darthacheron.fitbe.components.date.month

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object PastOrPresentSelectableMonths : SelectableMonths {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    override fun isMonthSelectable(yearMonth: YearMonth): Boolean {
        return today.year > yearMonth.year || today.year == yearMonth.year && today.monthNumber >= yearMonth.month
    }
}