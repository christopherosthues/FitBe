package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource

data class BeverageOverviewUiState(
    val isLoading: Boolean = true,
    val beverages: List<BeverageOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val errorMessage: StringResource? = null,
    // New properties for the dialog
    val showAddBeverageDialog: Boolean = false,
    val selectedDateForDialog: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    val dialogAmount: String = "",
    val dialogBeverageName: String = "",
    val dialogSelectedUnit: FluidUnit = FluidUnit.Milliliter, // Default unit
    val dialogAmountError: StringResource? = null,
    val dialogBeverageNameError: StringResource? = null
)
