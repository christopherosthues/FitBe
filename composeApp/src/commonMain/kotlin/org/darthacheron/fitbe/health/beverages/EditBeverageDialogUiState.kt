package org.darthacheron.fitbe.health.beverages

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class EditBeverageDialogUiState(
    val id: Uuid = Uuid.NIL,
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