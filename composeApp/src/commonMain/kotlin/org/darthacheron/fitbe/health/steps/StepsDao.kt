package org.darthacheron.fitbe.health.steps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface StepsDao {
    @Query("""
        SELECT * FROM steps 
        WHERE profileId = :profileId
        AND dateUtc BETWEEN SUBSTR(:startDate, 1, 10) AND SUBSTR(:endDate, 1, 10)
        ORDER BY dateUtc ASC
    """)
    fun getStepsBetweenDates(
        startDate: String,
        endDate: String,
        profileId: Uuid
    ): Flow<List<StepsEntity>>

    @Query("""
        SELECT * FROM steps
        WHERE dateUtc = :today
        AND profileId = :profileId
        LIMIT 1
    """)
    fun getStepsForDate(today: String, profileId: Uuid): Flow<StepsEntity>

    @Upsert
    suspend fun upsertSteps(steps: StepsEntity)

    @Delete
    suspend fun deleteSteps(steps: StepsEntity)
}