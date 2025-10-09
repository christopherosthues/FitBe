package org.darthacheron.fitbe.health.beverages

import org.darthacheron.fitbe.ui.UiState

class BeverageDailyUiState(
    isLoading: Boolean = true,
    error: BeverageDailyError = BeverageDailyError(),
    val beverages: List<Beverage> = emptyList(),
    val progress: Double = 0.0
) : UiState<BeverageDailyError>(isLoading, error)