package org.darthacheron.fitbe.workouts.workouts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WorkoutExecutionRepository(
    private val workoutExecutionDao: WorkoutExecutionDao
) {
    suspend fun startNewWorkoutExecution(
        profileId: Uuid,
        exerciseId: Uuid,
        plannedSets: Int
    ): Uuid {
        val newExecution =
            WorkoutExecutionEntity(
                exerciseId = exerciseId,
                profileId = profileId,
                startTimeUtc = Clock.System.now(),
                status = WorkoutExecutionStatus.IN_PROGRESS,
                totalPlannedSets = plannedSets
            )
        workoutExecutionDao.upsertWorkoutExecution(newExecution)
        return newExecution.id
    }

    suspend fun saveWorkoutSet(
        workoutExecutionId: Uuid,
        setNumber: Int,
        status: WorkoutSetStatus,
        targetReps: Int?,
        targetWeightKg: Double?,
        targetDurationSeconds: Int?,
        targetDistanceKm: Double?,
        actualReps: Int?,
        actualWeightKg: Double?,
        actualDurationSeconds: Int?,
        actualDistanceKm: Double?,
        restTimeSecondsAfterSet: Int? = null,
        notes: String? = null
    ) {
        val workoutSet =
            WorkoutSetExecutionEntity(
                workoutExecutionId = workoutExecutionId,
                setNumber = setNumber,
                status = status,
                targetReps = targetReps,
                targetWeightKg = targetWeightKg,
                targetDurationSeconds = targetDurationSeconds,
                targetDistanceKm = targetDistanceKm,
                actualReps = actualReps,
                actualWeightKg = actualWeightKg,
                actualDurationSeconds = actualDurationSeconds,
                actualDistanceKm = actualDistanceKm,
                restTimeSecondsAfterSet = restTimeSecondsAfterSet,
                notes = notes
            )
        workoutExecutionDao.insertWorkoutSetExecution(workoutSet)
    }

    suspend fun updateWorkoutExecutionStatus(
        workoutExecutionId: Uuid,
        newStatus: WorkoutExecutionStatus,
        endTime: Instant? = Clock.System.now()
    ) {
        val execution =
            workoutExecutionDao
                .getWorkoutExecutionWithSets(workoutExecutionId)
                .firstOrNull()
                ?.workoutExecution
        execution?.let {
            val updatedExecution =
                it.copy(
                    status = newStatus,
                    endTimeUtc = if (newStatus == WorkoutExecutionStatus.IN_PROGRESS) null else endTime
                )
            workoutExecutionDao.upsertWorkoutExecution(updatedExecution)
        }
    }

    fun getWorkoutExecutionWithSets(workoutExecutionId: Uuid): Flow<WorkoutExecutionWithSetsEntity?> =
        workoutExecutionDao.getWorkoutExecutionWithSets(workoutExecutionId)

    fun getInProgressWorkoutExecution(
        profileId: Uuid,
        exerciseId: Uuid
    ): Flow<WorkoutExecutionEntity?> = workoutExecutionDao.getInProgressWorkoutExecution(exerciseId, profileId)

    fun getAllWorkoutExecutionsForProfile(profileId: Uuid): Flow<List<WorkoutExecutionWithSetsEntity>> =
        workoutExecutionDao.getAllWorkoutExecutionsForProfile(profileId)
}