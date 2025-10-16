package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_add_dialog_error_invalid_steps
import fitbe.composeapp.generated.resources.steps_add_dialog_error_invalid_total_steps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.validators.PositiveNumberValidator
import org.darthacheron.fitbe.components.validators.StepsValidator
import org.darthacheron.fitbe.health.components.AddDialogViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AddStepsDialogViewModel(
    private val positiveNumberValidator: PositiveNumberValidator,
    private val stepsValidator: StepsValidator,
    private val settingsRepository: SettingsRepository,
    private val stepsRepository: StepsRepository
) : AddDialogViewModel<AddStepsDialogUiState>() {
    override val uiState = MutableStateFlow(AddStepsDialogUiState())

    override fun dismissDialog() {
        uiState.update { AddStepsDialogUiState() }
    }

    fun onStepsChange(steps: String) {
        uiState.update { it.copy(steps = steps) }
        validateSteps()
    }

    private fun validateSteps() {
        viewModelScope.launch {
            val currentState = uiState.value
            val steps = currentState.steps
            val stepsAsUInt = steps.replace(',', '.').toUIntOrNull()

            var error =
                if (!positiveNumberValidator.validate(steps) || !stepsValidator.validate(stepsAsUInt)) {
                    Res.string.steps_add_dialog_error_invalid_steps
                } else {
                    null
                }

            if (error == null && stepsAsUInt != null) {
                val selectedDate = currentState.date
                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch

                val stepsForDate = stepsRepository.getSteps(selectedDate, profileId).first()
                val totalAmountForDay = stepsForDate.sumOf { it.steps } + stepsAsUInt

                if (totalAmountForDay > 500_000u) {
                    error = Res.string.steps_add_dialog_error_invalid_total_steps
                }
            }
            uiState.update { it.copy(stepsError = error) }
        }
    }

    fun onDateChange(date: LocalDate) {
        uiState.update { it.copy(date = date) }
        validateSteps()
    }
}