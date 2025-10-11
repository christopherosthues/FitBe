package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.AB_WHEEL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.AIR_BIKE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.ANKLE_WEIGHTS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BACK_EXTENSION_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BALANCE_PAD
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BARBELL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BATTLE_ROPES
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BENCH
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BODYWEIGHT
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BOSU_BALL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.BULGARIAN_BAG
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.CABLE_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.CHEST_PRESS_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DIP_BARS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DIP_STATION
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DUMBBELL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.ELLIPTICAL_TRAINER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.EZ_CURL_BAR
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.FOAM_ROLLER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.GYMNASTIC_RINGS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.JUMP_ROPE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.KETTLEBELL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.LANDMINE_ATTACHMENT
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.LAT_PULLDOWN_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.LEG_CURL_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.LEG_EXTENSION_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.LEG_PRESS_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.MEDICINE_BALL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.PARALLETTES
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.PLYO_BOX
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.POWER_RACK
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.PULL_UP_BAR
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.PUSH_UP_HANDLES
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.RESISTANCE_BANDS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.ROWING_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SANDBAG
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SEATED_ROW_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SHOULDER_PRESS_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SLAM_BALL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SMITH_MACHINE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SPOTTER_ARMS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SQUAT_RACK
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.STAIR_CLIMBER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.STATIONARY_BIKE
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SUSPENSION_TRAINER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.TREADMILL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.WALL_BALL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.WEIGHT_PLATES
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.WRIST_WEIGHTS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.YOGA_MAT
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.toDefaultTrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor
import org.darthacheron.fitbe.workouts.exercises.toDefaultExerciseEntity
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal val equipmentList =
    listOf(
        AB_WHEEL,
        AIR_BIKE,
        ANKLE_WEIGHTS,
        BACK_EXTENSION_MACHINE,
        BALANCE_PAD,
        BARBELL,
        BATTLE_ROPES,
        BENCH,
        BODYWEIGHT,
        BOSU_BALL,
        BULGARIAN_BAG,
        CABLE_MACHINE,
        CHEST_PRESS_MACHINE,
        DIP_BARS,
        DIP_STATION,
        DUMBBELL,
        ELLIPTICAL_TRAINER,
        EZ_CURL_BAR,
        FOAM_ROLLER,
        GYMNASTIC_RINGS,
        JUMP_ROPE,
        KETTLEBELL,
        LANDMINE_ATTACHMENT,
        LAT_PULLDOWN_MACHINE,
        LEG_CURL_MACHINE,
        LEG_EXTENSION_MACHINE,
        LEG_PRESS_MACHINE,
        MEDICINE_BALL,
        PARALLETTES,
        PLYO_BOX,
        POWER_RACK,
        PULL_UP_BAR,
        PUSH_UP_HANDLES,
        RESISTANCE_BANDS,
        ROWING_MACHINE,
        SANDBAG,
        SEATED_ROW_MACHINE,
        SHOULDER_PRESS_MACHINE,
        SLAM_BALL,
        SMITH_MACHINE,
        SPOTTER_ARMS,
        SQUAT_RACK,
        STAIR_CLIMBER,
        STATIONARY_BIKE,
        SUSPENSION_TRAINER,
        TREADMILL,
        WALL_BALL,
        WEIGHT_PLATES,
        WRIST_WEIGHTS,
        YOGA_MAT,
    )

internal data class ExerciseSeedData(
    val key: String,
    val muscleGroups: List<MuscleGroup>,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType,
    val equipmentKeys: List<String> = emptyList()
)

internal val exerciseList: List<ExerciseSeedData> =
    listOf(
        ExerciseSeedData(
            key = "default_exercise_squat_bodyweight",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_squat_barbell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, SQUAT_RACK)
        ),
        ExerciseSeedData(
            key = "default_exercise_squat_dumbbell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_goblet_squat_kettlebell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_deadlift_barbell",
            muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_romanian_deadlift_dumbbell",
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_romanian_deadlift_barbell",
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_bench_press_barbell",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_bench_press_dumbbell",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_overhead_press_barbell",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_overhead_press_dumbbell",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_pull_ups",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_push_ups_bodyweight",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_push_ups_handles",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PUSH_UP_HANDLES)
        ),
        ExerciseSeedData(
            key = "default_exercise_lunges_bodyweight",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_lunges_dumbbell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_lunges_barbell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_sit_ups",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_plank",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_jumping_jacks",
            muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_side_lunges_bodyweight",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_side_lunges_dumbbell",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_burpees",
            muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_mountain_climbers",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS, MuscleGroup.SHOULDERS, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_rack_pulls_barbell_power_rack",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            key = "default_exercise_squat_barbell_power_rack",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            key = "default_exercise_bench_press_barbell_power_rack",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH, POWER_RACK)
        ),
        ExerciseSeedData(
            key = "default_exercise_overhead_press_barbell_power_rack",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            key = "default_exercise_squat_smith_machine",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_bench_press_smith_machine",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_overhead_press_smith_machine",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_chest_press_machine",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CHEST_PRESS_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_air_bike_cardio",
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(AIR_BIKE)
        ),
        ExerciseSeedData(
            key = "default_exercise_treadmill_running",
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.DISTANCE_TIMED,
            equipmentKeys = listOf(TREADMILL)
        ),
        ExerciseSeedData(
            key = "default_exercise_treadmill_walking",
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.DISTANCE_TIMED,
            equipmentKeys = listOf(TREADMILL)
        ),
        ExerciseSeedData(
            key = "default_exercise_seated_row_machine",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SEATED_ROW_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_rowing_machine_cardio",
            muscleGroups =
                listOf(
                    MuscleGroup.CARDIO,
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.BACK,
                    MuscleGroup.QUADS,
                    MuscleGroup.HAMSTRINGS,
                    MuscleGroup.ABS,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.BICEPS
                ),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.DISTANCE_TIMED,
            equipmentKeys = listOf(ROWING_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_battle_ropes_waves",
            muscleGroups =
                listOf(
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.BICEPS,
                    MuscleGroup.TRICEPS,
                    MuscleGroup.FOREARMS,
                    MuscleGroup.ABS,
                    MuscleGroup.CARDIO
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BATTLE_ROPES)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_swings",
            muscleGroups =
                listOf(
                    MuscleGroup.GLUTES,
                    MuscleGroup.HAMSTRINGS,
                    MuscleGroup.BACK,
                    MuscleGroup.ABS,
                    MuscleGroup.SHOULDERS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_single_arm_rows",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_turkish_get_up",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.ABS,
                    MuscleGroup.QUADS,
                    MuscleGroup.GLUTES
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_halo",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_windmill",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_clean_and_press",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.TRICEPS,
                    MuscleGroup.QUADS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.ABS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_snatch",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.HAMSTRINGS,
                    MuscleGroup.BACK,
                    MuscleGroup.ABS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_russian_twists",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_farmers_walk",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.FOREARMS,
                    MuscleGroup.ABS,
                    MuscleGroup.BACK,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.QUADS,
                    MuscleGroup.CALVES
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_TIMED,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_lunges",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_kettlebell_deadlifts",
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_curl",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_preacher_curl",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_reverse_curl",
            muscleGroups = listOf(MuscleGroup.FOREARMS, MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_skullcrusher",
            muscleGroups = listOf(MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_close_grip_bench_press",
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_ez_bar_upright_row",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_triceps_dips_dip_station",
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            key = "default_exercise_weighted_dips_dip_station",
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DIP_STATION, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            key = "default_exercise_dip_station_leg_raises",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            key = "default_exercise_plate_halos",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            key = "default_exercise_plate_front_raise",
            muscleGroups = listOf(MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            key = "default_exercise_weighted_plank_plate_on_back",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_TIMED,
            equipmentKeys = listOf(WEIGHT_PLATES, BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_ankle_weight_leg_raises",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS)
        ),
        ExerciseSeedData(
            key = "default_exercise_ankle_weight_glute_kickbacks",
            muscleGroups = listOf(MuscleGroup.GLUTES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS)
        ),
        ExerciseSeedData(
            key = "default_exercise_wrist_weight_shadow_boxing",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_TIMED,
            equipmentKeys = listOf(WRIST_WEIGHTS)
        ),
        ExerciseSeedData(
            key = "default_exercise_wrist_weight_arm_circles",
            muscleGroups = listOf(MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WRIST_WEIGHTS)
        ),
        ExerciseSeedData(
            key = "default_exercise_box_jumps",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.CALVES, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            key = "default_exercise_plyo_box_step_ups",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            key = "default_exercise_decline_push_ups_feet_on_plyo_box",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX, BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_medicine_ball_overhead_slams",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.ABS,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.BACK,
                    MuscleGroup.CARDIO
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(MEDICINE_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_medicine_ball_russian_twists",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(MEDICINE_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_medicine_ball_woodchoppers",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(MEDICINE_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_slam_ball_over_shoulder_toss",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.GLUTES,
                    MuscleGroup.BACK,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.ABS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SLAM_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_slam_ball_ground_to_overhead",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.QUADS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.ABS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SLAM_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_wall_ball_shots",
            muscleGroups =
                listOf(
                    MuscleGroup.QUADS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.TRICEPS,
                    MuscleGroup.ABS,
                    MuscleGroup.CARDIO
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WALL_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_wall_ball_squat_and_press_no_throw",
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WALL_BALL)
        ),
        ExerciseSeedData(
            key = "default_exercise_sandbag_bear_hug_squats",
            muscleGroups =
                listOf(
                    MuscleGroup.QUADS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.ABS,
                    MuscleGroup.BACK,
                    MuscleGroup.BICEPS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SANDBAG)
        ),
        ExerciseSeedData(
            key = "default_exercise_sandbag_cleans",
            muscleGroups =
                listOf(
                    MuscleGroup.FULL_BODY,
                    MuscleGroup.GLUTES,
                    MuscleGroup.HAMSTRINGS,
                    MuscleGroup.BACK,
                    MuscleGroup.SHOULDERS,
                    MuscleGroup.ABS
                ),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SANDBAG)
        ),
        ExerciseSeedData(
            key = "default_exercise_sandbag_shoulder_to_shoulder_press",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SANDBAG)
        ),
        ExerciseSeedData(
            key = "default_exercise_assisted_pull_ups",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR, RESISTANCE_BANDS)
        ),
        ExerciseSeedData(
            key = "default_exercise_negative_pull_ups",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_crunches_bodyweight",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_crunches_weighted_plate",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_decline_crunches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_lying_leg_raises_bodyweight",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_lying_leg_raises_dumbbell_between_feet",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_flutter_kicks",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_bicycle_crunches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_heel_touches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_reverse_crunches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_incline_reverse_crunches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BENCH, BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_hanging_leg_raises",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_hanging_knee_raises",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            key = "default_exercise_captains_chair_leg_raises",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            key = "default_exercise_cable_crunches_kneeling",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_cable_crunches_standing",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_dumbbell_side_bends",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_sit_ups_weighted_plate",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES, BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_sit_ups_weighted_dumbbell",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_woodchoppers_dumbbell_high_to_low",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_woodchoppers_cable_high_to_low",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_woodchoppers_kettlebell_high_to_low",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_landmine_twists_attachment",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, LANDMINE_ATTACHMENT, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            key = "default_exercise_landmine_twists_corner",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            key = "default_exercise_ab_wheel_rollouts_knees",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(AB_WHEEL, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_ab_wheel_rollouts_standing",
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(AB_WHEEL)
        ),
        ExerciseSeedData(
            key = "default_exercise_ankle_weight_reverse_crunches",
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS, YOGA_MAT)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_bodyweight_standing",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_dumbbell_standing",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_barbell_standing",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_smith_machine_standing",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_bodyweight_seated",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_dumbbell_seated",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_single_leg_bodyweight",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_calf_raises_single_leg_dumbbell",
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BODYWEIGHT)
        ),
        ExerciseSeedData(
            key = "default_exercise_box_jumps_calf_focus",
            muscleGroups = listOf(MuscleGroup.CALVES, MuscleGroup.GLUTES, MuscleGroup.QUADS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            key = "default_exercise_jump_rope",
            muscleGroups = listOf(MuscleGroup.CALVES, MuscleGroup.CARDIO, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(JUMP_ROPE)
        ),
        ExerciseSeedData(
            key = "default_exercise_incline_bench_press_barbell",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_incline_bench_press_dumbbell",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_around_the_world_dumbbell",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            key = "default_exercise_around_the_world_plate",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        )
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
        }
    }

    private suspend fun populateDefaultEquipment(equipmentDao: EquipmentDao) {
        equipmentList.forEach { equipmentKey ->
            val equipmentId = Uuid.random()
            val equipment =
                TrainingEquipmentEntity(
                    id = equipmentId,
                    name = equipmentKey,
                    imageUri = equipmentKey,
                    default = true
                )
            equipmentDao.upsertEquipment(equipment)
            equipmentDao.insertDefaultEquipment(
                equipment.toDefaultTrainingEquipmentEntity()
            )
        }
    }

    private suspend fun populateDefaultExercises(
        exerciseDao: ExerciseDao,
        equipmentMap: Map<String, Uuid>
    ) {
        exerciseList.forEach { exerciseData ->
            val exerciseId = Uuid.random()
            val exercise =
                ExerciseEntity(
                    id = exerciseId,
                    name = exerciseData.key,
                    guide = exerciseData.key,
                    imageUri = exerciseData.key,
                    targetMuscleGroups = exerciseData.muscleGroups,
                    recommendedFor = exerciseData.recommendedFor,
                    exerciseType = exerciseData.exerciseType,
                    default = true
                )
            exerciseDao.upsertExercise(exercise)
            exerciseDao.insertDefaultExercise(
                exercise.toDefaultExerciseEntity()
            )

            val crossRefs =
                exerciseData.equipmentKeys.mapNotNull { equipmentKey ->
                    equipmentMap[equipmentKey]?.let { mappedEquipmentId ->
                        DefaultExerciseEquipmentCrossRef(exerciseId = exerciseId, equipmentId = mappedEquipmentId)
                    }
                }
            if (crossRefs.isNotEmpty()) {
                exerciseDao.insertDefaultExerciseEquipmentCrossRefs(crossRefs)
                exerciseDao.insertCrossRefs(
                    crossRefs.map {
                        ExerciseEquipmentCrossRef(exerciseId = exerciseId, equipmentId = it.equipmentId)
                    }
                )
            }
        }
    }
}