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
import org.darthacheron.fitbe.workouts.equipment.fromTrainingEquipmentEntity // Needed for DefaultTrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseEquipmentCrossRef // Correct import
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor
import org.darthacheron.fitbe.workouts.exercises.fromExerciseEntity // Needed for DefaultExerciseEntity
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateEntity
import org.darthacheron.fitbe.workouts.templates.fromWorkoutTemplateEntity // Needed for DefaultWorkoutTemplateEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    "default_training_equipment_yoga_mat",
    "default_training_equipment_jump_rope",
    "default_training_equipment_ab_wheel",
)

internal data class ExerciseSeedData(
    val key: String,
    val muscleGroups: List<MuscleGroup>,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType,
    val equipmentKeys: List<String> = emptyList() // Specifies which equipment this exercise uses by key
)

// Updated exerciseList with equipmentKeys and corrected MuscleGroup usage
internal val exerciseList: List<ExerciseSeedData> = listOf(
    ExerciseSeedData(
        key = "default_exercise_squat_bodyweight",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_squat_barbell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_squat_rack")
    ),
    ExerciseSeedData(
        key = "default_exercise_squat_dumbbell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_goblet_squat_kettlebell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_deadlift_barbell",
        muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_romanian_deadlift_dumbbell",
        muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK), // BACK_LOWER -> BACK
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_romanian_deadlift_barbell",
        muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_bench_press_barbell",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS), // SHOULDERS_FRONT -> SHOULDERS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_bench")
    ),
    ExerciseSeedData(
        key = "default_exercise_bench_press_dumbbell",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS), // SHOULDERS_FRONT -> SHOULDERS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell", "default_training_equipment_bench")
    ),
    ExerciseSeedData(
        key = "default_exercise_overhead_press_barbell",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_overhead_press_dumbbell",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_pull_ups",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS), // BACK_UPPER -> BACK
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_pull_up_bar")
    ),
    ExerciseSeedData(
        key = "default_exercise_push_ups_bodyweight",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS), // SHOULDERS_FRONT -> SHOULDERS, CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_push_ups_handles",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS), // SHOULDERS_FRONT -> SHOULDERS, CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_push_up_handles")
    ),
    ExerciseSeedData(
        key = "default_exercise_lunges_bodyweight",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_lunges_dumbbell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell")
    ),
     ExerciseSeedData(
        key = "default_exercise_lunges_barbell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_sit_ups",
        muscleGroups = listOf(MuscleGroup.ABS), // HIP_FLEXORS removed as secondary
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight", "default_training_equipment_yoga_mat") // yoga_mat can be primary here
    ),
    ExerciseSeedData(
        key = "default_exercise_plank",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS), // CORE -> ABS, BACK_LOWER -> BACK
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.TIMED,
        equipmentKeys = listOf("default_training_equipment_bodyweight", "default_training_equipment_yoga_mat") // yoga_mat can be primary here
    ),
    ExerciseSeedData(
        key = "default_exercise_jumping_jacks",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Warmup),
        exerciseType = ExerciseType.TIMED,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_side_lunges_bodyweight",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS), // ABDUCTORS removed
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_side_lunges_dumbbell",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS), // ABDUCTORS removed
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dumbbell")
    ),
    ExerciseSeedData(
        key = "default_exercise_burpees",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO, MuscleGroup.ABS), // CORE -> ABS
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    ExerciseSeedData(
        key = "default_exercise_mountain_climbers",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS, MuscleGroup.SHOULDERS, MuscleGroup.CARDIO), // CORE, HIP_FLEXORS removed/subsumed
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.TIMED,
        equipmentKeys = listOf("default_training_equipment_bodyweight")
    ),
    // Power Rack Exercises
    ExerciseSeedData(
        key = "default_exercise_rack_pulls_barbell_power_rack",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_power_rack")
    ),
    ExerciseSeedData(
        key = "default_exercise_squat_barbell_power_rack",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_power_rack")
    ),
    ExerciseSeedData(
        key = "default_exercise_bench_press_barbell_power_rack",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_bench", "default_training_equipment_power_rack")
    ),
    ExerciseSeedData(
        key = "default_exercise_overhead_press_barbell_power_rack",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_barbell", "default_training_equipment_power_rack")
    ),
    // Smith Machine Exercises
    ExerciseSeedData(
        key = "default_exercise_squat_smith_machine",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_smith_machine")
    ),
    ExerciseSeedData(
        key = "default_exercise_bench_press_smith_machine",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_smith_machine", "default_training_equipment_bench")
    ),
    ExerciseSeedData(
        key = "default_exercise_overhead_press_smith_machine",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_smith_machine")
    ),
    // Chest Press Machine Exercises
    ExerciseSeedData(
        key = "default_exercise_chest_press_machine",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_chest_press_machine")
    ),
    // Air Bike Exercises
    ExerciseSeedData(
        key = "default_exercise_air_bike_cardio",
        muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.FULL_BODY),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.TIMED, // Can also be DISTANCE_TIMED
        equipmentKeys = listOf("default_training_equipment_air_bike")
    ),
    // Treadmill Exercises
    ExerciseSeedData(
        key = "default_exercise_treadmill_running",
        muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.DISTANCE_TIMED, // Can also be TIMED
        equipmentKeys = listOf("default_training_equipment_treadmill")
    ),
    ExerciseSeedData(
        key = "default_exercise_treadmill_walking",
        muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.DISTANCE_TIMED, // Can also be TIMED
        equipmentKeys = listOf("default_training_equipment_treadmill")
    ),
    // Seated Row Machine Exercises
    ExerciseSeedData(
        key = "default_exercise_seated_row_machine",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_seated_row_machine")
    ),
    // Rowing Machine Exercises
    ExerciseSeedData(
        key = "default_exercise_rowing_machine_cardio",
        muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.FULL_BODY, MuscleGroup.BACK, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS, MuscleGroup.SHOULDERS, MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.DISTANCE_TIMED, // Can also be TIMED
        equipmentKeys = listOf("default_training_equipment_rowing_machine")
    ),
    // Battle Ropes Exercises
    ExerciseSeedData(
        key = "default_exercise_battle_ropes_waves",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.FOREARMS, MuscleGroup.ABS, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.TIMED,
        equipmentKeys = listOf("default_training_equipment_battle_ropes")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_swings",
        muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.ABS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_single_arm_rows",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_turkish_get_up",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.QUADS, MuscleGroup.GLUTES),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_halo",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.TRICEPS),
        recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_windmill",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_clean_and_press",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_snatch",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.SHOULDERS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_russian_twists",
        muscleGroups = listOf(MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_farmers_walk",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.FOREARMS, MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.QUADS, MuscleGroup.CALVES),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_TIMED,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_lunges",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    ExerciseSeedData(
        key = "default_exercise_kettlebell_deadlifts",
        muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_kettlebell")
    ),
    // EZ-Curl Bar Exercises
    ExerciseSeedData(
        key = "default_exercise_ez_bar_curl",
        muscleGroups = listOf(MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar")
    ),
    ExerciseSeedData(
        key = "default_exercise_ez_bar_preacher_curl",
        muscleGroups = listOf(MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar", "default_training_equipment_bench") // Assuming preacher curls might use a bench
    ),
    ExerciseSeedData(
        key = "default_exercise_ez_bar_reverse_curl",
        muscleGroups = listOf(MuscleGroup.FOREARMS, MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar")
    ),
    ExerciseSeedData(
        key = "default_exercise_ez_bar_skullcrusher",
        muscleGroups = listOf(MuscleGroup.TRICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar", "default_training_equipment_bench")
    ),
    ExerciseSeedData(
        key = "default_exercise_ez_bar_close_grip_bench_press",
        muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar", "default_training_equipment_bench")
    ),
    ExerciseSeedData(
        key = "default_exercise_ez_bar_upright_row",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.BACK), // Traps are part of back or shoulders depending on specificity
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ez_curl_bar")
    ),
    // ... (existing exerciseList entries) ...

    // Dip Station / Dip Bars Exercises
    ExerciseSeedData(
        key = "default_exercise_triceps_dips_dip_station",
        muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_dip_station")
    ),
    ExerciseSeedData(
        key = "default_exercise_weighted_dips_dip_station",
        muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_dip_station", "default_training_equipment_weight_plates")
    ),
    ExerciseSeedData(
        key = "default_exercise_dip_station_leg_raises",
        muscleGroups = listOf(MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_dip_station")
    ),
    // Weight Plates Exercises
    ExerciseSeedData(
        key = "default_exercise_plate_halos",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_weight_plates")
    ),
    ExerciseSeedData(
        key = "default_exercise_plate_front_raise",
        muscleGroups = listOf(MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_weight_plates")
    ),
    ExerciseSeedData(
        key = "default_exercise_weighted_plank_plate_on_back",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_TIMED,
        equipmentKeys = listOf("default_training_equipment_weight_plates", "default_training_equipment_bodyweight")
    ),
    // Ankle Weights Exercises
    ExerciseSeedData(
        key = "default_exercise_ankle_weight_leg_raises",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ankle_weights")
    ),
    ExerciseSeedData(
        key = "default_exercise_ankle_weight_glute_kickbacks",
        muscleGroups = listOf(MuscleGroup.GLUTES),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_ankle_weights")
    ),
    // Wrist Weights Exercises
    ExerciseSeedData(
        key = "default_exercise_wrist_weight_shadow_boxing",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
        exerciseType = ExerciseType.WEIGHT_TIMED,
        equipmentKeys = listOf("default_training_equipment_wrist_weights")
    ),
    ExerciseSeedData(
        key = "default_exercise_wrist_weight_arm_circles",
        muscleGroups = listOf(MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Warmup),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_wrist_weights")
    ),
    // Plyo Box Exercises
    ExerciseSeedData(
        key = "default_exercise_box_jumps",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.CALVES, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_plyo_box")
    ),
    ExerciseSeedData(
        key = "default_exercise_plyo_box_step_ups",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY, // Assuming bodyweight primarily, can be WEIGHT_REPS
        equipmentKeys = listOf("default_training_equipment_plyo_box")
    ),
    ExerciseSeedData(
        key = "default_exercise_decline_push_ups_feet_on_plyo_box",
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_plyo_box", "default_training_equipment_bodyweight")
    ),
    // Medicine Ball Exercises
    ExerciseSeedData(
        key = "default_exercise_medicine_ball_overhead_slams",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.ABS, MuscleGroup.SHOULDERS, MuscleGroup.BACK, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_medicine_ball")
    ),
    ExerciseSeedData(
        key = "default_exercise_medicine_ball_russian_twists",
        muscleGroups = listOf(MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_medicine_ball")
    ),
    ExerciseSeedData(
        key = "default_exercise_medicine_ball_woodchoppers",
        muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_medicine_ball")
    ),
    // Slam Ball Exercises
    ExerciseSeedData(
        key = "default_exercise_slam_ball_over_shoulder_toss",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.GLUTES, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_slam_ball")
    ),
    ExerciseSeedData(
        key = "default_exercise_slam_ball_ground_to_overhead",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_slam_ball")
    ),
    // Wall Ball Exercises
    ExerciseSeedData(
        key = "default_exercise_wall_ball_shots",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS, MuscleGroup.CARDIO),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_wall_ball")
    ),
    ExerciseSeedData(
        key = "default_exercise_wall_ball_squat_and_press_no_throw",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_wall_ball")
    ),
    // Sandbag Exercises
    ExerciseSeedData(
        key = "default_exercise_sandbag_bear_hug_squats",
        muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.BICEPS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_sandbag")
    ),
    ExerciseSeedData(
        key = "default_exercise_sandbag_cleans",
        muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_sandbag")
    ),
    ExerciseSeedData(
        key = "default_exercise_sandbag_shoulder_to_shoulder_press",
        muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.WEIGHT_REPS,
        equipmentKeys = listOf("default_training_equipment_sandbag")
    ),
    ExerciseSeedData(
        key = "default_exercise_assisted_pull_ups",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY, // Or WEIGHT_REPS if machine has weight assistance
        equipmentKeys = listOf("default_training_equipment_pull_up_bar", "default_training_equipment_resistance_bands") // Or a specific assisted pull-up machine key
    ),
    ExerciseSeedData(
        key = "default_exercise_negative_pull_ups",
        muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
        recommendedFor = listOf(RecommendedFor.Workout),
        exerciseType = ExerciseType.REPS_ONLY,
        equipmentKeys = listOf("default_training_equipment_pull_up_bar")
    ),
)

internal data class WorkoutTemplateSeedData(
    val key: String,
    val description: String? = null,
    val imageKey: String? = null
)

internal val workoutList: List<WorkoutTemplateSeedData> = listOf(
    // Example:
    // WorkoutTemplateSeedData(
    //     key = "default_workout_template_full_body_strength",
    //     description = "A full body strength workout.",
    //     imageKey = "default_workout_template_full_body_strength_image"
    // )
)

@OptIn(ExperimentalUuidApi::class)
class PrepopulateCallback(
    private val exerciseDaoProvider: () -> ExerciseDao,
    private val equipmentDaoProvider: () -> EquipmentDao,
    private val workoutTemplateDaoProvider: () -> WorkoutTemplateDao
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        applicationScope.launch {
            val exerciseDao = exerciseDaoProvider()
            val equipmentDao = equipmentDaoProvider()
            val workoutTemplateDao = workoutTemplateDaoProvider()

            populateDefaultEquipment(equipmentDao)
            // Fetch equipment after it has been populated to get their generated IDs
            val allDefaultEquipment = equipmentDao.getAllDefaultEquipmentSuspend()
            val equipmentMap = allDefaultEquipment.associateBy({ it.name }, { it.id })

            populateDefaultExercises(exerciseDao, equipmentMap)
            populateDefaultWorkoutTemplates(workoutTemplateDao)
        }
    }

    private suspend fun populateDefaultEquipment(equipmentDao: EquipmentDao) {
        equipmentList.forEach { equipmentKey ->
            // Note: Uuid.random() here means IDs will change on each app build if DB is new.
            // This is generally fine for default items if they are identified by key/name elsewhere.
            val equipmentId = Uuid.random() 
            val equipment = TrainingEquipmentEntity(
                id = equipmentId,
                name = equipmentKey,
                imageUri = equipmentKey, // Placeholder: assumes image key is same as name key
                default = true,
            )
            equipmentDao.upsertEquipment(equipment) // Upsert to user-editable table
            equipmentDao.insertDefaultEquipment( // Insert into default table for reference
                fromTrainingEquipmentEntity(equipment)
            )
        }
    }

    private suspend fun populateDefaultExercises(exerciseDao: ExerciseDao, equipmentMap: Map<String, Uuid>) {
        exerciseList.forEach { exerciseData ->
            val exerciseId = Uuid.random()
            val exercise = ExerciseEntity(
                id = exerciseId,
                name = exerciseData.key,
                guide = exerciseData.key, // Placeholder: guide key is same as name key
                imageUri = exerciseData.key, // Placeholder: image key is same as name key
                targetMuscleGroups = exerciseData.muscleGroups,
                recommendedFor = exerciseData.recommendedFor,
                exerciseType = exerciseData.exerciseType,
                default = true,
            )
            // Insert into user-editable exercises table first
            exerciseDao.upsertExercise(exercise)
            // Then insert into default_exercises table for reference
            exerciseDao.insertDefaultExercise(
                fromExerciseEntity(exercise)
            )

            // Prepare and insert cross-references for default exercise and its required equipment
            val crossRefs = exerciseData.equipmentKeys.mapNotNull { equipmentKey ->
                equipmentMap[equipmentKey]?.let { mappedEquipmentId ->
                    DefaultExerciseEquipmentCrossRef(exerciseId = exerciseId, equipmentId = mappedEquipmentId)
                }
            }
            if (crossRefs.isNotEmpty()) {
                exerciseDao.insertDefaultExerciseEquipmentCrossRefs(crossRefs)
                // Also insert into the user-editable cross-ref table
                exerciseDao.insertCrossRefs(crossRefs.map { ExerciseEquipmentCrossRef(exerciseId = exerciseId, equipmentId = it.equipmentId) })
            }
        }
    }

    private suspend fun populateDefaultWorkoutTemplates(workoutTemplateDao: WorkoutTemplateDao) {
        workoutList.forEach { workoutData ->
            val workoutTemplateId = Uuid.random()
            val workoutTemplate = WorkoutTemplateEntity(
                id = workoutTemplateId,
                name = workoutData.key,
                description = workoutData.description,
                imageUri = workoutData.imageKey,
                default = true
            )
            workoutTemplateDao.upsertWorkoutTemplate(workoutTemplate)
            workoutTemplateDao.insertDefaultWorkoutTemplate(
                fromWorkoutTemplateEntity(workoutTemplate)
            )
        }
    }
}
