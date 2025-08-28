package org.darthacheron.fitbe.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.database.converters.FluidUnitConverter
import org.darthacheron.fitbe.database.converters.GenderConverter
import org.darthacheron.fitbe.database.converters.InstantConverter
import org.darthacheron.fitbe.database.converters.LocalDateConverter
import org.darthacheron.fitbe.database.converters.LocalDateTimeConverter
import org.darthacheron.fitbe.database.converters.LocalTimeConverter
import org.darthacheron.fitbe.database.converters.UuidConverter
import org.darthacheron.fitbe.exercises.ExerciseEntity
import org.darthacheron.fitbe.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.exercises.TrainingEquipmentEntity
import org.darthacheron.fitbe.health.beverages.BeverageDao
import org.darthacheron.fitbe.health.beverages.BeverageEntity
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.sleep.SleepDao
import org.darthacheron.fitbe.health.sleep.SleepEntity
import org.darthacheron.fitbe.health.steps.Steps
import org.darthacheron.fitbe.health.steps.StepsDao
import org.darthacheron.fitbe.health.steps.StepsEntity
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.health.weight.BodyWeightDao
import org.darthacheron.fitbe.health.weight.BodyWeightEntity
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileEntity
import org.darthacheron.fitbe.utils.roundToDecimals
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@Database(
    entities = [BeverageEntity::class, SleepEntity::class, ProfileEntity::class, BodyWeightEntity::class, StepsEntity::class, TrainingEquipmentEntity::class, ExerciseEntity::class, ExerciseEquipmentCrossRef::class],
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
    abstract val stepsDao: StepsDao

    companion object {
        const val DB_NAME = "fitbe.db"
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
suspend fun seedDatabase(db: FitBeDatabase) {
    val sleepDao = db.sleepDao
    val beverageDao = db.beverageDao
    val profileDao = db.profileDao
    val bodyWeightDao = db.bodyWeightDao
    val stepsDao = db.stepsDao

    val profile = profileDao.getAllProfiles().first().first()

    for (i in 1..730) {
        val sleep = SleepEntity(
            dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                i - 1,
                DateTimeUnit.DAY
            ).atStartOfDayIn(TimeZone.UTC),
            hours = Random.nextInt(0, 12),
            minutes = Random.nextInt(0, 59),
            profileId = profile.id
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
                unit = FluidUnit.Milliliter,
                profileId = profile.id
            )
            beverageDao.upsertBeverage(beverage)
        }

        val steps = StepsEntity(
            steps = Random.nextInt(0, 20_000),
            dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                i - 1,
                DateTimeUnit.DAY
            ),
            profileId = profile.id
        )
        stepsDao.upsertSteps(steps)

        // Step 1: Generate initial weight
        var weightInKg = Random.nextDouble(55.0, 130.0).roundToDecimals(2)

        // Step 2: Randomize component masses in kg
        val muscleMassInKg = Random.nextDouble(20.0, weightInKg * 0.6).roundToDecimals(2)
        val boneMassInKg = Random.nextDouble(2.0, 4.0).roundToDecimals(2)
        val bodyFatInKg = Random.nextDouble(10.0, weightInKg * 0.3).roundToDecimals(2)
        val bodyWaterInKg = Random.nextDouble(25.0, weightInKg * 0.65).roundToDecimals(2)

        // Step 3: Compute total of components
        val totalComponents = muscleMassInKg + boneMassInKg + bodyFatInKg + bodyWaterInKg

        // Step 4: Adjust weight if needed
        if (totalComponents > weightInKg) {
            weightInKg = (totalComponents + Random.nextDouble(0.5, 2.0)).roundToDecimals(2)
        }

        // Step 5: Calculate percentages based on final weight
        val bodyFatPercentage = (bodyFatInKg / weightInKg) * 100
        val bodyWaterPercentage = (bodyWaterInKg / weightInKg) * 100
        val bodyWeight = BodyWeightEntity(
            dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                i - 1,
                DateTimeUnit.DAY
            ).atStartOfDayIn(TimeZone.UTC).plus(12.hours),
            muscleMassInKg = muscleMassInKg,
            boneMassInKg = boneMassInKg,
            bodyFatPercentage = bodyFatPercentage.roundToDecimals(2),
            bodyWaterInPercentage = bodyWaterPercentage.roundToDecimals(2),
            weightInKg = weightInKg,
            profileId = profile.id
        )
        bodyWeightDao.upsertBodyWeight(bodyWeight)
    }
}