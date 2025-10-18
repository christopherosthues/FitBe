package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.ui.UiState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class BodyWeightOverviewUiState(
    isLoading: Boolean = true,
    val bodyWeights: List<BodyWeightOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val weightUnit: WeightUnit = WeightUnit.KG,
    val maxBodyWeight: Double = ProfileDefaults.MAX_BODY_WEIGHT,
    val targetBodyWeight: Double? = null,
    error: BodyWeightOverviewError = BodyWeightOverviewError()
) : UiState<BodyWeightOverviewError>(isLoading, error)