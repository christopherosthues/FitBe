package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.ui.UiState

class BeverageOverviewUiState(
    isLoading: Boolean = true,
    val beverages: List<BeverageOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val target: Int? = null,
    val maxBeverages: Int = ProfileDefaults.BEVERAGE.toInt(),
    error: BeverageOverviewError = BeverageOverviewError()
) : UiState<BeverageOverviewError>(isLoading, error)