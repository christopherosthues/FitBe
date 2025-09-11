package org.darthacheron.fitbe.exercises.equipment

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_add_edit_training_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_new_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_loading
import fitbe.composeapp.generated.resources.training_equipment_detail_error_missing_name
import fitbe.composeapp.generated.resources.training_equipment_detail_error_not_found
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_default_exercise
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_new_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_non_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_saving
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.exercises.exercises.Exercise
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TrainingEquipmentError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null
) {
    val hasError: Boolean
        get() = hasGeneralError || hasNameError
}

@OptIn(ExperimentalUuidApi::class)
data class AddEditTrainingEquipmentUiState(
    val name: String = "",
    val imageUri: String? = null,
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val equipmentId: Uuid? = null,
    val error: TrainingEquipmentError = TrainingEquipmentError(),
    val persistedDefaultName: String? = null,
    val persistedDefaultImageUri: String? = null,
    val isModifiedFromPersistedDefault: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
)

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentDetailViewModel(
    private val equipmentRepository: EquipmentRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_add_edit_training_equipment

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true,
                    default = false,
                    equipmentId = null,
                    name = "",
                    imageUri = null,
                    error = TrainingEquipmentError(),
                    persistedDefaultName = null,
                    persistedDefaultImageUri = null,
                    isModifiedFromPersistedDefault = false,
                    exercises = emptyList(),
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val parsedEquipmentId = Uuid.parse(equipmentIdString)
                val currentEquipment =
                    equipmentRepository.getEquipmentWithExercisesById(parsedEquipmentId)
                        .firstOrNull()
                if (currentEquipment != null) {
                    if (currentEquipment.default) {
                        val originalDefaultEquipment =
                            equipmentRepository.getDefaultEquipmentById(currentEquipment.id)
                                .firstOrNull()
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = true,
                                isLoading = false,
                                isEditing = false,
                                equipmentId = currentEquipment.id,
                                error = TrainingEquipmentError(),
                                persistedDefaultName = originalDefaultEquipment?.name,
                                persistedDefaultImageUri = originalDefaultEquipment?.imageUri,
                                isModifiedFromPersistedDefault = if (originalDefaultEquipment != null) {
                                    (currentEquipment.name != originalDefaultEquipment.name || currentEquipment.imageUri != originalDefaultEquipment.imageUri)
                                } else {
                                    false
                                },
                                exercises = currentEquipment.exercises
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = false,
                                isLoading = false,
                                isEditing = false,
                                equipmentId = currentEquipment.id,
                                error = TrainingEquipmentError(),
                                persistedDefaultName = null,
                                persistedDefaultImageUri = null,
                                isModifiedFromPersistedDefault = false,
                                exercises = currentEquipment.exercises
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = TrainingEquipmentError(
                                hasGeneralError = true,
                                generalError = Res.string.training_equipment_detail_error_not_found
                            ),
                            isEditing = false,
                            persistedDefaultName = null,
                            persistedDefaultImageUri = null,
                            isModifiedFromPersistedDefault = false,
                            exercises = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_loading
                        ),
                        isEditing = false,
                        persistedDefaultName = null,
                        persistedDefaultImageUri = null,
                        isModifiedFromPersistedDefault = false,
                        exercises = emptyList()
                    )
                }
            }
        }
    }

    fun setEditing(isEditing: Boolean) {
        _uiState.update { it.copy(isEditing = isEditing) }
    }

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (name != currentState.persistedDefaultName || currentState.imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            val nameError =
                if (name.isBlank()) Res.string.training_equipment_detail_error_missing_name else null
            currentState.copy(
                name = name,
                error = currentState.error.copy(
                    hasNameError = nameError != null,
                    nameError = nameError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (currentState.name != currentState.persistedDefaultName || imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            currentState.copy(
                imageUri = imageUri,
                error = currentState.error.copy(hasGeneralError = false, generalError = null),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun saveEquipment() {
        val currentState = _uiState.value
        if (currentState.error.hasError) {
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val equipmentToSave = TrainingEquipment(
                id = currentState.equipmentId ?: Uuid.random(),
                name = currentState.name,
                imageUri = currentState.imageUri,
                default = currentState.default,
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            )

            try {
                equipmentRepository.upsertEquipment(equipmentToSave)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        equipmentId = equipmentToSave.id,
                        name = equipmentToSave.name,
                        imageUri = equipmentToSave.imageUri,
                        error = TrainingEquipmentError(),
                        isModifiedFromPersistedDefault = false // Reset modified status after save
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_saving
                        )
                    )
                }
            }
        }
    }

    fun resetEquipmentToDefault() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.equipmentId == null -> Res.string.training_equipment_detail_error_reset_new_equipment
            !currentState.default -> Res.string.training_equipment_detail_error_reset_non_default_equipment
            else -> null // No error, proceed with reset
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = TrainingEquipmentError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.equipmentId?.let { equipmentId ->
                    equipmentRepository.resetEquipmentToDefault(equipmentId)
                    loadEquipment(equipmentId.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_reset_default_exercise
                        )
                    )
                }
            }
        }
    }

    fun deleteEquipment() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.equipmentId == null -> Res.string.training_equipment_detail_error_delete_new_equipment
            currentState.default -> Res.string.training_equipment_detail_error_delete_default_equipment
            else -> null // No error, proceed with delete
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = TrainingEquipmentError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.equipmentId?.let { equipmentId ->
                    val equipmentToDelete = TrainingEquipment(
                        id = equipmentId,
                        name = currentState.name,
                        imageUri = currentState.imageUri,
                        default = currentState.default, // Should be false here based on prior checks
                        dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    )
                    equipmentRepository.deleteEquipment(equipmentToDelete)
                    navHostController.popBackStack()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_delete_equipment
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun navigateToExerciseDetail(exerciseId: Uuid) {
        navHostController.navigate(Screen.ExerciseDetail(exerciseId.toString()))
    }

    fun clearGeneralError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = currentState.error.copy(
                    hasGeneralError = false,
                    generalError = null
                )
            )
        }
    }
}
