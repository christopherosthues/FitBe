package org.darthacheron.fitbe.health.weight

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import org.darthacheron.fitbe.health.steps.StepsEntity
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Dao
interface BodyWeightDao {
    @Query("SELECT * FROM body_weights WHERE id = :id")
    suspend fun getBodyWeight(id: Uuid): BodyWeightEntity?

    @Upsert
    suspend fun upsertBodyWeight(bodyWeight: BodyWeightEntity)

    @Query("SELECT * FROM body_weights WHERE profileId = :profileId ORDER BY dateUtc DESC")
    fun getAllBodyWeightsForProfile(profileId: Uuid): Flow<List<BodyWeightEntity>>

    @Query(
        """
        SELECT * FROM body_weights 
        WHERE profileId = :profileId
        AND dateUtc >= :start AND dateUtc < :end
        ORDER BY dateUtc ASC
        """
    )
    fun getBodyWeightsBetweenDates(
        start: Instant,
        end: Instant,
        profileId: Uuid
    ): Flow<List<BodyWeightEntity>>

    @Query("DELETE FROM body_weights WHERE id = :id")
    suspend fun deleteBodyWeight(id: Uuid)
}