package org.darthacheron.fitbe.components.date.week

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import org.darthacheron.fitbe.utils.isoWeekAndYear
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object PastOrPresentSelectableWeeks : SelectableWeeks {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    override fun isWeekSelectable(yearWeek: YearWeek): Boolean {
        val isoWeekAndYear = today.date.isoWeekAndYear()
        return isoWeekAndYear.first > yearWeek.year ||
            isoWeekAndYear.first == yearWeek.year &&
            isoWeekAndYear.second >= yearWeek.week
    }
}