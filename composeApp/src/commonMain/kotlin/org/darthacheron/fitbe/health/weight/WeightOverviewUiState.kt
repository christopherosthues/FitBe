package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.ui.UiState

class WeightOverviewUiState(
    isLoading: Boolean = true,
    val bodyWeights: List<BodyWeight> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    error: WeightOverviewError = WeightOverviewError()
) : UiState<WeightOverviewError>(isLoading, error)

