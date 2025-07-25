package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Dao
interface BeverageDao {
    @Upsert
    suspend fun upsertDrink(intake: BeverageEntity)

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    @Query("SELECT * FROM beverages WHERE profileId = :profileId AND dateUtc BETWEEN :start AND :end ORDER BY dateUtc ASC")
    fun getTodayDrinks(start: String, end: String, profileId: Uuid): Flow<List<BeverageEntity>>
}