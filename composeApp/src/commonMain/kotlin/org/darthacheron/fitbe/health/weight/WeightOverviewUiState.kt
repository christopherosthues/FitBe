package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.jetbrains.compose.resources.StringResource

data class WeightOverviewUiState(
    val isLoading: Boolean = true,
    val bodyWeights: List<BodyWeight> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val errorMessage: StringResource? = null
)
