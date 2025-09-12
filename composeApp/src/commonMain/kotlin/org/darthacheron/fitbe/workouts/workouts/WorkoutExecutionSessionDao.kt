package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
// Import for the relationship data class
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionSessionWithSets

@OptIn(ExperimentalUuidApi::class)
@Dao
interface WorkoutExecutionSessionDao {

    @Upsert
    suspend fun upsertWorkoutExecutionSession(workoutExecutionSession: WorkoutExecutionSessionEntity)

    @Delete
    suspend fun deleteWorkoutExecutionSession(workoutExecutionSession: WorkoutExecutionSessionEntity)

    @Query("SELECT * FROM workout_execution_sessions WHERE id = :id")
    fun getWorkoutExecutionSessionById(id: Uuid): Flow<WorkoutExecutionSessionEntity?>

    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId ORDER BY startTimestamp DESC")
    fun getWorkoutExecutionSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutExecutionSessionEntity>>

    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId ORDER BY startTimestamp DESC LIMIT 1")
    fun getLastWorkoutExecutionSessionByProfileId(profileId: Uuid): Flow<WorkoutExecutionSessionEntity?>

    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND startTimestamp >= :startTime AND startTimestamp <= :endTime ORDER BY startTimestamp DESC")
    fun getWorkoutExecutionSessionsByProfileIdAndDateRange(profileId: Uuid, startTime: Long, endTime: Long): Flow<List<WorkoutExecutionSessionEntity>>

    // Methods for PerformedSetEntity
    @Upsert
    suspend fun upsertPerformedSet(performedSet: PerformedSetEntity)

    @Upsert
    suspend fun upsertPerformedSets(performedSets: List<PerformedSetEntity>)

    @Delete
    suspend fun deletePerformedSet(performedSet: PerformedSetEntity)

    @Query("SELECT * FROM performed_sets WHERE workoutExecutionSessionId = :workoutExecutionSessionId ORDER BY setOrder ASC")
    fun getPerformedSetsForWorkoutExecutionSession(workoutExecutionSessionId: Uuid): Flow<List<PerformedSetEntity>>

    // Transaction to get a WorkoutExecutionSession with its PerformedSets
    @Transaction
    @Query("SELECT * FROM workout_execution_sessions WHERE id = :sessionId")
    fun getWorkoutExecutionSessionWithSets(sessionId: Uuid): Flow<WorkoutExecutionSessionWithSets?>
}
