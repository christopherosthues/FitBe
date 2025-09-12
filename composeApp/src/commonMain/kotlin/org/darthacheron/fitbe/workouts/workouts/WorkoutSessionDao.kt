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
interface WorkoutSessionDao {

    @Upsert
    suspend fun upsertWorkoutSession(workoutSession: WorkoutSessionEntity)

    @Delete
    suspend fun deleteWorkoutSession(workoutSession: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    fun getWorkoutSessionById(id: Uuid): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_sessions WHERE profileId = :profileId ORDER BY startTimestamp DESC")
    fun getWorkoutSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutSessionEntity>>

    // Query to get the last workout session for a profile
    @Query("SELECT * FROM workout_sessions WHERE profileId = :profileId ORDER BY startTimestamp DESC LIMIT 1")
    fun getLastWorkoutSessionByProfileId(profileId: Uuid): Flow<WorkoutSessionEntity?>

    // Query to get workout sessions within a specific date range
    @Query("SELECT * FROM workout_sessions WHERE profileId = :profileId AND startTimestamp >= :startTime AND startTimestamp <= :endTime ORDER BY startTimestamp DESC")
    fun getWorkoutSessionsByProfileIdAndDateRange(profileId: Uuid, startTime: Long, endTime: Long): Flow<List<WorkoutSessionEntity>>
}
