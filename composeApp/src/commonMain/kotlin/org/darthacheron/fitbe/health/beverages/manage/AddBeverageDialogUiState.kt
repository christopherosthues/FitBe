package org.darthacheron.fitbe.health.beverages.manage

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddBeverageDialogUiState(
    val dateTime: LocalDateTime =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    val amount: String = "",
    val beverageName: String = "",
    val selectedUnit: FluidUnit = FluidUnit.Milliliter,
    val amountError: StringResource? = null,
    val beverageNameError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() = amountError == null && beverageNameError == null && amount.isNotBlank() && beverageName.isNotBlank()
}