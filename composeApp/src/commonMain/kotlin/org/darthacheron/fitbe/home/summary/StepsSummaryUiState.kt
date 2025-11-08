package org.darthacheron.fitbe.home.summary

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_summary_content_description_progress_percent_target
import fitbe.composeapp.generated.resources.steps_summary_progress_percent
import fitbe.composeapp.generated.resources.steps_summary_progress_total
import fitbe.composeapp.generated.resources.steps_summary_progress_total_target
import org.darthacheron.fitbe.ui.UiState
import org.jetbrains.compose.resources.stringResource

class StepsSummaryUiState(
    isLoading: Boolean = true,
    error: StepsSummaryError = StepsSummaryError(),
    val progress: Float = 0f,
    val total: Int = 0,
    val target: Int? = null,
) : UiState<StepsSummaryError>(isLoading, error) {
    private val progressInPercent = (progress * 100).toInt()

    @Composable
    fun progressText(): String {
        return if (target != null) {
            stringResource(
                Res.string.steps_summary_progress_percent,
                progressInPercent
            )
        } else {
            stringResource(
                Res.string.steps_summary_progress_total,
                total
            )
        }
    }

    @Composable
    fun totalAmountText(): String? {
        return if (target != null) {
            stringResource(
                Res.string.steps_summary_progress_total_target,
                total,
                target
            )
        } else {
            null
        }
    }

    @Composable
    fun contentDescription(): String? {
        return if (target != null) {
            stringResource(
                Res.string.steps_summary_content_description_progress_percent_target,
                progressInPercent
            )
        } else {
            return null
        }
    }
}
