package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource

data class BeverageOverviewUiState(
    val isLoading: Boolean = true,
    val beverages: List<BeverageOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val errorMessage: StringResource? = null
)
