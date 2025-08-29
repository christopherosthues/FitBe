package org.darthacheron.fitbe.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {

    val allEquipment: StateFlow<List<TrainingEquipmentEntity>> =
        exerciseDao.getAllEquipmentWithExercises() // Assuming this returns Flow<List<EquipmentWithExercisesEntity>>
            .map { list -> list.map { it.equipment } } // Transform to List<TrainingEquipmentEntity>
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = emptyList()
            )

    fun getEquipmentById(equipmentId: Uuid): Flow<TrainingEquipmentEntity?> {
        return exerciseDao.getEquipmentById(equipmentId)
    }

    fun addOrUpdateEquipment(name: String, id: Uuid? = null, isDefault: Boolean = false) {
        viewModelScope.launch {
            val equipment = TrainingEquipmentEntity(
                id = id ?: Uuid.Companion.random(),
                name = name,
                default = isDefault
            )
            exerciseDao.upsertEquipment(equipment)
        }
    }

    fun deleteEquipment(equipment: TrainingEquipmentEntity) {
        viewModelScope.launch {
            if (!equipment.default) {
                exerciseDao.deleteEquipment(equipment)
            } else {
                // Handle cannot delete default equipment (e.g., show a message)
                println("Cannot delete default equipment. ID: ${equipment.id}")
            }
        }
    }

    fun resetToDefault(equipmentId: Uuid) {
        viewModelScope.launch {
            exerciseDao.resetEquipmentToDefault(equipmentId)
        }
    }

    // Method to initially populate default equipment and their original values
    // This should be called once, perhaps during database creation/migration
    fun addInitialDefaultEquipment(defaultEquipments: List<Pair<String, Uuid>>) {
        viewModelScope.launch {
            defaultEquipments.forEach { (name, id) ->
                val equipment = TrainingEquipmentEntity(id = id, name = name, default = true)
                val defaultOriginal = DefaultTrainingEquipmentEntity(id = id, name = name)
                exerciseDao.upsertEquipment(equipment) // Add to main table
                exerciseDao.insertDefaultEquipment(defaultOriginal) // Add to defaults table
            }
        }
    }
}