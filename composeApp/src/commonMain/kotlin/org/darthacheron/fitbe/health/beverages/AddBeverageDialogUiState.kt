package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.componenets.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddBeverageDialogUiState(
    val date: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date,
    val amount: String = "",
    val beverageName: String = "",
    val selectedUnit: FluidUnit = FluidUnit.Milliliter,
    val amountError: StringResource? = null,
    val beverageNameError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() = amountError == null && beverageNameError == null && amount.isNotBlank() && beverageName.isNotBlank()
}