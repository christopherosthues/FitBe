package org.darthacheron.fitbe.exercises

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
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val equipmentId: Uuid? = null,
    val error: String? = null
)

@OptIn(ExperimentalUuidApi::class)
class AddEditTrainingEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent = _navigateBackEvent.asSharedFlow()

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(isLoading = false, isEditing = false, equipmentId = null, name = "", imageUri = null)
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val equipmentId = Uuid.parse(equipmentIdString)
                // Collect the first item from the Flow or null if the Flow is empty
                val equipment = equipmentRepository.getEquipmentById(equipmentId).firstOrNull()
                if (equipment != null) {
                    _uiState.update {
                        it.copy(
                            name = equipment.name,
                            imageUri = equipment.imageUri,
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
                // Handle potential UUID parsing errors or other exceptions
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
                    id = currentState.equipmentId ?: Uuid.random(), // Generate new ID if null
                    name = currentState.name,
                    imageUri = currentState.imageUri,
                    default = false, // Assuming user-added equipment is not default
                    dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                )

                // Assuming upsertEquipment exists or needs to be replaced with insert/update logic
                equipmentRepository.upsertEquipment(equipmentToSave)
                _navigateBackEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save equipment: ${e.message}") }
            } finally {
                 // Ensure loading is set to false even if navigateBackEvent emission fails or an error occurs before it
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
