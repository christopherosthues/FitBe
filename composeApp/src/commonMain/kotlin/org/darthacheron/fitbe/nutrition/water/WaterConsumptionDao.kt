package org.darthacheron.fitbe.nutrition.water

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface WaterConsumptionDao {
    @Upsert
    suspend fun upsert(waterConsumption: WaterConsumption)

    suspend fun getWaterConsumption(id: String?): WaterConsumption?
}