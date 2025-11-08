package org.darthacheron.fitbe.home.summary

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_summary_content_description_progress_percent_target
import fitbe.composeapp.generated.resources.sleep_summary_progress_percent
import fitbe.composeapp.generated.resources.sleep_summary_progress_total
import fitbe.composeapp.generated.resources.sleep_summary_progress_total_target
import org.darthacheron.fitbe.ui.UiState
import org.jetbrains.compose.resources.stringResource

class SleepSummaryUiState(
    isLoading: Boolean = true,
    error: SleepSummaryError = SleepSummaryError(),
    val progress: Float = 0f,
    val total: Int = 0,
    val target: Int? = null,
    val sleepHours: Int = 0,
    val sleepMinutes: Int = 0
) : UiState<SleepSummaryError>(isLoading, error) {
    private val progressInPercent = (progress * 100).toInt()

    @Composable
    fun progressText(): String {
        return if (target != null) {
            stringResource(
                Res.string.sleep_summary_progress_percent,
                progressInPercent
            )
        } else {
            stringResource(
                Res.string.sleep_summary_progress_total,
                sleepHours,
                sleepMinutes
            )
        }
    }

    @Composable
    fun totalAmountText(): String? {
        return if (target != null) {
            stringResource(
                Res.string.sleep_summary_progress_total_target,
                sleepHours,
                sleepMinutes,
                target / 60,
                target % 60
            )
        } else {
            null
        }
    }

    @Composable
    fun contentDescription(): String? {
        return if (target != null) {
            stringResource(
                Res.string.sleep_summary_content_description_progress_percent_target,
                progressInPercent
            )
        } else {
            return null
        }
    }
}
