package org.darthacheron.fitbe.health.beverages

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BeverageDao {
    @Upsert
    suspend fun upsertDrink(intake: BeverageEntity)

    @Query("SELECT * FROM beverages WHERE dateUtc = :today")
    fun getTodayDrinks(today: String): Flow<List<BeverageEntity>>
}