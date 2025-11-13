package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Dao
interface BeverageDao {
    @Query("SELECT * FROM beverages WHERE id = :id")
    suspend fun getBeverage(id: Uuid): BeverageEntity?

    @Upsert
    suspend fun upsertBeverage(beverage: BeverageEntity)

    @Query("SELECT * FROM beverages WHERE profileId = :profileId ORDER BY dateUtc DESC")
    suspend fun getAllBeveragesForProfile(profileId: Uuid): List<BeverageEntity>

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