package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface BeverageDao {
    @Upsert
    suspend fun upsertBeverage(intake: BeverageEntity)

    @Query("""
        SELECT * FROM beverages 
        WHERE profileId = :profileId
        AND dateUtc BETWEEN :start AND :end
        ORDER BY dateUtc ASC
    """)
    fun getBeverages(start: String, end: String, profileId: Uuid): Flow<List<BeverageEntity>>
}