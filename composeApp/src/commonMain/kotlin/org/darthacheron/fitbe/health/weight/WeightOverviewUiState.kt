package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.ui.UiState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class WeightOverviewUiState(
    isLoading: Boolean = true,
    val bodyWeights: List<BodyWeight> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val settings: Settings = Settings(),
    error: WeightOverviewError = WeightOverviewError()
) : UiState<WeightOverviewError>(isLoading, error)

