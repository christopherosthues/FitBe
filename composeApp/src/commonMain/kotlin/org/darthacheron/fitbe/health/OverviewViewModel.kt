package org.darthacheron.fitbe.health


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.darthacheron.fitbe.utils.minusOne
import org.darthacheron.fitbe.utils.plusOne
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.days

abstract class OverviewViewModel<Error : UiStateError, State : UiState<Error>>(
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

    protected val isLoading = MutableStateFlow(true)
    protected val errorMessage = MutableStateFlow<StringResource?>(null)

    abstract val uiState: StateFlow<State>

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

    fun clearErrorMessage() {
        errorMessage.value = null
    }
}