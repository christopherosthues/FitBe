package org.darthacheron.fitbe.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.BeverageDao
import org.darthacheron.fitbe.health.beverages.BeverageEntity
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.sleep.SleepDao
import org.darthacheron.fitbe.health.sleep.SleepEntity
import org.darthacheron.fitbe.health.weight.BodyWeightDao
import kotlin.random.Random
import kotlinx.datetime.Clock
import org.darthacheron.fitbe.database.converters.InstantConverter
import org.darthacheron.fitbe.database.converters.FluidUnitConverter
import org.darthacheron.fitbe.database.converters.GenderConverter
import org.darthacheron.fitbe.database.converters.LocalDateConverter
import org.darthacheron.fitbe.database.converters.LocalDateTimeConverter
import org.darthacheron.fitbe.database.converters.LocalTimeConverter
import org.darthacheron.fitbe.database.converters.UuidConverter
import org.darthacheron.fitbe.health.weight.BodyWeightEntity
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@Database(
    entities = [BeverageEntity::class, SleepEntity::class, ProfileEntity::class, BodyWeightEntity::class],
    version = 1
)
@TypeConverters(
    FluidUnitConverter::class, UuidConverter::class, InstantConverter::class,
    LocalDateTimeConverter::class, GenderConverter::class, LocalTimeConverter::class,
    LocalDateConverter::class
)
@ConstructedBy(FitBeDatabaseConstructor::class)
abstract class FitBeDatabase : RoomDatabase() {
    abstract val beverageDao: BeverageDao
    abstract val sleepDao: SleepDao
    abstract val bodyWeightDao: BodyWeightDao
    abstract val profileDao: ProfileDao

    companion object {
        const val DB_NAME = "fitbe.db"
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
suspend fun seedDatabase(db: FitBeDatabase) {
    val sleepDao = db.sleepDao
    val beverageDao = db.beverageDao
    for (i in 1..730) {
        val sleep = SleepEntity(
            dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                i - 1,
                DateTimeUnit.DAY
            ).atStartOfDayIn(TimeZone.UTC),
            hours = Random.nextInt(0, 12),
            minutes = Random.nextInt(0, 59)
        )
        sleepDao.upsertSleep(
            sleep
        )

        for (j in 1..Random.nextInt(1, 5)) {
            val beverage = BeverageEntity(
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                    i - 1,
                    DateTimeUnit.DAY
                ).atStartOfDayIn(TimeZone.UTC),
                beverage = "",
                amount = Random.nextInt(200, 1500),
                unit = FluidUnit.Milliliter
            )
            beverageDao.upsertDrink(beverage)
        }
    }
}