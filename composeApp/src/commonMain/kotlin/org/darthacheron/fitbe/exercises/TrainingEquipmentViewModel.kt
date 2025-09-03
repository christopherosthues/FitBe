package org.darthacheron.fitbe.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentViewModel(private val equipmentRepository: EquipmentRepository) : ViewModel() {

    val allEquipment: StateFlow<List<EquipmentWithExercises>> =
        equipmentRepository.getAllEquipmentWithExercises()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = emptyList()
            )

    fun getEquipmentById(equipmentId: Uuid): Flow<TrainingEquipment?> {
        return equipmentRepository.getEquipmentById(equipmentId)
    }

    fun addOrUpdateEquipment(name: String, id: Uuid? = null, imageUri: String? = null, isDefault: Boolean = false, dateUtc: LocalDate) {
        viewModelScope.launch {
            val equipment = TrainingEquipment(
                id = id ?: Uuid.Companion.random(),
                name = name,
                imageUri = imageUri,
                default = isDefault,
                dateUtc = dateUtc
            )
            equipmentRepository.upsertEquipment(equipment)
        }
    }

    fun deleteEquipment(equipment: TrainingEquipment) {
        viewModelScope.launch {
            if (!equipment.default) {
                equipmentRepository.deleteEquipment(equipment)
            } else {
                // Handle cannot delete default equipment (e.g., show a message)
                println("Cannot delete default equipment. ID: ${equipment.id}")
            }
        }
    }

    fun resetToDefault(equipmentId: Uuid) {
        viewModelScope.launch {
            equipmentRepository.resetEquipmentToDefault(equipmentId)
        }
    }
}
