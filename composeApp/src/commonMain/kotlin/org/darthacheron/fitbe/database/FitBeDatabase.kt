package org.darthacheron.fitbe.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.darthacheron.fitbe.health.beverages.BeverageDao
import org.darthacheron.fitbe.health.beverages.BeverageEntity
import org.darthacheron.fitbe.health.sleep.SleepDao
import org.darthacheron.fitbe.health.sleep.SleepEntity

@Database(entities = [BeverageEntity::class, SleepEntity::class], version = 1)
@TypeConverters(FluidUnitConverter::class, UuidConverter::class, DateConverter::class)
@ConstructedBy(FitBeDatabaseConstructor::class)
abstract class FitBeDatabase : RoomDatabase() {
    abstract val waterConsumptionDao: BeverageDao
    abstract val sleepDao: SleepDao

    companion object {
        const val DB_NAME = "fitbe.db"
    }
}

