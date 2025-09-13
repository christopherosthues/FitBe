package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.fromTrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor
import org.darthacheron.fitbe.workouts.exercises.fromExerciseEntity
import kotlin.uuid.ExperimentalUuidApi

internal val equipmentList = listOf(
    "default_training_equipment_barbell",
    "default_training_equipment_dumbbell",
    "default_training_equipment_bodyweight",
    "default_training_equipment_kettlebell",
    "default_training_equipment_dip_station",
    "default_training_equipment_bench",
    "default_training_equipment_rowing_machine",
    "default_training_equipment_chest_press_machine",
    "default_training_equipment_ez_curl_bar",
    "default_training_equipment_weight_plates",
    "default_training_equipment_ankle_weights",
    "default_training_equipment_wrist_weights",
    "default_training_equipment_cable_machine",
    "default_training_equipment_lat_pulldown_machine",
    "default_training_equipment_leg_press_machine",
    "default_training_equipment_leg_extension_machine",
    "default_training_equipment_leg_curl_machine",
    "default_training_equipment_smith_machine",
    "default_training_equipment_shoulder_press_machine",
    "default_training_equipment_seated_row_machine",
    "default_training_equipment_pull_up_bar",
    "default_training_equipment_dip_bars",
    "default_training_equipment_push_up_handles",
    "default_training_equipment_parallettes",
    "default_training_equipment_gymnastic_rings",
    "default_training_equipment_suspension_trainer",
    "default_training_equipment_plyo_box",
    "default_training_equipment_medicine_ball",
    "default_training_equipment_slam_ball",
    "default_training_equipment_wall_ball",
    "default_training_equipment_battle_ropes",
    "default_training_equipment_sandbag",
    "default_training_equipment_bulgarian_bag",
    "default_training_equipment_resistance_bands",
    "default_training_equipment_foam_roller",
    "default_training_equipment_balance_pad",
    "default_training_equipment_bosu_ball",
    "default_training_equipment_treadmill",
    "default_training_equipment_elliptical_trainer",
    "default_training_equipment_stationary_bike",
    "default_training_equipment_stair_climber",
    "default_training_equipment_air_bike",
    "default_training_equipment_power_rack",
    "default_training_equipment_squat_rack",
    "default_training_equipment_spotter_arms",
)

internal data class ExerciseSeedData(
    val key: String,
    val muscleGroups: List<MuscleGroup>,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType
)

internal val exerciseList: List<ExerciseSeedData> = listOf(
    ExerciseSeedData(
        key = "default_exercise_squat",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS
    ),
    ExerciseSeedData(
        key = "default_exercise_deadlift",
        muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS
    ),
    ExerciseSeedData(
        key = "default_exercise_bench_press",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS
    ),
    ExerciseSeedData(
        key = "default_exercise_overhead_press",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS
    ),
    ExerciseSeedData(
        key = "default_exercise_pull_ups",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_push_ups",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_lunges",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_sit_ups",
        muscleGroups = listOf(MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_plank",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.TIMED
    ),
    ExerciseSeedData(
        key = "default_exercise_jumping_jacks",
        muscleGroups = listOf(MuscleGroup.FULL_BODY),
        recommendedFor = listOf(RecommendedFor.Warmup),
        exerciseType = ExerciseType.TIMED
    ),
    ExerciseSeedData(
        key = "default_exercise_side_lunges",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_burpees",
        muscleGroups = listOf(MuscleGroup.FULL_BODY),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY
    ),
    ExerciseSeedData(
        key = "default_exercise_mountain_climbers",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.TIMED
    )
)

@OptIn(ExperimentalUuidApi::class)
class PrepopulateCallback(
    private val exerciseDaoProvider: () -> ExerciseDao,
    private val equipmentDaoProvider: () -> EquipmentDao,
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        applicationScope.launch {
            val exerciseDao = exerciseDaoProvider()
            val equipmentDao = equipmentDaoProvider()
            populateDefaultEquipment(equipmentDao)
            populateDefaultExercises(exerciseDao)
        }
    }

    private suspend fun populateDefaultEquipment(equipmentDao: EquipmentDao) {
        equipmentList.forEach { equipmentKey ->
            val equipment = TrainingEquipmentEntity(
                name = equipmentKey,
                imageUri = equipmentKey,
                default = true,
            )
            equipmentDao.upsertEquipment(equipment)
            equipmentDao.insertDefaultEquipment(
                fromTrainingEquipmentEntity(equipment)
            )
        }
    }

    private suspend fun populateDefaultExercises(exerciseDao: ExerciseDao) {
        exerciseList.forEach { exerciseData ->
            val exercise = ExerciseEntity(
                name = exerciseData.key,
                guide = exerciseData.key, // Assuming guide key is same as name key
                imageUri = exerciseData.key, // Assuming image key is same as name key
                targetMuscleGroups = exerciseData.muscleGroups,
                recommendedFor = exerciseData.recommendedFor,
                exerciseType = exerciseData.exerciseType,
                default = true,
            )
            exerciseDao.upsertExercise(exercise)
            exerciseDao.insertDefaultExercise(
                fromExerciseEntity(exercise)
            )
        }
    }
}
