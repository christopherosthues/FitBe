package org.darthacheron.fitbe.home.summary

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_summary_last_weight
import fitbe.composeapp.generated.resources.body_weight_summary_target_weight
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.stringResource

class BodyWeightSummaryUiState(
    isLoading: Boolean = true,
    error: BodyWeightSummaryError = BodyWeightSummaryError(),
    val lastWeight: Double? = null,
    val targetWeight: Double? = null,
    val weightUnit: WeightUnit = WeightUnit.KG,
) : UiState<BodyWeightSummaryError>(isLoading, error) {
    @Composable
    fun lastWeightText(): String? {
        return lastWeight?.let {
            stringResource(
                Res.string.body_weight_summary_last_weight,
                it.roundToDecimals(1),
                stringResource(weightUnit.toStringResource())
            )
        }
    }

    @Composable
    fun targetWeightText(): String? {
        return targetWeight?.let {
            stringResource(
                Res.string.body_weight_summary_target_weight,
                it.roundToDecimals(1),
                stringResource(weightUnit.toStringResource())
            )
        }
    }
}
