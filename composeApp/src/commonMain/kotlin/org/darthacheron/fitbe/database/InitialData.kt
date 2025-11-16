package org.darthacheron.fitbe.database

import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider
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
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DIP_BARS
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DIP_STATION
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.DUMBBELL
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.ELLIPTICAL_TRAINER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.EZ_CURL_BAR
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.FOAM_ROLLER
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.GYMNASTIC_RINGS
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
import org.darthacheron.fitbe.workouts.equipment.DefaultEquipmentResProvider.SEATED_BACK_EXTENSION_MACHINE
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
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.AB_WHEEL_ROLLOUTS_KNEES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.AB_WHEEL_ROLLOUTS_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.AIR_BIKE_CARDIO
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ANKLE_WEIGHT_GLUTE_KICKBACKS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ANKLE_WEIGHT_LEG_RAISES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ANKLE_WEIGHT_REVERSE_CRUNCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.AROUND_THE_WORLD_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.AROUND_THE_WORLD_PLATE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ASSISTED_PULL_UPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BATTLE_ROPES_WAVES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BENCH_PRESS_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BENCH_PRESS_BARBELL_POWER_RACK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BENCH_PRESS_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BENCH_PRESS_SMITH_MACHINE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BICYCLE_CRUNCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BOX_JUMPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BOX_JUMPS_CALF_FOCUS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.BURPEES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CABLE_CRUNCHES_KNEELING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CABLE_CRUNCHES_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_BARBELL_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_BODYWEIGHT_SEATED
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_BODYWEIGHT_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_DUMBBELL_SEATED
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_DUMBBELL_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_SINGLE_LEG_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_SINGLE_LEG_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CALF_RAISES_SMITH_MACHINE_STANDING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CAPTAINS_CHAIR_LEG_RAISES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CRUNCHES_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.CRUNCHES_WEIGHTED_PLATE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.DEADLIFT_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.DECLINE_CRUNCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.DIP_STATION_LEG_RAISES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.DUMBBELL_SIDE_BENDS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_CLOSE_GRIP_BENCH_PRESS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_CURL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_PREACHER_CURL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_REVERSE_CURL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_SKULLCRUSHER
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.EZ_BAR_UPRIGHT_ROW
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.FLUTTER_KICKS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.GOBLET_SQUAT_KETTLEBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.HANGING_KNEE_RAISES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.HANGING_LEG_RAISES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.HEEL_TOUCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.INCLINE_BENCH_PRESS_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.INCLINE_BENCH_PRESS_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.INCLINE_REVERSE_CRUNCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.JUMPING_JACKS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_CLEAN_AND_PRESS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_DEADLIFTS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_FARMERS_WALK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_HALO
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_LUNGES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_RUSSIAN_TWISTS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_SINGLE_ARM_ROWS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_SNATCH
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_SWINGS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_TURKISH_GET_UP
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.KETTLEBELL_WINDMILL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LANDMINE_TWISTS_ATTACHMENT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LANDMINE_TWISTS_CORNER
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LUNGES_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LUNGES_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LUNGES_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LYING_LEG_RAISES_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.MEDICINE_BALL_OVERHEAD_SLAMS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.MEDICINE_BALL_RUSSIAN_TWISTS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.MEDICINE_BALL_WOODCHOPPERS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.MOUNTAIN_CLIMBERS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.NEGATIVE_PULL_UPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.OVERHEAD_PRESS_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.OVERHEAD_PRESS_BARBELL_POWER_RACK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.OVERHEAD_PRESS_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.OVERHEAD_PRESS_SMITH_MACHINE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PLANK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PLATE_FRONT_RAISE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PLATE_HALOS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PLYO_BOX_STEP_UPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PULL_UPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PUSH_UPS_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.PUSH_UPS_HANDLES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.RACK_PULLS_BARBELL_POWER_RACK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.REVERSE_CRUNCHES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ROMANIAN_DEADLIFT_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ROMANIAN_DEADLIFT_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.ROWING_MACHINE_CARDIO
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SANDBAG_BEAR_HUG_SQUATS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SANDBAG_CLEANS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SANDBAG_SHOULDER_TO_SHOULDER_PRESS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SIDE_LUNGES_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SIDE_LUNGES_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SIT_UPS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SIT_UPS_WEIGHTED_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SIT_UPS_WEIGHTED_PLATE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SLAM_BALL_GROUND_TO_OVERHEAD
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SLAM_BALL_OVER_SHOULDER_TOSS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SQUAT_BARBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SQUAT_BARBELL_POWER_RACK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SQUAT_BODYWEIGHT
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SQUAT_DUMBBELL
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.SQUAT_SMITH_MACHINE
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.TREADMILL_RUNNING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.TREADMILL_WALKING
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.TRICEPS_DIPS_DIP_STATION
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WALL_BALL_SHOTS
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WALL_BALL_SQUAT_AND_PRESS_NO_THROW
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WEIGHTED_DIPS_DIP_STATION
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WEIGHTED_PLANK_PLATE_ON_BACK
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WOODCHOPPERS_CABLE_HIGH_TO_LOW
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WRIST_WEIGHT_ARM_CIRCLES
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseResProvider.WRIST_WEIGHT_SHADOW_BOXING
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.darthacheron.fitbe.workouts.exercises.MuscleGroup
import org.darthacheron.fitbe.workouts.exercises.RecommendedFor
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal data class EquipmentSeedData(
    val id: Uuid,
    val key: String
)

@OptIn(ExperimentalUuidApi::class)
internal data class ExerciseSeedData(
    val id: Uuid,
    val key: String,
    val muscleGroups: List<MuscleGroup>,
    val recommendedFor: List<RecommendedFor>,
    val exerciseType: ExerciseType,
    val equipmentKeys: List<String> = emptyList()
)

@OptIn(ExperimentalUuidApi::class)
internal val equipmentList =
    listOf(
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000001"), key = AB_WHEEL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000002"), key = AIR_BIKE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000003"), key = ANKLE_WEIGHTS),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000004"), key = BACK_EXTENSION_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000005"), key = BALANCE_PAD),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000006"), key = BARBELL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000007"), key = BATTLE_ROPES),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000008"), key = BENCH),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000009"), key = BODYWEIGHT),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000010"), key = BOSU_BALL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000011"), key = BULGARIAN_BAG),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000012"), key = CABLE_MACHINE),
        EquipmentSeedData(
            id = Uuid.parse("fb100000-aee0-1611-2025-000000000013"),
            key = DefaultEquipmentResProvider.CHEST_PRESS_MACHINE
        ),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000014"), key = DIP_BARS),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000015"), key = DIP_STATION),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000016"), key = DUMBBELL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000017"), key = ELLIPTICAL_TRAINER),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000018"), key = EZ_CURL_BAR),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000019"), key = FOAM_ROLLER),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000020"), key = GYMNASTIC_RINGS),
        EquipmentSeedData(
            id = Uuid.parse("fb100000-aee0-1611-2025-000000000021"),
            key = DefaultEquipmentResProvider.JUMP_ROPE
        ),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000022"), key = KETTLEBELL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000023"), key = LANDMINE_ATTACHMENT),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000024"), key = LAT_PULLDOWN_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000025"), key = LEG_CURL_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000026"), key = LEG_EXTENSION_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000027"), key = LEG_PRESS_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000028"), key = MEDICINE_BALL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000029"), key = PARALLETTES),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000030"), key = PLYO_BOX),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000031"), key = POWER_RACK),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000032"), key = PULL_UP_BAR),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000033"), key = PUSH_UP_HANDLES),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000034"), key = RESISTANCE_BANDS),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000035"), key = ROWING_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000036"), key = SANDBAG),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000037"), key = SEATED_BACK_EXTENSION_MACHINE),
        EquipmentSeedData(
            id = Uuid.parse("fb100000-aee0-1611-2025-000000000038"),
            key = DefaultEquipmentResProvider.SEATED_ROW_MACHINE
        ),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000039"), key = SHOULDER_PRESS_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000040"), key = SLAM_BALL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000041"), key = SMITH_MACHINE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000042"), key = SPOTTER_ARMS),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000043"), key = SQUAT_RACK),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000044"), key = STAIR_CLIMBER),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000045"), key = STATIONARY_BIKE),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000046"), key = SUSPENSION_TRAINER),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000047"), key = TREADMILL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000048"), key = WALL_BALL),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000049"), key = WEIGHT_PLATES),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000050"), key = WRIST_WEIGHTS),
        EquipmentSeedData(id = Uuid.parse("fb100000-aee0-1611-2025-000000000051"), key = YOGA_MAT),
    )

@OptIn(ExperimentalUuidApi::class)
internal val exerciseList: List<ExerciseSeedData> =
    listOf(
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000001"),
            key = SQUAT_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000002"),
            key = SQUAT_BARBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, SQUAT_RACK)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000003"),
            key = SQUAT_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000004"),
            key = GOBLET_SQUAT_KETTLEBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000005"),
            key = DEADLIFT_BARBELL,
            muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.BACK, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000006"),
            key = ROMANIAN_DEADLIFT_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000007"),
            key = ROMANIAN_DEADLIFT_BARBELL,
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000008"),
            key = BENCH_PRESS_BARBELL,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000009"),
            key = BENCH_PRESS_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000010"),
            key = OVERHEAD_PRESS_BARBELL,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000011"),
            key = OVERHEAD_PRESS_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000012"),
            key = PULL_UPS,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000013"),
            key = PUSH_UPS_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000014"),
            key = PUSH_UPS_HANDLES,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PUSH_UP_HANDLES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000015"),
            key = LUNGES_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000016"),
            key = LUNGES_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000017"),
            key = LUNGES_BARBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000018"),
            key = SIT_UPS,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000019"),
            key = PLANK,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000020"),
            key = JUMPING_JACKS,
            muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000021"),
            key = SIDE_LUNGES_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000022"),
            key = SIDE_LUNGES_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ADDUCTORS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000023"),
            key = BURPEES,
            muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000024"),
            key = MOUNTAIN_CLIMBERS,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS, MuscleGroup.SHOULDERS, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000025"),
            key = RACK_PULLS_BARBELL_POWER_RACK,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000026"),
            key = SQUAT_BARBELL_POWER_RACK,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000027"),
            key = BENCH_PRESS_BARBELL_POWER_RACK,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH, POWER_RACK)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000028"),
            key = OVERHEAD_PRESS_BARBELL_POWER_RACK,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, POWER_RACK)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000029"),
            key = SQUAT_SMITH_MACHINE,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000030"),
            key = BENCH_PRESS_SMITH_MACHINE,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000031"),
            key = OVERHEAD_PRESS_SMITH_MACHINE,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000032"),
            key = DefaultExerciseResProvider.CHEST_PRESS_MACHINE,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DefaultEquipmentResProvider.CHEST_PRESS_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000033"),
            key = AIR_BIKE_CARDIO,
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(AIR_BIKE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000034"),
            key = TREADMILL_RUNNING,
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.DISTANCE_TIMED,
            equipmentKeys = listOf(TREADMILL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000035"),
            key = TREADMILL_WALKING,
            muscleGroups = listOf(MuscleGroup.CARDIO, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.DISTANCE_TIMED,
            equipmentKeys = listOf(TREADMILL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000036"),
            key = DefaultExerciseResProvider.SEATED_ROW_MACHINE,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DefaultEquipmentResProvider.SEATED_ROW_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000037"),
            key = ROWING_MACHINE_CARDIO,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000038"),
            key = BATTLE_ROPES_WAVES,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000039"),
            key = KETTLEBELL_SWINGS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000040"),
            key = KETTLEBELL_SINGLE_ARM_ROWS,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000041"),
            key = KETTLEBELL_TURKISH_GET_UP,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000042"),
            key = KETTLEBELL_HALO,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000043"),
            key = KETTLEBELL_WINDMILL,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000044"),
            key = KETTLEBELL_CLEAN_AND_PRESS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000045"),
            key = KETTLEBELL_SNATCH,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000046"),
            key = KETTLEBELL_RUSSIAN_TWISTS,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000047"),
            key = KETTLEBELL_FARMERS_WALK,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000048"),
            key = KETTLEBELL_LUNGES,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000049"),
            key = KETTLEBELL_DEADLIFTS,
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.BACK, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000050"),
            key = EZ_BAR_CURL,
            muscleGroups = listOf(MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000051"),
            key = EZ_BAR_PREACHER_CURL,
            muscleGroups = listOf(MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000052"),
            key = EZ_BAR_REVERSE_CURL,
            muscleGroups = listOf(MuscleGroup.FOREARMS, MuscleGroup.BICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000053"),
            key = EZ_BAR_SKULLCRUSHER,
            muscleGroups = listOf(MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000054"),
            key = EZ_BAR_CLOSE_GRIP_BENCH_PRESS,
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000055"),
            key = EZ_BAR_UPRIGHT_ROW,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(EZ_CURL_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000056"),
            key = TRICEPS_DIPS_DIP_STATION,
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000057"),
            key = WEIGHTED_DIPS_DIP_STATION,
            muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DIP_STATION, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000058"),
            key = DIP_STATION_LEG_RAISES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000059"),
            key = PLATE_HALOS,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000060"),
            key = PLATE_FRONT_RAISE,
            muscleGroups = listOf(MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000061"),
            key = WEIGHTED_PLANK_PLATE_ON_BACK,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_TIMED,
            equipmentKeys = listOf(WEIGHT_PLATES, BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000062"),
            key = ANKLE_WEIGHT_LEG_RAISES,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.QUADS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000063"),
            key = ANKLE_WEIGHT_GLUTE_KICKBACKS,
            muscleGroups = listOf(MuscleGroup.GLUTES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000064"),
            key = WRIST_WEIGHT_SHADOW_BOXING,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_TIMED,
            equipmentKeys = listOf(WRIST_WEIGHTS)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000065"),
            key = WRIST_WEIGHT_ARM_CIRCLES,
            muscleGroups = listOf(MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Warmup),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WRIST_WEIGHTS)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000066"),
            key = BOX_JUMPS,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.CALVES, MuscleGroup.CARDIO),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000067"),
            key = PLYO_BOX_STEP_UPS,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000068"),
            key = DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX, BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000069"),
            key = MEDICINE_BALL_OVERHEAD_SLAMS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000070"),
            key = MEDICINE_BALL_RUSSIAN_TWISTS,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(MEDICINE_BALL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000071"),
            key = MEDICINE_BALL_WOODCHOPPERS,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(MEDICINE_BALL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000072"),
            key = SLAM_BALL_OVER_SHOULDER_TOSS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000073"),
            key = SLAM_BALL_GROUND_TO_OVERHEAD,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000074"),
            key = WALL_BALL_SHOTS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000075"),
            key = WALL_BALL_SQUAT_AND_PRESS_NO_THROW,
            muscleGroups = listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES, MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WALL_BALL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000076"),
            key = SANDBAG_BEAR_HUG_SQUATS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000077"),
            key = SANDBAG_CLEANS,
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
            id = Uuid.parse("fb100000-eece-1611-2025-000000000078"),
            key = SANDBAG_SHOULDER_TO_SHOULDER_PRESS,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SANDBAG)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000079"),
            key = ASSISTED_PULL_UPS,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR, RESISTANCE_BANDS)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000080"),
            key = NEGATIVE_PULL_UPS,
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000081"),
            key = CRUNCHES_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000082"),
            key = CRUNCHES_WEIGHTED_PLATE,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000083"),
            key = DECLINE_CRUNCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000084"),
            key = LYING_LEG_RAISES_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000085"),
            key = LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000086"),
            key = FLUTTER_KICKS,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000087"),
            key = BICYCLE_CRUNCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000088"),
            key = HEEL_TOUCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000089"),
            key = REVERSE_CRUNCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000090"),
            key = INCLINE_REVERSE_CRUNCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BENCH, BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000091"),
            key = HANGING_LEG_RAISES,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000092"),
            key = HANGING_KNEE_RAISES,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.FOREARMS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PULL_UP_BAR)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000093"),
            key = CAPTAINS_CHAIR_LEG_RAISES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(DIP_STATION)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000094"),
            key = CABLE_CRUNCHES_KNEELING,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000095"),
            key = CABLE_CRUNCHES_STANDING,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000096"),
            key = DUMBBELL_SIDE_BENDS,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000097"),
            key = SIT_UPS_WEIGHTED_PLATE,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES, BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000098"),
            key = SIT_UPS_WEIGHTED_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BODYWEIGHT, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000099"),
            key = WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000100"),
            key = WOODCHOPPERS_CABLE_HIGH_TO_LOW,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(CABLE_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000101"),
            key = WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(KETTLEBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000102"),
            key = LANDMINE_TWISTS_ATTACHMENT,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, LANDMINE_ATTACHMENT, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000103"),
            key = LANDMINE_TWISTS_CORNER,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, WEIGHT_PLATES)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000104"),
            key = AB_WHEEL_ROLLOUTS_KNEES,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(AB_WHEEL, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000105"),
            key = AB_WHEEL_ROLLOUTS_STANDING,
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(AB_WHEEL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000106"),
            key = ANKLE_WEIGHT_REVERSE_CRUNCHES,
            muscleGroups = listOf(MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(ANKLE_WEIGHTS, YOGA_MAT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000107"),
            key = CALF_RAISES_BODYWEIGHT_STANDING,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout, RecommendedFor.Warmup),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000108"),
            key = CALF_RAISES_DUMBBELL_STANDING,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000109"),
            key = CALF_RAISES_BARBELL_STANDING,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000110"),
            key = CALF_RAISES_SMITH_MACHINE_STANDING,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(SMITH_MACHINE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000111"),
            key = CALF_RAISES_BODYWEIGHT_SEATED,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000112"),
            key = CALF_RAISES_DUMBBELL_SEATED,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000113"),
            key = CALF_RAISES_SINGLE_LEG_BODYWEIGHT,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000114"),
            key = CALF_RAISES_SINGLE_LEG_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.CALVES),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BODYWEIGHT)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000115"),
            key = BOX_JUMPS_CALF_FOCUS,
            muscleGroups = listOf(MuscleGroup.CALVES, MuscleGroup.GLUTES, MuscleGroup.QUADS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.REPS_ONLY,
            equipmentKeys = listOf(PLYO_BOX)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000116"),
            key = DefaultExerciseResProvider.JUMP_ROPE,
            muscleGroups = listOf(MuscleGroup.CALVES, MuscleGroup.CARDIO, MuscleGroup.FULL_BODY),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.TIMED,
            equipmentKeys = listOf(DefaultEquipmentResProvider.JUMP_ROPE)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000117"),
            key = INCLINE_BENCH_PRESS_BARBELL,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(BARBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000118"),
            key = INCLINE_BENCH_PRESS_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000119"),
            key = AROUND_THE_WORLD_DUMBBELL,
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            recommendedFor = listOf(RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(DUMBBELL, BENCH)
        ),
        ExerciseSeedData(
            id = Uuid.parse("fb100000-eece-1611-2025-000000000120"),
            key = AROUND_THE_WORLD_PLATE,
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.ABS),
            recommendedFor = listOf(RecommendedFor.Warmup, RecommendedFor.Workout),
            exerciseType = ExerciseType.WEIGHT_REPS,
            equipmentKeys = listOf(WEIGHT_PLATES)
        )
    )