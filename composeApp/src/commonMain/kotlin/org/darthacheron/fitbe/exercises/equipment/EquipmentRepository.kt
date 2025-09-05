package org.darthacheron.fitbe.exercises.equipment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EquipmentRepository(private val equipmentDao: EquipmentDao) {
    fun getAllEquipments(): Flow<List<TrainingEquipment>> =
        equipmentDao.getAllEquipments().map { it.map { e ->  e.toTrainingEquipment() } }

    fun getEquipmentById(equipmentId: Uuid): Flow<TrainingEquipment?> =
        equipmentDao.getEquipmentById(equipmentId).map { it?.toTrainingEquipment() }

    fun getDefaultEquipmentById(equipmentId: Uuid): Flow<TrainingEquipment?> =
        equipmentDao.getDefaultEquipmentById(equipmentId).map { it?.toTrainingEquipment() }

    fun getEquipmentWithExercisesById(equipmentId: Uuid): Flow<EquipmentWithExercises?> =
        equipmentDao.getEquipmentWithExercises(equipmentId).map { it?.toEquipmentWithExercises() }

    suspend fun upsertEquipment(equipment: TrainingEquipment) {
        equipmentDao.upsertEquipment(toEntity(equipment))
    }

    suspend fun deleteEquipment(equipment: TrainingEquipment) {
        equipmentDao.deleteEquipment(toEntity(equipment))
    }

    suspend fun resetEquipmentToDefault(equipmentId: Uuid) {
        equipmentDao.resetEquipmentToDefault(equipmentId)
    }
}
