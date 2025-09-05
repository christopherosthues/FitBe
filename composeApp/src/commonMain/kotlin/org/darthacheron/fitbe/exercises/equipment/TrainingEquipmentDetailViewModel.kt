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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class AddEditTrainingEquipmentUiState(
    val name: String = "",
    val imageUri: String? = null,
    val default: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false, // True if an existing equipment is loaded from training_equipment table
    val equipmentId: Uuid? = null,
    val error: String? = null,
    // Fields for comparing with the persisted default_training_equipment entity
    val persistedDefaultName: String? = null,
    val persistedDefaultImageUri: String? = null,
    val isModifiedFromPersistedDefault: Boolean = false
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

    private fun compareWithPersistedDefault(equipmentId: Uuid, currentName: String, currentImageUri: String?) {
        viewModelScope.launch {
            val persistedDefaultEquipment = equipmentRepository.getDefaultEquipmentById(equipmentId).firstOrNull()
            if (persistedDefaultEquipment != null) {
                _uiState.update {
                    it.copy(
                        persistedDefaultName = persistedDefaultEquipment.name,
                        persistedDefaultImageUri = persistedDefaultEquipment.imageUri,
                        isModifiedFromPersistedDefault = (currentName != persistedDefaultEquipment.name || currentImageUri != persistedDefaultEquipment.imageUri)
                    )
                }
            } else {
                 // Should not happen if equipment.default was true for this ID, but handle defensively
                _uiState.update {
                    it.copy(persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false)
                }
            }
        }
    }

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false, isEditing = false, default = false, equipmentId = null, name = "", imageUri = null, error = null,
                    persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false
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
                    _uiState.update {
                        it.copy(
                            name = currentEquipment.name,
                            imageUri = currentEquipment.imageUri,
                            default = currentEquipment.default,
                            isLoading = false,
                            isEditing = true,
                            equipmentId = currentEquipment.id,
                            error = null
                        )
                    }
                    if (currentEquipment.default) {
                        compareWithPersistedDefault(currentEquipment.id, currentEquipment.name, currentEquipment.imageUri)
                    } else {
                        _uiState.update { it.copy(persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false) }
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Equipment not found", isEditing = false,
                                persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to load equipment: ${e.message}", isEditing = false,
                            persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false)
                }
            }
        }
    }

    fun onNameChange(name: String) {
        val currentId = _uiState.value.equipmentId
        val isDefaultItem = _uiState.value.default
        val currentImageUri = _uiState.value.imageUri

        _uiState.update { it.copy(name = name, error = null) }

        if (isDefaultItem && currentId != null) {
            compareWithPersistedDefault(currentId, name, currentImageUri)
        }
    }

    fun onImageUriChange(imageUri: String?) {
        val currentId = _uiState.value.equipmentId
        val isDefaultItem = _uiState.value.default
        val currentName = _uiState.value.name

        _uiState.update { it.copy(imageUri = imageUri, error = null) }

        if (isDefaultItem && currentId != null) {
            compareWithPersistedDefault(currentId, currentName, imageUri)
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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = true, 
                        equipmentId = equipmentToSave.id, 
                        name = equipmentToSave.name, 
                        imageUri = equipmentToSave.imageUri,
                        default = equipmentToSave.default,
                        error = null
                        // isModifiedFromPersistedDefault will be updated by compareWithPersistedDefault below
                    )
                }
                if (equipmentToSave.default) {
                    compareWithPersistedDefault(equipmentToSave.id, equipmentToSave.name, equipmentToSave.imageUri)
                } else {
                     _uiState.update { it.copy(persistedDefaultName = null, persistedDefaultImageUri = null, isModifiedFromPersistedDefault = false) }
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
                loadEquipment(currentState.equipmentId.toString()) // This will reload and re-compare
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
