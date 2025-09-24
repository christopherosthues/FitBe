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
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.database.converters.ExerciseTypeConverter
import org.darthacheron.fitbe.database.converters.FluidUnitConverter
import org.darthacheron.fitbe.database.converters.GenderConverter
import org.darthacheron.fitbe.database.converters.InstantConverter
import org.darthacheron.fitbe.database.converters.LocalDateConverter
import org.darthacheron.fitbe.database.converters.LocalDateTimeConverter
import org.darthacheron.fitbe.database.converters.LocalTimeConverter
import org.darthacheron.fitbe.database.converters.MuscleGroupListConverter
import org.darthacheron.fitbe.database.converters.RecommendedForListConverter
import org.darthacheron.fitbe.database.converters.UuidConverter
import org.darthacheron.fitbe.database.converters.WorkoutExecutionStatusConverter
import org.darthacheron.fitbe.database.converters.WorkoutSetStatusConverter
import org.darthacheron.fitbe.workouts.equipment.DefaultTrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.ProfileFavoriteExerciseCrossRef
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.ProfileFavoriteEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseEquipmentCrossRef
import org.darthacheron.fitbe.health.beverages.BeverageDao
import org.darthacheron.fitbe.health.beverages.BeverageEntity
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.sleep.SleepDao
import org.darthacheron.fitbe.health.sleep.SleepEntity
import org.darthacheron.fitbe.health.steps.StepsDao
import org.darthacheron.fitbe.health.steps.StepsEntity
import org.darthacheron.fitbe.health.weight.BodyWeightDao
import org.darthacheron.fitbe.health.weight.BodyWeightEntity
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileEntity
import org.darthacheron.fitbe.workouts.templates.DefaultWorkoutTemplateEntity
import org.darthacheron.fitbe.workouts.templates.ProfileFavoriteWorkoutTemplateCrossRef
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateEntity
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import org.darthacheron.fitbe.utils.roundToDecimals
import org.darthacheron.fitbe.workouts.programs.ProgramDao
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionDao
import org.darthacheron.fitbe.workouts.workouts.WorkoutExecutionEntity
import org.darthacheron.fitbe.workouts.workouts.WorkoutSetExecutionEntity
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@Database(
    entities = [
        BeverageEntity::class,
        BodyWeightEntity::class,
        DefaultExerciseEntity::class,
        DefaultExerciseEquipmentCrossRef::class,
        DefaultTrainingEquipmentEntity::class,
        DefaultWorkoutTemplateEntity::class,
        ExerciseEntity::class,
        ExerciseEquipmentCrossRef::class,
        ProfileEntity::class,
        ProfileFavoriteEquipmentCrossRef::class,
        ProfileFavoriteExerciseCrossRef::class,
        ProfileFavoriteWorkoutTemplateCrossRef::class,
        SleepEntity::class,
        StepsEntity::class,
        TrainingEquipmentEntity::class,
        WorkoutExecutionEntity::class,
        WorkoutSetExecutionEntity::class,
        WorkoutTemplateEntity::class,
    ],
    version = 1
)
@TypeConverters(
    ExerciseTypeConverter::class,
    FluidUnitConverter::class,
    GenderConverter::class,
    InstantConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    LocalTimeConverter::class,
    MuscleGroupListConverter::class,
    RecommendedForListConverter::class,
    UuidConverter::class,
    WorkoutExecutionStatusConverter::class,
    WorkoutSetStatusConverter::class,
)
@ConstructedBy(FitBeDatabaseConstructor::class)
abstract class FitBeDatabase : RoomDatabase() {
    abstract val beverageDao: BeverageDao
    abstract val sleepDao: SleepDao
    abstract val bodyWeightDao: BodyWeightDao
    abstract val profileDao: ProfileDao
    abstract val stepsDao: StepsDao
    abstract val exerciseDao: ExerciseDao
    abstract val equipmentDao: EquipmentDao
    abstract val workoutTemplateDao: WorkoutTemplateDao
    abstract val workoutExecutionDao: WorkoutExecutionDao
    abstract val programDao: ProgramDao

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

    val profile = profileDao.getAllProfiles().first().firstOrNull() ?: run {
        println("Warning: No profile found to associate with seed data.")
        return
    }

    for (i in 1..730) {
        val sleep = SleepEntity(
            dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.minus(
                i - 1,
                DateTimeUnit.DAY
            ),
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

        var weightInKg = Random.nextDouble(55.0, 130.0).roundToDecimals(2)
        val muscleMassInKg = Random.nextDouble(20.0, weightInKg * 0.6).roundToDecimals(2)
        val boneMassInKg = Random.nextDouble(2.0, 4.0).roundToDecimals(2)
        val bodyFatInKg = Random.nextDouble(10.0, weightInKg * 0.3).roundToDecimals(2)
        val bodyWaterInKg = Random.nextDouble(25.0, weightInKg * 0.65).roundToDecimals(2)
        val totalComponents = muscleMassInKg + boneMassInKg + bodyFatInKg + bodyWaterInKg
        if (totalComponents > weightInKg) {
            weightInKg = (totalComponents + Random.nextDouble(0.5, 2.0)).roundToDecimals(2)
        }
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
