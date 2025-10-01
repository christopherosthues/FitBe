package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_invalid_amount
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_invalid_total_amount
import fitbe.composeapp.generated.resources.beverages_add_dialog_error_name_empty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.validators.BeverageValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.health.componenets.AddDialogViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AddBeverageDialogViewModel(
    private val beverageRepository: BeverageRepository,
    private val settingsRepository: SettingsRepository,
    private val beverageValidator: BeverageValidator,
    private val positiveDecimalValidator: PositiveDecimalValidator,
) : AddDialogViewModel<AddBeverageDialogUiState>() {
    override val uiState = MutableStateFlow(AddBeverageDialogUiState())

    override fun dismissDialog() {
        uiState.update {
            it.copy(
                amount = "",
                beverageName = "",
                selectedUnit = FluidUnit.Milliliter,
                date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                amountError = null,
                beverageNameError = null,
            )
        }
    }

    fun onAmountChange(amount: String) {
        uiState.update { it.copy(amount = amount) }
        validateAmount()
    }

    private fun validateAmount() {
        viewModelScope.launch {
            val currentState = uiState.value
            val amount = currentState.amount
            val amountAsDouble = amount.replace(',', '.').toDoubleOrNull()

            var error = if (!positiveDecimalValidator.validate(amount) || !beverageValidator.validate(amountAsDouble)) {
                Res.string.beverages_add_dialog_error_invalid_amount
            } else {
                null
            }

            if (error == null) {
                val selectedDate = currentState.date
                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch

                val beveragesForDay = beverageRepository.getBeveragesForDate(selectedDate, profileId).first()
                val amountInMl = currentState.selectedUnit.toMilliliter(amountAsDouble!!)
                val totalAmountForDay = beveragesForDay.sumOf { it.unit.toMilliliter(it.amount) } + amountInMl

                if (totalAmountForDay > 5000) {
                    error = Res.string.beverages_add_dialog_error_invalid_total_amount
                }
            }
            uiState.update { it.copy(amountError = error) }
        }
    }

    fun onNameChange(name: String) {

        val error = if (name.isBlank()) {
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
        uiState.update { it.copy(date = date) }
        validateAmount()
    }

    val allFluidUnits: List<FluidUnit> = FluidUnit.entries
}