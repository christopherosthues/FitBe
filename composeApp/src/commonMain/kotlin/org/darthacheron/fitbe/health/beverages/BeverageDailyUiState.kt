package org.darthacheron.fitbe.health.beverages

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_progress_percent_target
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_percent
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total_target
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.stringResource

class BeverageDailyUiState(
    isLoading: Boolean = true,
    error: BeverageDailyError = BeverageDailyError(),
    val beverages: List<Beverage> = emptyList(),
    val progress: Double = 0.0,
    val total: Double = 0.0,
    val target: Int? = null,
) : UiState<BeverageDailyError>(isLoading = isLoading, error = error) {
    val progressInPercent = (progress * 100).toInt()

    @Composable
    fun progressText(): String {
        return if (target != null) {
            stringResource(
                Res.string.beverages_daily_view_progress_percent,
                progressInPercent
            )
        } else {
            stringResource(
                Res.string.beverages_daily_view_progress_total,
                total.toFloat()
            )
        }
    }

    @Composable
    fun totalAmountText(): String? {
        return if (target != null) {
            stringResource(
                Res.string.beverages_daily_view_progress_total_target,
                total.roundToDecimals(2),
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
                Res.string.beverages_daily_view_content_description_progress_percent_target,
                progressInPercent
            )
        } else {
            return null
        }
    }
}