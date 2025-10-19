package org.darthacheron.fitbe.health.steps

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface StepsDao {
    @Query("SELECT * FROM steps WHERE id = :id")
    suspend fun getSteps(id: Uuid): StepsEntity?

    @Upsert
    suspend fun upsertSteps(steps: StepsEntity)

    // TODO: check call sites for correct dates
    @Query(
        """
        SELECT * FROM steps 
        WHERE profileId = :profileId
        AND dateUtc >= :start AND dateUtc < :end
        ORDER BY dateUtc ASC
        """
    )
    fun getStepsBetweenDates(
        start: Instant,
        end: Instant,
        profileId: Uuid
    ): Flow<List<StepsEntity>>

    @Query("DELETE FROM steps WHERE id = :id")
    suspend fun deleteSteps(id: Uuid)
}