package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface BeverageDao {
    @Upsert
    suspend fun upsertBeverage(intake: BeverageEntity)

    // TODO: check call sites for correct dates
    @Query(
        """
        SELECT * FROM beverages 
        WHERE profileId = :profileId
        AND dateUtc >= :start AND dateUtc < :end
        ORDER BY dateUtc ASC
        """
    )
    fun getBeverages(
        start: Instant,
        end: Instant,
        profileId: Uuid
    ): Flow<List<BeverageEntity>>

    @Query("DELETE FROM beverages WHERE id = :id")
    suspend fun deleteBeverage(id: Uuid)
}