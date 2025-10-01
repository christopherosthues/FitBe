package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_total_weight_kg
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_total_weight_lb
import fitbe.composeapp.generated.resources.steps_add_dialog_error_invalid_steps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.validators.BodyWeightValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.health.componenets.AddDialogViewModel
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter

class AddBodyWeightDialogViewModel(
    private val positiveDecimalValidator: PositiveDecimalValidator,
    private val bodyWeightValidator: BodyWeightValidator,
    private val bodyWeightRepository: BodyWeightRepository,
    weightUnitConverter: WeightUnitConverter,
    private val settingsRepository: SettingsRepository
) : AddDialogViewModel<AddBodyWeightDialogUiState>() {
    val settings: Flow<Settings> = settingsRepository.getSettingsFlow()

    override val uiState = MutableStateFlow(AddBodyWeightDialogUiState())

    override fun dismissDialog() {
        uiState.update { it.copy(
            weight = "",
            bodyFatPercentage = "",
            muscleMass = "",
            boneMass = "",
            bodyWaterInPercentage = "",
            date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            weightError = null,
            bodyFatError = null,
            muscleMassError = null,
            boneMassError = null,
            bodyWaterError = null
        ) }
    }

    fun onDateChange(date: LocalDate) {
        uiState.update { it.copy(date = date) }
    }

    fun onWeightChange(weight: String) {
        uiState.update { it.copy(weight = weight) }
        validateWeight()
        validateBodyWeight()
    }

    private fun validateWeight() {
        viewModelScope.launch {
            val currentState = uiState.value
            val weight = currentState.weight
            val settings = settings.first()
            val weightUnit = settings.weightUnit

            val weightAsDouble = weight.replace(',', '.').toDoubleOrNull()
            val error = if (!positiveDecimalValidator.validate(weight) || !bodyWeightValidator.validate(weightAsDouble, weightUnit)) {
                when (settings.weightUnit) {
                    WeightUnit.KG -> Res.string.body_weight_add_dialog_error_total_weight_kg
                    WeightUnit.POUND -> Res.string.body_weight_add_dialog_error_total_weight_lb
                }
            } else {
                null
            }
            uiState.update { it.copy(weightError = error) }
        }
    }

    fun onBodyFatChange(bodyFat: String) {
        uiState.update { it.copy(bodyFatPercentage = bodyFat) }
        // Res.string.body_weight_add_dialog_error_body_fat
        validateBodyWeight()
    }

    fun onMuscleMassChange(muscleMass: String) {
        uiState.update { it.copy(muscleMass = muscleMass) }
//        when (settings.weightUnit) {
//            WeightUnit.KG -> Res.string.body_weight_add_dialog_error_muscle_mass_kg
//            WeightUnit.POUND -> Res.string.body_weight_add_dialog_error_muscle_mass_lb
//        }
        validateBodyWeight()
    }

    fun onBoneMassChange(boneMass: String) {
        uiState.update { it.copy(boneMass = boneMass) }
//        when (settings.weightUnit) {
//            WeightUnit.KG -> Res.string.body_weight_add_dialog_error_bone_mass_kg
//            WeightUnit.POUND -> Res.string.body_weight_add_dialog_error_bone_mass_lb
//        }
        validateBodyWeight()
    }

    fun onBodyWaterChange(bodyWater: String) {
        uiState.update { it.copy(bodyWaterInPercentage = bodyWater) }
//        Res.string.body_weight_add_dialog_error_body_water
        validateBodyWeight()
    }

    private fun validateBodyWeight() {
        viewModelScope.launch {
            // sum: body_weight_add_dialog_error_total_weight_sum

            val currentState = uiState.value
//            val sum =
//                currentState.bodyFatPercentage.toDoubleOrNull() +
//                        currentState.muscleMass.toDoubleOrNull() +
//                        currentState.boneMass.toDoubleOrNull()
//            val steps = currentState.steps
//            val stepsAsUInt = steps.replace(',', '.').toUIntOrNull()
//
//            var error = if (!positiveNumberValidator.validate(steps) || !stepsValidator.validate(stepsAsUInt)) {
//                Res.string.steps_add_dialog_error_invalid_steps
//            } else {
//                null
//            }
//
//            if (error == null) {
//                val selectedDate = currentState.date
//                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch
//
//                val stepsForDate = stepsRepository.getStepsForDate(selectedDate, profileId).first()
//                val totalAmountForDay = stepsForDate.sumOf { it.steps } + stepsAsUInt!!
//
//                if (totalAmountForDay > 500_000u) {
//                    error = Res.string.steps_add_dialog_error_invalid_total_steps
//                }
//            }
//            uiState.update { it.copy(stepsError = error) }
        }
    }
}