package org.darthacheron.fitbe.workouts.exercises

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ExerciseRepository(
    private val exerciseDao: ExerciseDao
) {
    fun getAllExercises(): Flow<List<Exercise>> = exerciseDao.getAllExercises().map { it.map { e -> e.toExercise() } }

    fun getExerciseById(exerciseId: Uuid): Flow<Exercise?> =
        exerciseDao.getExerciseById(exerciseId).map { it?.toExercise() }

    fun getDefaultExerciseId(exerciseId: Uuid): Flow<Exercise?> =
        exerciseDao.getDefaultExerciseById(exerciseId).map { it?.toExercise() }

    fun getDefaultExerciseWithEquipment(exerciseId: Uuid): Flow<ExerciseWithEquipment?> =
        exerciseDao.getDefaultExerciseWithEquipment(exerciseId).map { it?.toExerciseWithEquipment() }

    fun getExerciseWithExercisesById(exerciseId: Uuid): Flow<ExerciseWithEquipment?> =
        exerciseDao.getExerciseWithEquipment(exerciseId).map { it?.toExerciseWithEquipment() }

    suspend fun upsertExercise(exercise: Exercise) {
        exerciseDao.upsertExercise(exercise.toExerciseEntity())
    }

    suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise.toExerciseEntity())
    }

    suspend fun resetExerciseToDefault(exerciseId: Uuid) {
        exerciseDao.resetExerciseToDefault(exerciseId)
    }

    suspend fun updateExerciseEquipmentLinks(
        exerciseId: Uuid,
        equipmentIds: List<Uuid>
    ) {
        exerciseDao.updateExerciseEquipmentLinks(exerciseId, equipmentIds)
    }

    fun getFavoriteExerciseIds(profileId: Uuid): Flow<List<Uuid>> = exerciseDao.getFavoriteExerciseIds(profileId)

    fun isFavorite(
        profileId: Uuid,
        exerciseId: Uuid
    ): Flow<Boolean> = exerciseDao.isFavorite(profileId, exerciseId)

    suspend fun addFavorite(
        profileId: Uuid,
        exerciseId: Uuid
    ) {
        exerciseDao.addFavorite(ProfileFavoriteExerciseCrossRef(profileId, exerciseId))
    }

    suspend fun removeFavorite(
        profileId: Uuid,
        exerciseId: Uuid
    ) {
        exerciseDao.removeFavorite(ProfileFavoriteExerciseCrossRef(profileId, exerciseId))
    }
}