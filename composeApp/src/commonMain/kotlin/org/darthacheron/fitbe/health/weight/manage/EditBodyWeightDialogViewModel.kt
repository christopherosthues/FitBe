package org.darthacheron.fitbe.health.weight.manage

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_body_fat
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_body_water
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_bone_mass_kg
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_bone_mass_lb
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_muscle_mass_kg
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_muscle_mass_lb
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_total_weight_kg
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_total_weight_lb
import fitbe.composeapp.generated.resources.body_weight_add_dialog_error_total_weight_sum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.validators.BodyWeightValidator
import org.darthacheron.fitbe.components.validators.PercentageValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.health.components.DialogViewModel
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EditBodyWeightDialogViewModel(
    private val positiveDecimalValidator: PositiveDecimalValidator,
    private val bodyWeightValidator: BodyWeightValidator,
    private val percentageValidator: PercentageValidator,
    settingsRepository: SettingsRepository,
    private val bodyWeightRepository: BodyWeightRepository
) : DialogViewModel<EditBodyWeightDialogUiState>() {
    val settings: Flow<Settings> = settingsRepository.getSettingsFlow()

    override val uiState = MutableStateFlow(EditBodyWeightDialogUiState())

    override fun dismissDialog() {
        uiState.update { EditBodyWeightDialogUiState() }
    }

    fun init(id: Uuid) {
        viewModelScope.launch {
            val bodyWeight = bodyWeightRepository.getBodyWeight(id)

            if (bodyWeight == null) {
                dismissDialog()
                return@launch
            }
            val settings = settings.first()
            val weightUnit = settings.weightUnit

            uiState.update {
                val dateTime = bodyWeight.date.toLocalDateTime(TimeZone.currentSystemDefault())

                val weight =
                    if (weightUnit == WeightUnit.POUND){
                        weightUnit.toPound(bodyWeight.weightInKg)
                    } else {
                        bodyWeight.weightInKg
                    }
                val muscleMass =
                    if (weightUnit == WeightUnit.POUND && bodyWeight.muscleMassInKg != null) {
                        weightUnit.toPound(bodyWeight.muscleMassInKg)
                    } else {
                        bodyWeight.muscleMassInKg
                    }
                val boneMass =
                    if (weightUnit == WeightUnit.POUND && bodyWeight.boneMassInKg != null) {
                        weightUnit.toPound(bodyWeight.boneMassInKg)
                    } else {
                        bodyWeight.boneMassInKg
                    }


                it.copy(
                    id = id,
                    weight = weight.toString(),
                    muscleMass = muscleMass.toString(),
                    boneMass = boneMass.toString(),
                    bodyFatInPercentage = bodyWeight.bodyFatPercentage.toString(),
                    bodyWaterInPercentage = bodyWeight.bodyWaterInPercentage.toString(),
                    dateTime = LocalDateTime(dateTime.date, dateTime.time)
                )
            }
        }
    }

    fun onDateChange(date: LocalDate) {
        uiState.update { it.copy(dateTime = LocalDateTime(date, it.dateTime.time)) }
        validateBodyWeight()
    }

    fun onTimeChange(time: LocalTime) {
        uiState.update { it.copy(dateTime = LocalDateTime(it.dateTime.date, time)) }
        validateBodyWeight()
    }

    fun onWeightChange(weight: String) {
        uiState.update { it.copy(weight = weight) }
        validateWeight()
        validateBodyWeight()
    }

    private fun validateWeight() {
        // TODO: check if this is working correctly
        viewModelScope.launch {
            val currentState = uiState.value
            val weight = currentState.weight
            val settings = settings.first()
            val weightUnit = settings.weightUnit

            val weightAsDouble = weight.replace(',', '.').toDoubleOrNull()
            val error =
                if (!positiveDecimalValidator.validate(weight) ||
                    !bodyWeightValidator.validate(weightAsDouble, weightUnit)
                ) {
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
        uiState.update { it.copy(bodyFatInPercentage = bodyFat) }

        validateBodyFat(bodyFat)
        validateBodyWeight()
    }

    private fun validateBodyFat(bodyFat: String) {
        val bodyFatAsDouble = bodyFat.replace(',', '.').toDoubleOrNull()
        val error =
            if (!positiveDecimalValidator.validate(bodyFat) || !percentageValidator.validate(bodyFatAsDouble)) {
                Res.string.body_weight_add_dialog_error_body_fat
            } else {
                null
            }

        uiState.update { it.copy(bodyFatError = error) }
    }

    fun onMuscleMassChange(muscleMass: String) {
        uiState.update { it.copy(muscleMass = muscleMass) }

        validateMuscleMass(muscleMass)
        validateBodyWeight()
    }

    private fun validateMuscleMass(muscleMass: String) {
        viewModelScope.launch {
            val settings = settings.first()
            val weightUnit = settings.weightUnit

            val muscleMassAsDouble = muscleMass.replace(',', '.').toDoubleOrNull()
            val error =
                if (!positiveDecimalValidator.validate(muscleMass) ||
                    !bodyWeightValidator.validate(muscleMassAsDouble, weightUnit)
                ) {
                    when (settings.weightUnit) {
                        WeightUnit.KG -> Res.string.body_weight_add_dialog_error_muscle_mass_kg
                        WeightUnit.POUND -> Res.string.body_weight_add_dialog_error_muscle_mass_lb
                    }
                } else {
                    null
                }
            uiState.update { it.copy(muscleMassError = error) }
        }
    }

    fun onBoneMassChange(boneMass: String) {
        uiState.update { it.copy(boneMass = boneMass) }

        validateBoneMass(boneMass)
        validateBodyWeight()
    }

    private fun validateBoneMass(boneMass: String) {
        viewModelScope.launch {
            val settings = settings.first()
            val weightUnit = settings.weightUnit

            val boneMassAsDouble = boneMass.replace(',', '.').toDoubleOrNull()
            val error =
                if (!positiveDecimalValidator.validate(boneMass) ||
                    !bodyWeightValidator.validate(boneMassAsDouble, weightUnit)
                ) {
                    when (settings.weightUnit) {
                        WeightUnit.KG -> Res.string.body_weight_add_dialog_error_bone_mass_kg
                        WeightUnit.POUND -> Res.string.body_weight_add_dialog_error_bone_mass_lb
                    }
                } else {
                    null
                }
            uiState.update { it.copy(boneMassError = error) }
        }
    }

    fun onBodyWaterChange(bodyWater: String) {
        uiState.update { it.copy(bodyWaterInPercentage = bodyWater) }
        validateBodyWater(bodyWater)
        validateBodyWeight()
    }

    private fun validateBodyWater(bodyWater: String) {
        val bodyWaterAsDouble = bodyWater.replace(',', '.').toDoubleOrNull()
        val error =
            if (!positiveDecimalValidator.validate(bodyWater) || !percentageValidator.validate(bodyWaterAsDouble)) {
                Res.string.body_weight_add_dialog_error_body_water
            } else {
                null
            }

        uiState.update { it.copy(bodyWaterError = error) }
    }

    private fun validateBodyWeight() {
        viewModelScope.launch {
            val currentState = uiState.value

            if (currentState.weightError != null) {
                return@launch
            }

            val totalWeight = currentState.weight.replace(',', '.').toDoubleOrNull()

            // We can only validate the sum if the total weight is a valid positive number.
            // `validateWeight()` is responsible for flagging errors on the weight field itself.
            if (totalWeight == null || totalWeight <= 0.0) {
                return@launch
            }

            // Safely parse component values, defaulting to 0.0 if empty or invalid.
            val bodyFatPercentage = currentState.bodyFatInPercentage.replace(',', '.').toDoubleOrNull() ?: 0.0
            val muscleMass = currentState.muscleMass.replace(',', '.').toDoubleOrNull() ?: 0.0
            val boneMass = currentState.boneMass.replace(',', '.').toDoubleOrNull() ?: 0.0
            val bodyWaterPercentage = currentState.bodyWaterInPercentage.replace(',', '.').toDoubleOrNull() ?: 0.0

            // Calculate fat mass from the percentage of total weight.
            val fatMass = (bodyFatPercentage / 100.0) * totalWeight
            val bodyWaterMass = (bodyWaterPercentage / 100.0) * totalWeight

            // Sum of the component masses. Body water is a sub-component of the others,
            // so it's not included in this top-level sum check.
            val componentSum = fatMass + muscleMass + boneMass + bodyWaterMass

            val error =
                if (componentSum > totalWeight) {
                    Res.string.body_weight_add_dialog_error_total_weight_sum
                } else {
                    null
                }

            // Update the error state for all component fields involved in the sum.
            // This provides clear feedback to the user about which values are part of the issue.
            uiState.update {
                it.copy(
                    weightError = error
                )
            }
        }
    }
}