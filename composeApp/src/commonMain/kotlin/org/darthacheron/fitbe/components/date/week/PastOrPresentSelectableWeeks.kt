package org.darthacheron.fitbe.components.date.week

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.isoWeekAndYear

object PastOrPresentSelectableWeeks : SelectableWeeks {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    override fun isWeekSelectable(yearWeek: YearWeek): Boolean {
        val isoWeekAndYear = today.date.isoWeekAndYear()
        return isoWeekAndYear.first > yearWeek.year || isoWeekAndYear.first == yearWeek.year && isoWeekAndYear.second >= yearWeek.week
    }
}