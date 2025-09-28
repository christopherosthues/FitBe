package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource

data class BeverageDialogUiState(
    val selectedDateForDialog: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.Companion.UTC).date,
    val amount: String = "",
    val beverageName: String = "",
    val selectedUnit: FluidUnit = FluidUnit.Milliliter,
    val amountError: StringResource? = null,
    val beverageNameError: StringResource? = null,
) {
    val canSave: Boolean
        get() = amountError == null && beverageNameError == null && amount.isNotBlank() && beverageName.isNotBlank()
}