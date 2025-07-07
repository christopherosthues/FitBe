package org.darthacheron.fitbe.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.darthacheron.fitbe.nutrition.water.WaterConsumptionDao
import org.darthacheron.fitbe.nutrition.water.WaterConsumptionEntity

@Database(entities = [WaterConsumptionEntity::class], version = 1)
@ConstructedBy(FitBeDatabaseConstructor::class)
abstract class FitBeDatabase : RoomDatabase() {
    abstract val waterConsumptionDao: WaterConsumptionDao

    companion object {
        const val DB_NAME = "fitbe.db"
    }
}

