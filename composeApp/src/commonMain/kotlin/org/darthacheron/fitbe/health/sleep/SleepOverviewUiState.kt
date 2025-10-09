package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.ui.UiState

class SleepOverviewUiState(
    isLoading: Boolean = true,
    val sleeps: List<SleepOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    error: SleepOverviewError = SleepOverviewError()
) : UiState<SleepOverviewError>(isLoading, error)