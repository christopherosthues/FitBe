package org.darthacheron.fitbe.workouts.workouts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant // Added import for Instant
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

    // Get IN-PROGRESS or COMPLETED sessions by ProfileId
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND startTimestamp IS NOT NULL ORDER BY startTimestamp DESC")
    fun getCompletedOrInProgressSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutExecutionSessionEntity>>

    // Get LAST IN-PROGRESS or COMPLETED session by ProfileId
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND startTimestamp IS NOT NULL ORDER BY startTimestamp DESC LIMIT 1")
    fun getLastCompletedOrInProgressSessionByProfileId(profileId: Uuid): Flow<WorkoutExecutionSessionEntity?>

    // Get IN-PROGRESS or COMPLETED sessions by ProfileId and DateRange (based on startTimestamp)
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND startTimestamp IS NOT NULL AND startTimestamp >= :fromInstant AND startTimestamp <= :toInstant ORDER BY startTimestamp DESC")
    fun getCompletedOrInProgressSessionsByProfileIdAndDateRange(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSessionEntity>>

    // --- New queries for SCHEDULED sessions ---
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND scheduledTimestamp IS NOT NULL AND startTimestamp IS NULL ORDER BY scheduledTimestamp ASC")
    fun getScheduledWorkoutExecutionSessions(profileId: Uuid): Flow<List<WorkoutExecutionSessionEntity>>

    @Transaction // To also fetch performed sets for scheduled sessions if needed immediately
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND scheduledTimestamp IS NOT NULL AND startTimestamp IS NULL ORDER BY scheduledTimestamp ASC")
    fun getScheduledWorkoutExecutionSessionsWithSets(profileId: Uuid): Flow<List<WorkoutExecutionSessionWithSets>>

    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND scheduledTimestamp IS NOT NULL AND startTimestamp IS NULL AND scheduledTimestamp >= :fromInstant AND scheduledTimestamp <= :toInstant ORDER BY scheduledTimestamp ASC")
    fun getScheduledWorkoutExecutionSessionsByDateRange(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSessionEntity>>
    
    @Transaction
    @Query("SELECT * FROM workout_execution_sessions WHERE profileId = :profileId AND scheduledTimestamp IS NOT NULL AND startTimestamp IS NULL AND scheduledTimestamp >= :fromInstant AND scheduledTimestamp <= :toInstant ORDER BY scheduledTimestamp ASC")
    fun getScheduledWorkoutExecutionSessionsByDateRangeWithSets(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSessionWithSets>>

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
