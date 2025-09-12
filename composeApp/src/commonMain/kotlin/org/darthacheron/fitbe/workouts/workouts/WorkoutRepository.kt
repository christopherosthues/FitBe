package org.darthacheron.fitbe.workouts.workouts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

// DAOs are implicitly in the same package
// Domain models are implicitly in the same package
// Entity to Domain mapping functions are implicitly in the same package or members of entities

class WorkoutRepository(
    private val workoutSessionDao: WorkoutSessionDao,
    private val setDao: SetDao,
    private val exerciseExecutionDao: ExerciseExecutionDao
) {

    // WorkoutSession operations
    suspend fun upsertWorkoutSession(workoutSession: WorkoutSession) {
        workoutSessionDao.upsertWorkoutSession(toEntity(workoutSession))
    }

    suspend fun deleteWorkoutSession(workoutSession: WorkoutSession) {
        workoutSessionDao.deleteWorkoutSession(toEntity(workoutSession))
    }

    fun getWorkoutSessionById(id: Uuid): Flow<WorkoutSession?> {
        return workoutSessionDao.getWorkoutSessionById(id).map { entity ->
            entity?.toWorkoutSession()
        }
    }

    fun getWorkoutSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutSession>> {
        return workoutSessionDao.getWorkoutSessionsByProfileId(profileId).map { list ->
            list.map { it.toWorkoutSession() }
        }
    }

    fun getLastWorkoutSessionByProfileId(profileId: Uuid): Flow<WorkoutSession?> {
        return workoutSessionDao.getLastWorkoutSessionByProfileId(profileId).map { entity ->
            entity?.toWorkoutSession()
        }
    }

    fun getWorkoutSessionsByProfileIdAndDateRange(profileId: Uuid, startTime: Long, endTime: Long): Flow<List<WorkoutSession>> {
        return workoutSessionDao.getWorkoutSessionsByProfileIdAndDateRange(profileId, startTime, endTime).map { list ->
            list.map { it.toWorkoutSession() }
        }
    }

    // ExerciseSet operations
    suspend fun upsertExerciseSet(exerciseSet: ExerciseSet) {
        setDao.upsertSet(toEntity(exerciseSet))
    }

    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet) {
        setDao.deleteSet(toEntity(exerciseSet))
    }

    fun getExerciseSetById(id: Uuid): Flow<ExerciseSet?> {
        return setDao.getSetById(id).map { entity ->
            entity?.toExerciseSet()
        }
    }

    fun getExerciseSetsByWorkoutSessionId(workoutSessionId: Uuid): Flow<List<ExerciseSet>> {
        return setDao.getSetsByWorkoutSessionId(workoutSessionId).map { list ->
            list.map { it.toExerciseSet() }
        }
    }

    fun getExerciseSetsByWorkoutSessionAndExerciseId(workoutSessionId: Uuid, exerciseId: Uuid): Flow<List<ExerciseSet>> {
        return setDao.getSetsByWorkoutSessionAndExerciseId(workoutSessionId, exerciseId).map { list ->
            list.map { it.toExerciseSet() }
        }
    }

    // ExerciseExecution operations
    suspend fun upsertExerciseExecution(exerciseExecution: ExerciseExecution) {
        exerciseExecutionDao.upsertExerciseExecution(toEntity(exerciseExecution))
    }

    suspend fun deleteExerciseExecution(exerciseExecution: ExerciseExecution) {
        exerciseExecutionDao.deleteExerciseExecution(toEntity(exerciseExecution))
    }

    fun getExerciseExecutionById(id: Uuid): Flow<ExerciseExecution?> {
        return exerciseExecutionDao.getExerciseExecutionById(id).map { entity ->
            entity?.toExerciseExecution()
        }
    }

    fun getExerciseExecutionsByProfileId(profileId: Uuid): Flow<List<ExerciseExecution>> {
        return exerciseExecutionDao.getExerciseExecutionsByProfileId(profileId).map { list ->
            list.map { it.toExerciseExecution() }
        }
    }

    fun getExerciseExecutionsByExerciseId(exerciseId: Uuid): Flow<List<ExerciseExecution>> {
        return exerciseExecutionDao.getExerciseExecutionsByExerciseId(exerciseId).map { list ->
            list.map { it.toExerciseExecution() }
        }
    }

    fun getExerciseExecutionsBySetId(setId: Uuid): Flow<List<ExerciseExecution>> {
        return exerciseExecutionDao.getExerciseExecutionsBySetId(setId).map { list ->
            list.map { it.toExerciseExecution() }
        }
    }
}
