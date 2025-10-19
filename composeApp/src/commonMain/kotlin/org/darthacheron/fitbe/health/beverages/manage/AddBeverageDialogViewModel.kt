package org.darthacheron.fitbe.health.beverages.manage

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_invalid_amount
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_invalid_total_amount
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_name_empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.components.validators.BeverageValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.components.DialogViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AddBeverageDialogViewModel(
    private val beverageRepository: BeverageRepository,
    private val settingsRepository: SettingsRepository,
    private val beverageValidator: BeverageValidator,
    private val positiveDecimalValidator: PositiveDecimalValidator
) : DialogViewModel<AddBeverageDialogUiState>() {
    override val uiState = MutableStateFlow(AddBeverageDialogUiState())

    override fun dismissDialog() {
        uiState.update { AddBeverageDialogUiState() }
    }

    fun onAmountChange(amount: String) {
        uiState.update { it.copy(amount = amount) }
        validateAmount()
    }

    private fun validateAmount() {
        viewModelScope.launch {
            val currentState = uiState.value
            val amount = currentState.amount
            var amountInMlAsDouble = amount.replace(',', '.').toDoubleOrNull()
            val unit = currentState.selectedUnit
            amountInMlAsDouble = if (amountInMlAsDouble != null) unit.toMilliliter(amountInMlAsDouble) else null

            var error =
                if (!positiveDecimalValidator.validate(amount) ||
                    !beverageValidator.validate(amountInMlAsDouble)
                ) {
                    Res.string.beverages_add_dialog_error_invalid_amount
                } else {
                    null
                }

            if (error == null && amountInMlAsDouble != null) {
                val selectedDate = currentState.dateTime.date
                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch

                val beveragesForDay = beverageRepository.getBeverages(selectedDate, profileId).first()
                val totalAmountForDay = beveragesForDay.sumOf { it.unit.toMilliliter(it.amount) } + amountInMlAsDouble

                if (totalAmountForDay > 5000) {
                    error = Res.string.beverages_add_dialog_error_invalid_total_amount
                }
            }
            uiState.update { it.copy(amountError = error) }
        }
    }

    fun onNameChange(name: String) {
        val error =
            if (name.isBlank()) {
                Res.string.beverages_add_dialog_error_name_empty
            } else {
                null
            }
        uiState.update { it.copy(beverageName = name, beverageNameError = error) }
    }

    fun onUnitChange(unit: FluidUnit) {
        uiState.update { it.copy(selectedUnit = unit) }
        validateAmount()
    }

    fun onDateChange(date: LocalDate) {
        uiState.update { it.copy(dateTime = LocalDateTime(date, it.dateTime.time)) }
        validateAmount()
    }

    fun onTimeChange(time: LocalTime) {
        uiState.update { it.copy(dateTime = LocalDateTime(it.dateTime.date, time)) }
        validateAmount()
    }

    val allFluidUnits: List<FluidUnit> = FluidUnit.entries
}