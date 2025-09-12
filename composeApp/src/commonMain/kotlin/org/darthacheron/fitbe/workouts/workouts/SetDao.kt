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
interface SetDao {

    @Upsert
    suspend fun upsertSet(set: SetEntity)

    @Delete
    suspend fun deleteSet(set: SetEntity)

    @Query("SELECT * FROM sets WHERE id = :id")
    fun getSetById(id: Uuid): Flow<SetEntity?>

    @Query("SELECT * FROM sets WHERE workoutSessionId = :workoutSessionId ORDER BY setOrder ASC")
    fun getSetsByWorkoutSessionId(workoutSessionId: Uuid): Flow<List<SetEntity>>

    // Potentially a query to get sets for a specific exercise within a session
    @Query("SELECT * FROM sets WHERE workoutSessionId = :workoutSessionId AND exerciseId = :exerciseId ORDER BY setOrder ASC")
    fun getSetsByWorkoutSessionAndExerciseId(workoutSessionId: Uuid, exerciseId: Uuid): Flow<List<SetEntity>>
}
