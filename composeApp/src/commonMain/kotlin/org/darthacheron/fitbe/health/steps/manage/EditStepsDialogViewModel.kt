package org.darthacheron.fitbe.health.steps.manage

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_add_dialog_error_invalid_steps
import fitbe.composeapp.generated.resources.steps_add_dialog_error_invalid_total_steps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.validators.PositiveNumberValidator
import org.darthacheron.fitbe.components.validators.StepsValidator
import org.darthacheron.fitbe.health.components.DialogViewModel
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EditStepsDialogViewModel(
    private val positiveNumberValidator: PositiveNumberValidator,
    private val stepsValidator: StepsValidator,
    private val settingsRepository: SettingsRepository,
    private val stepsRepository: StepsRepository
) : DialogViewModel<EditStepsDialogUiState>() {
    override val uiState = MutableStateFlow(EditStepsDialogUiState())

    override fun dismissDialog() {
        uiState.update { EditStepsDialogUiState() }
    }

    fun init(id: Uuid) {
        viewModelScope.launch {
            val steps = stepsRepository.getSteps(id)

            if (steps == null) {
                dismissDialog()
                return@launch
            }
            uiState.update {
                val dateTime = steps.date.toLocalDateTime(TimeZone.currentSystemDefault())
                it.copy(
                    id = id,
                    steps = steps.steps.toString(),
                    dateTime = LocalDateTime(dateTime.date, dateTime.time)
                )
            }
        }
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
                val selectedDate = currentState.dateTime
                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch

                val stepsForDate = stepsRepository.getSteps(selectedDate.date, profileId).first()
                val totalAmountForDay =
                    stepsForDate
                        .filter { it.id != currentState.id }
                        .sumOf { it.steps } + stepsAsUInt

                if (totalAmountForDay > 500_000u) {
                    error = Res.string.steps_add_dialog_error_invalid_total_steps
                }
            }
            uiState.update { it.copy(stepsError = error) }
        }
    }

    fun onDateChange(date: LocalDate) {
        uiState.update { it.copy(dateTime = LocalDateTime(date, it.dateTime.time)) }
        validateSteps()
    }

    fun onTimeChange(time: LocalTime) {
        uiState.update { it.copy(dateTime = LocalDateTime(it.dateTime.date, time)) }
        validateSteps()
    }
}