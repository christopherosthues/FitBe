package org.darthacheron.fitbe.components.date.year

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object PastOrPresentSelectableYears : SelectableYears {
    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    override fun isYearSelectable(year: Year): Boolean = today.year >= year.value
}