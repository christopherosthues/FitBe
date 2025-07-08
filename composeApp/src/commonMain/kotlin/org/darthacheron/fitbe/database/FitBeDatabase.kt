package org.darthacheron.fitbe.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.darthacheron.fitbe.nutrition.water.WaterIntakeDao
import org.darthacheron.fitbe.nutrition.water.WaterIntakeEntity

@Database(entities = [WaterIntakeEntity::class], version = 1)
@ConstructedBy(FitBeDatabaseConstructor::class)
abstract class FitBeDatabase : RoomDatabase() {
    abstract val waterConsumptionDao: WaterIntakeDao

    companion object {
        const val DB_NAME = "fitbe.db"
    }
}

