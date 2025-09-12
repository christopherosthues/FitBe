package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface ExerciseExecutionDao {

    @Upsert
    suspend fun upsertExerciseExecution(exerciseExecution: ExerciseExecutionEntity)

    @Delete
    suspend fun deleteExerciseExecution(exerciseExecution: ExerciseExecutionEntity)

    @Query("SELECT * FROM exercise_executions WHERE id = :id")
    fun getExerciseExecutionById(id: Uuid): Flow<ExerciseExecutionEntity?>

    @Query("SELECT * FROM exercise_executions WHERE profileId = :profileId ORDER BY timestamp DESC")
    fun getExerciseExecutionsByProfileId(profileId: Uuid): Flow<List<ExerciseExecutionEntity>>

    @Query("SELECT * FROM exercise_executions WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    fun getExerciseExecutionsByExerciseId(exerciseId: Uuid): Flow<List<ExerciseExecutionEntity>>

    @Query("SELECT * FROM exercise_executions WHERE setId = :setId ORDER BY timestamp ASC")
    fun getExerciseExecutionsBySetId(setId: Uuid): Flow<List<ExerciseExecutionEntity>>

    // You might want more specific queries, e.g., for a date range, or for a specific workout session
    // For example, to get all executions for a profile within a workout session (if not using setId directly)
    // This would require joining with WorkoutSessionEntity or passing session start/end times
}
