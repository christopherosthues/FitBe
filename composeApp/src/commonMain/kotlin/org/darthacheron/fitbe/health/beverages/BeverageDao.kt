package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

@Dao
interface BeverageDao {
    @Upsert
    suspend fun upsertDrink(intake: BeverageEntity)

    @OptIn(ExperimentalTime::class)
    @Query("SELECT * FROM beverages WHERE dateUtc BETWEEN :start AND :end")
    fun getTodayDrinks(start: String, end: String): Flow<List<BeverageEntity>>
}