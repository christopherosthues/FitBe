package org.darthacheron.fitbe.components.date.year

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object PastOrPresentSelectableYears : SelectableYears {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    override fun isYearSelectable(year: Year): Boolean {
        return today.year >= year.value
    }
}