package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.ui.UiState

class StepsOverviewUiState(
    isLoading: Boolean = true,
    val steps: List<Steps> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    error: StepsOverviewError = StepsOverviewError()
) : UiState<StepsOverviewError>(isLoading, error)
