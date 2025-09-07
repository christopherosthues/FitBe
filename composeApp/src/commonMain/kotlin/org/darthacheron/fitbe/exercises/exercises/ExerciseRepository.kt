package org.darthacheron.fitbe.exercises.exercises

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ExerciseRepository(private val exerciseDao: ExerciseDao) {
    fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercises().map { it.map { e ->  e.toExercise() } }

    fun getExerciseById(exerciseId: Uuid): Flow<Exercise?> =
        exerciseDao.getExerciseById(exerciseId).map { it?.toExercise() }

    fun getDefaultExerciseId(exerciseId: Uuid): Flow<Exercise?> =
        exerciseDao.getDefaultExerciseById(exerciseId).map { it?.toExercise() }

    fun getDefaultExerciseWithEquipment(exerciseId: Uuid): Flow<ExerciseWithEquipment?> =
        exerciseDao.getDefaultExerciseWithEquipment(exerciseId).map { it?.toDefaultExerciseWithEquipment() }

    fun getExerciseWithExercisesById(exerciseId: Uuid): Flow<ExerciseWithEquipment?> =
        exerciseDao.getExerciseWithEquipment(exerciseId).map { it?.toExerciseWithEquipment() }

    suspend fun upsertExercise(exercise: Exercise) {
        exerciseDao.upsertExercise(toEntity(exercise))
    }

    suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(toEntity(exercise))
    }

    suspend fun resetExerciseToDefault(exerciseId: Uuid) {
        exerciseDao.resetExerciseToDefault(exerciseId)
    }
}