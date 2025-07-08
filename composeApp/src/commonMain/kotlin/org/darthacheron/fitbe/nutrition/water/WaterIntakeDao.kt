package org.darthacheron.fitbe.nutrition.water

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Upsert
    suspend fun upsertIntake(intake: WaterIntakeEntity)

    @Query("SELECT * FROM water_intake WHERE date = :today")
    fun getTodayIntake(today: String): Flow<WaterIntakeEntity?>
}