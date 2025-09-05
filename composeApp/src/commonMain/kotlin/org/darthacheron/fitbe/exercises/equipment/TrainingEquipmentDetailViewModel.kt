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
    val isEditing: Boolean = false, // True if an existing equipment is loaded
    val equipmentId: Uuid? = null,
    val error: String? = null
)

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentDetailViewModel(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    private val _saveCompletedEvent = MutableSharedFlow<Unit>()
    val saveCompletedEvent = _saveCompletedEvent.asSharedFlow()

    private val _navigateBackEvent = MutableSharedFlow<Unit>() // Kept for delete
    val navigateBackEvent = _navigateBackEvent.asSharedFlow()

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                // This is for adding a new item, isEditing (in UI state) is false
                it.copy(isLoading = false, isEditing = false, default = false, equipmentId = null, name = "", imageUri = null, error = null)
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val equipmentId = Uuid.parse(equipmentIdString)
                val equipment = equipmentRepository.getEquipmentWithExercisesById(equipmentId).firstOrNull()
                if (equipment != null) {
                    _uiState.update {
                        it.copy(
                            name = equipment.name,
                            imageUri = equipment.imageUri,
                            default = equipment.default,
                            isLoading = false,
                            isEditing = true, // Existing equipment loaded
                            equipmentId = equipment.id,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Equipment not found", isEditing = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to load equipment: ${e.message}", isEditing = false)
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, error = null) }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { it.copy(imageUri = imageUri, error = null) }
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
                        isEditing = true, // Now it's an existing item
                        equipmentId = equipmentToSave.id, // Ensure id is updated
                        name = equipmentToSave.name, // Ensure state reflects saved data
                        imageUri = equipmentToSave.imageUri,
                        default = equipmentToSave.default,
                        error = null
                    )
                }
                _saveCompletedEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save equipment: ${e.message}") }
            }
            // isLoading is set to false either in success or catch block
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
                loadEquipment(currentState.equipmentId.toString()) // Reload to reflect changes
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to reset equipment: ${e.message}") }
            }
            // isLoading will be set to false by loadEquipment or the catch block
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
                _navigateBackEvent.emit(Unit) // Deletion still navigates back
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to delete equipment: ${e.message}") }
            } finally {
                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
