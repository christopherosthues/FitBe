package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.ui.UiState

class StepsOverviewUiState(
    isLoading: Boolean = true,
    val steps: List<StepsOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val target: Int? = null,
    val maxSteps: Int = ProfileDefaults.STEPS.toInt(),
    error: StepsOverviewError = StepsOverviewError()
) : UiState<StepsOverviewError>(isLoading, error)