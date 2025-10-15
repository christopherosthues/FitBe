package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.ui.UiState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class BodyWeightDailyUiState(
    isLoading: Boolean = true,
    error: BodyWeightDailyError = BodyWeightDailyError(),
    val bodyWeights: List<BodyWeight> = emptyList(),
    val times: List<LocalTime> = emptyList(),
    val target: Double? = null,
    val weightUnit: WeightUnit = WeightUnit.KG,
) : UiState<BodyWeightDailyError>(isLoading, error)