package org.darthacheron.fitbe.exercises.equipment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.darthacheron.fitbe.exercises.ExerciseDao
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EquipmentRepository(private val exerciseDao: ExerciseDao) {
    fun getAllEquipmentWithExercises(): Flow<List<EquipmentWithExercises>> =
        exerciseDao.getAllEquipmentWithExercises().map { it.map { e ->  e.toEquipmentWithExercises() } }

    fun getAllEquipments(): Flow<List<TrainingEquipment>> =
        exerciseDao.getAllEquipments().map { it.map { e ->  e.toTrainingEquipment() } }

    fun getEquipmentById(equipmentId: Uuid): Flow<TrainingEquipment?> =
        exerciseDao.getEquipmentById(equipmentId).map { it?.toTrainingEquipment() }

    fun getDefaultEquipmentById(equipmentId: Uuid): Flow<TrainingEquipment?> =
        exerciseDao.getDefaultEquipmentById(equipmentId).map { it?.toTrainingEquipment() }

    fun getEquipmentWithExercisesById(equipmentId: Uuid): Flow<EquipmentWithExercises?> =
        exerciseDao.getEquipmentWithExercises(equipmentId).map { it?.toEquipmentWithExercises() }

    suspend fun upsertEquipment(equipment: TrainingEquipment) {
        exerciseDao.upsertEquipment(toEntity(equipment))
    }

    suspend fun deleteEquipment(equipment: TrainingEquipment) {
        exerciseDao.deleteEquipment(toEntity(equipment))
    }

    suspend fun resetEquipmentToDefault(equipmentId: Uuid) {
        exerciseDao.resetEquipmentToDefault(equipmentId)
    }
}
