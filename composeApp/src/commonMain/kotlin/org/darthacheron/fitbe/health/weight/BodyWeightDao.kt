package org.darthacheron.fitbe.health.weight

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface BodyWeightDao {
    @Upsert
    suspend fun upsertBodyWeight(bodyWeight: BodyWeightEntity)

    // TODO: check call sites for correct dates
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
}