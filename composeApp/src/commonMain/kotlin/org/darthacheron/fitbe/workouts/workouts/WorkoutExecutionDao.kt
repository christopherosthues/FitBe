package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface WorkoutExecutionDao {

    @Upsert
    suspend fun upsertWorkoutExecution(workoutExecution: WorkoutExecutionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Or IGNORE if sets are immutable once recorded
    suspend fun insertWorkoutSetExecution(workoutSetExecution: WorkoutSetExecutionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSetExecutions(workoutSetExecutions: List<WorkoutSetExecutionEntity>)

    @Transaction
    @Query("SELECT * FROM workout_executions WHERE id = :workoutExecutionId")
    fun getWorkoutExecutionWithSets(workoutExecutionId: Uuid): Flow<WorkoutExecutionWithSetsEntity?>

    @Transaction
    @Query("SELECT * FROM workout_executions WHERE profile_id = :profileId ORDER BY start_time_utc DESC")
    fun getAllWorkoutExecutionsForProfile(profileId: Uuid): Flow<List<WorkoutExecutionWithSetsEntity>>

    @Query("SELECT * FROM workout_executions WHERE exercise_id = :exerciseId AND profile_id = :profileId AND status = 'IN_PROGRESS' ORDER BY start_time_utc DESC LIMIT 1")
    fun getInProgressWorkoutExecution(exerciseId: Uuid, profileId: Uuid): Flow<WorkoutExecutionEntity?>

    @Query("SELECT * FROM workout_set_executions WHERE workout_execution_id = :workoutExecutionId ORDER BY set_number ASC")
    fun getSetsForWorkoutExecution(workoutExecutionId: Uuid): Flow<List<WorkoutSetExecutionEntity>>
}
