package org.darthacheron.fitbe.exercises.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.exercises.exercises.Exercise
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class AddEditTrainingEquipmentUiState(
    val name: String = "",
    val imageUri: String? = null,
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val equipmentId: Uuid? = null,
    val error: String? = null,
    val persistedDefaultName: String? = null,
    val persistedDefaultImageUri: String? = null,
    val isModifiedFromPersistedDefault: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
)

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentDetailViewModel(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    private val _saveCompletedEvent = MutableSharedFlow<Unit>()
    val saveCompletedEvent = _saveCompletedEvent.asSharedFlow()

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent = _navigateBackEvent.asSharedFlow()

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false, isEditing = false, default = false, equipmentId = null, name = "", imageUri = null, error = null,
                    persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false, exercises = emptyList(),
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val parsedEquipmentId = Uuid.parse(equipmentIdString)
                val currentEquipment = equipmentRepository.getEquipmentWithExercisesById(parsedEquipmentId).firstOrNull()
                if (currentEquipment != null) {
                    if (currentEquipment.default) {
                        // Fetch the true default values from default_training_equipment table
                        val persistedDefaultEntity = equipmentRepository.getDefaultEquipmentById(currentEquipment.id).firstOrNull()
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = true,
                                isLoading = false,
                                isEditing = true,
                                equipmentId = currentEquipment.id,
                                error = null,
                                persistedDefaultName = persistedDefaultEntity?.name,
                                persistedDefaultImageUri = persistedDefaultEntity?.imageUri,
                                isModifiedFromPersistedDefault = if (persistedDefaultEntity != null) {
                                    (currentEquipment.name != persistedDefaultEntity.name || currentEquipment.imageUri != persistedDefaultEntity.imageUri)
                                } else {
                                    false // Should not happen if item is default, but defensive
                                },
                                exercises = currentEquipment.exercises
                            )
                        }
                    } else {
                        // Not a default item
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = false,
                                isLoading = false,
                                isEditing = true,
                                equipmentId = currentEquipment.id,
                                error = null,
                                persistedDefaultName = null,
                                persistedDefaultImageUri = null,
                                isModifiedFromPersistedDefault = false,
                                exercises = currentEquipment.exercises
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Equipment not found", isEditing = false,
                                persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false,
                                exercises = emptyList())
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to load equipment: ${e.message}", isEditing = false,
                            persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false,
                            exercises = emptyList())
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (name != currentState.persistedDefaultName || currentState.imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            currentState.copy(name = name, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) { // Check persistedDefaultName as a proxy for defaults being loaded
                (currentState.name != currentState.persistedDefaultName || imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            currentState.copy(imageUri = imageUri, error = null, isModifiedFromPersistedDefault = modified)
        }
    }

    fun saveEquipment() {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }
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
                // After save, the current state is the new baseline for modification tracking if it's a default item.
                // The current name/imageUri become the new persistedDefaultName/ImageUri for this session.
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = true, 
                        equipmentId = equipmentToSave.id, 
                        name = equipmentToSave.name, 
                        imageUri = equipmentToSave.imageUri,
                        error = null
                    )
                }
                _saveCompletedEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save equipment: ${e.message}") }
            }
        }
    }

    fun resetEquipmentToDefault() {
        val currentState = _uiState.value
        if (currentState.equipmentId == null || !currentState.default) {
            _uiState.update { it.copy(error = "Cannot reset non-default equipment or new equipment.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                equipmentRepository.resetEquipmentToDefault(currentState.equipmentId)
                loadEquipment(currentState.equipmentId.toString())
                _saveCompletedEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to reset equipment: ${e.message}") }
            }
        }
    }

    fun deleteEquipment() {
        val currentState = _uiState.value
        if (currentState.equipmentId == null || currentState.default) {
            _uiState.update { it.copy(error = "Cannot delete default equipment or new equipment.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val equipmentToDelete = TrainingEquipment(
                    id = currentState.equipmentId,
                    name = currentState.name,
                    imageUri = currentState.imageUri,
                    default = currentState.default,
                    dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date 
                )
                equipmentRepository.deleteEquipment(equipmentToDelete)
                _navigateBackEvent.emit(Unit) 
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to delete equipment: ${e.message}") }
            } finally {
                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
