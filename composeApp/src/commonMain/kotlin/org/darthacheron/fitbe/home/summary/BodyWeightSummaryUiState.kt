package org.darthacheron.fitbe.home.summary

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.stringResource

class BodyWeightSummaryUiState(
    isLoading: Boolean = true,
    error: BodyWeightSummaryError = BodyWeightSummaryError(),
    val totalWeight: Double? = null,
    val targetWeight: Double? = null,
    val muscleMass: Double? = null,
    val boneMass: Double? = null,
    val bodyFatPercentage: Double? = null,
    val bodyWaterPercentage: Double? = null,
    val weightUnit: WeightUnit = WeightUnit.KG,
) : UiState<BodyWeightSummaryError>(isLoading, error) {

    @Composable
    fun totalWeightText(): String {
        return totalWeight?.let {
//            stringResource(
//                Res.string.body_weight_summary_last_weight,
                it.roundToDecimals(1).toString() + " " +
                stringResource(weightUnit.toStringResource())
//            )
        } ?: "-"
    }

    @Composable
    fun targetWeightText(): String? {
        return targetWeight?.let {
//            stringResource(
//                Res.string.body_weight_summary_target_weight,
                it.roundToDecimals(1).toString() + " " +
                stringResource(weightUnit.toStringResource())
//            )
        }
    }

    @Composable
    fun muscleMassText(): String? {
        return muscleMass?.let {
//            stringResource(
//                Res.string.body_weight_summary_muscle_mass,
                it.roundToDecimals(1).toString() + " " +
                stringResource(weightUnit.toStringResource())
//            )
        }
    }

    @Composable
    fun boneMassText(): String? {
        return boneMass?.let {
//            stringResource(
//                Res.string.body_weight_summary_bone_mass,
                it.roundToDecimals(1).toString() + " " +
                stringResource(weightUnit.toStringResource())
//            )
        }
    }

    @Composable
    fun bodyFatPercentageText(): String? {
        return bodyFatPercentage?.let {
//            stringResource(
//                Res.string.body_weight_summary_body_fat,
                it.roundToDecimals(1).toString()
//            )
        }
    }

    @Composable
    fun bodyWaterPercentageText(): String? {
        return bodyWaterPercentage?.let {
//            stringResource(
//                Res.string.body_weight_summary_body_water,
                it.roundToDecimals(1).toString()
//            )
        }
    }
}