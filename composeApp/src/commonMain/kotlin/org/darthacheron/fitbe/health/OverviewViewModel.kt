package org.darthacheron.fitbe.health


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.minusOne
import org.darthacheron.fitbe.utils.plusOne
import kotlin.time.Duration.Companion.days

abstract class OverviewViewModel<E>(
    protected val settingsRepository: SettingsRepository,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    protected val dateRange = MutableStateFlow(
        DateRange(
            Clock.System.now().minus(6.days),
            Clock.System.now(),
            DateUnit.DAY
        )
    )

    val dateRangeFlow: StateFlow<DateRange> = dateRange

    fun movePast() {
        val range = dateRangeFlow.value.minusOne()
        setRange(range)
    }

    fun moveFuture() {
        val range = dateRangeFlow.value.plusOne()
        setRange(range)
    }

    fun setRange(startDate: Instant, endDate: Instant, dateUnit: DateUnit) {
        dateRange.value = DateRange(startDate, endDate, dateUnit)
    }

    fun setRange(range: DateRange) {
        dateRange.value = range
    }
}