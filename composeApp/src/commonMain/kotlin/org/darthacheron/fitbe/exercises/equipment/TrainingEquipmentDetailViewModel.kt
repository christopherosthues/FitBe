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
    val isEditing: Boolean = false,
    val equipmentId: Uuid? = null,
    val error: String? = null
)

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentDetailViewModel(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent = _navigateBackEvent.asSharedFlow()

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(isLoading = false, isEditing = false, default = false, equipmentId = null, name = "", imageUri = null)
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
                            isEditing = true,
                            equipmentId = equipment.id
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
            try {
                val equipmentToSave = TrainingEquipment(
                    id = currentState.equipmentId ?: Uuid.random(),
                    name = currentState.name,
                    imageUri = currentState.imageUri,
                    default = currentState.default, // User cannot change this directly, but it's part of the state
                    dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                )

                equipmentRepository.upsertEquipment(equipmentToSave)
                _navigateBackEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save equipment: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetEquipmentToDefault() {
        val currentState = _uiState.value
        if (currentState.equipmentId == null || !currentState.default) {
            // Should not happen if button is shown correctly
            _uiState.update { it.copy(error = "Cannot reset non-default equipment or new equipment.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                equipmentRepository.resetEquipmentToDefault(currentState.equipmentId)
                // Reload the equipment to reflect the reset state
                loadEquipment(currentState.equipmentId.toString())
                // Optionally, inform the user about success, though reloading should show it
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to reset equipment: ${e.message}") }
            } finally {
                // isLoading will be set to false by loadEquipment or the catch block
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
                // We need the full TrainingEquipment object to pass to deleteEquipment in the repository
                // Or, the repository's deleteEquipment could be changed to accept an ID.
                // For now, let's assume we need the object. We can fetch it or construct it if we have all details.
                // Since we have equipmentId, name, imageUri, default status from the state, we can reconstruct it.
                // The dateUtc might not be perfectly accurate if it was loaded and not saved again,
                // but for deletion by ID (which is what Room will do), it should be fine.
                // A better approach might be to adjust repository.deleteEquipment to take an ID,
                // or ensure we always have the full, most up-to-date object.
                // Given the current repository signature, we reconstruct.
                val equipmentToDelete = TrainingEquipment(
                    id = currentState.equipmentId,
                    name = currentState.name,
                    imageUri = currentState.imageUri,
                    default = currentState.default,
                    dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date // Date may not matter for deletion by ID
                )
                equipmentRepository.deleteEquipment(equipmentToDelete)
                _navigateBackEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to delete equipment: ${e.message}") }
            } finally {
                // If deletion fails, we want to ensure isLoading is false.
                // If successful, navigation occurs, so this might not be strictly necessary, but good for safety.
                 _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
