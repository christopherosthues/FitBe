package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_name
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_guide
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_plank_name
import fitbe.composeapp.generated.resources.default_exercise_plank_guide
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_name
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_burpees_name
import fitbe.composeapp.generated.resources.default_exercise_burpees_guide
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_name
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_guide
// Power Rack Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_guide
// Smith Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_guide
// Chest Press Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_name
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_guide
// Air Bike Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_air_bike_cardio_name
import fitbe.composeapp.generated.resources.default_exercise_air_bike_cardio_guide
// Treadmill Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_treadmill_running_name
import fitbe.composeapp.generated.resources.default_exercise_treadmill_running_guide
import fitbe.composeapp.generated.resources.default_exercise_treadmill_walking_name
import fitbe.composeapp.generated.resources.default_exercise_treadmill_walking_guide
// Seated Row Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_seated_row_machine_name
import fitbe.composeapp.generated.resources.default_exercise_seated_row_machine_guide
// Rowing Machine Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_rowing_machine_cardio_name
import fitbe.composeapp.generated.resources.default_exercise_rowing_machine_cardio_guide
// Battle Ropes Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_battle_ropes_waves_name
import fitbe.composeapp.generated.resources.default_exercise_battle_ropes_waves_guide
// Kettlebell Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_swings_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_swings_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_single_arm_rows_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_single_arm_rows_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_turkish_get_up_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_turkish_get_up_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_halo_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_halo_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_windmill_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_windmill_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_clean_and_press_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_clean_and_press_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_snatch_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_snatch_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_russian_twists_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_russian_twists_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_farmers_walk_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_farmers_walk_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_lunges_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_lunges_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_deadlifts_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_deadlifts_guide

import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DefaultExerciseResProvider {
    val exerciseNameMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.string.default_exercise_squat_bodyweight_name,
        "default_exercise_squat_barbell" to Res.string.default_exercise_squat_barbell_name,
        "default_exercise_squat_dumbbell" to Res.string.default_exercise_squat_dumbbell_name,
        "default_exercise_goblet_squat_kettlebell" to Res.string.default_exercise_goblet_squat_kettlebell_name,
        "default_exercise_deadlift_barbell" to Res.string.default_exercise_deadlift_barbell_name,
        "default_exercise_romanian_deadlift_dumbbell" to Res.string.default_exercise_romanian_deadlift_dumbbell_name,
        "default_exercise_romanian_deadlift_barbell" to Res.string.default_exercise_romanian_deadlift_barbell_name,
        "default_exercise_bench_press_barbell" to Res.string.default_exercise_bench_press_barbell_name,
        "default_exercise_bench_press_dumbbell" to Res.string.default_exercise_bench_press_dumbbell_name,
        "default_exercise_overhead_press_barbell" to Res.string.default_exercise_overhead_press_barbell_name,
        "default_exercise_overhead_press_dumbbell" to Res.string.default_exercise_overhead_press_dumbbell_name,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_name,
        "default_exercise_push_ups_bodyweight" to Res.string.default_exercise_push_ups_bodyweight_name,
        "default_exercise_push_ups_handles" to Res.string.default_exercise_push_ups_handles_name,
        "default_exercise_lunges_bodyweight" to Res.string.default_exercise_lunges_bodyweight_name,
        "default_exercise_lunges_dumbbell" to Res.string.default_exercise_lunges_dumbbell_name,
        "default_exercise_lunges_barbell" to Res.string.default_exercise_lunges_barbell_name,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_name,
        "default_exercise_plank" to Res.string.default_exercise_plank_name,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_name,
        "default_exercise_side_lunges_bodyweight" to Res.string.default_exercise_side_lunges_bodyweight_name,
        "default_exercise_side_lunges_dumbbell" to Res.string.default_exercise_side_lunges_dumbbell_name,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_name,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_name,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.string.default_exercise_rack_pulls_barbell_power_rack_name,
        "default_exercise_squat_barbell_power_rack" to Res.string.default_exercise_squat_barbell_power_rack_name,
        "default_exercise_bench_press_barbell_power_rack" to Res.string.default_exercise_bench_press_barbell_power_rack_name,
        "default_exercise_overhead_press_barbell_power_rack" to Res.string.default_exercise_overhead_press_barbell_power_rack_name,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.string.default_exercise_squat_smith_machine_name,
        "default_exercise_bench_press_smith_machine" to Res.string.default_exercise_bench_press_smith_machine_name,
        "default_exercise_overhead_press_smith_machine" to Res.string.default_exercise_overhead_press_smith_machine_name,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.string.default_exercise_chest_press_machine_name,
        // Air Bike Exercises
        "default_exercise_air_bike_cardio" to Res.string.default_exercise_air_bike_cardio_name,
        // Treadmill Exercises
        "default_exercise_treadmill_running" to Res.string.default_exercise_treadmill_running_name,
        "default_exercise_treadmill_walking" to Res.string.default_exercise_treadmill_walking_name,
        // Seated Row Machine Exercises
        "default_exercise_seated_row_machine" to Res.string.default_exercise_seated_row_machine_name,
        // Rowing Machine Exercises
        "default_exercise_rowing_machine_cardio" to Res.string.default_exercise_rowing_machine_cardio_name,
        // Battle Ropes Exercises
        "default_exercise_battle_ropes_waves" to Res.string.default_exercise_battle_ropes_waves_name,
        // Kettlebell Exercises
        "default_exercise_kettlebell_swings" to Res.string.default_exercise_kettlebell_swings_name,
        "default_exercise_kettlebell_single_arm_rows" to Res.string.default_exercise_kettlebell_single_arm_rows_name,
        "default_exercise_kettlebell_turkish_get_up" to Res.string.default_exercise_kettlebell_turkish_get_up_name,
        "default_exercise_kettlebell_halo" to Res.string.default_exercise_kettlebell_halo_name,
        "default_exercise_kettlebell_windmill" to Res.string.default_exercise_kettlebell_windmill_name,
        "default_exercise_kettlebell_clean_and_press" to Res.string.default_exercise_kettlebell_clean_and_press_name,
        "default_exercise_kettlebell_snatch" to Res.string.default_exercise_kettlebell_snatch_name,
        "default_exercise_kettlebell_russian_twists" to Res.string.default_exercise_kettlebell_russian_twists_name,
        "default_exercise_kettlebell_farmers_walk" to Res.string.default_exercise_kettlebell_farmers_walk_name,
        "default_exercise_kettlebell_lunges" to Res.string.default_exercise_kettlebell_lunges_name,
        "default_exercise_kettlebell_deadlifts" to Res.string.default_exercise_kettlebell_deadlifts_name
    )

    val exerciseGuideMap: Map<String, StringResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.string.default_exercise_squat_bodyweight_guide,
        "default_exercise_squat_barbell" to Res.string.default_exercise_squat_barbell_guide,
        "default_exercise_squat_dumbbell" to Res.string.default_exercise_squat_dumbbell_guide,
        "default_exercise_goblet_squat_kettlebell" to Res.string.default_exercise_goblet_squat_kettlebell_guide,
        "default_exercise_deadlift_barbell" to Res.string.default_exercise_deadlift_barbell_guide,
        "default_exercise_romanian_deadlift_dumbbell" to Res.string.default_exercise_romanian_deadlift_dumbbell_guide,
        "default_exercise_romanian_deadlift_barbell" to Res.string.default_exercise_romanian_deadlift_barbell_guide,
        "default_exercise_bench_press_barbell" to Res.string.default_exercise_bench_press_barbell_guide,
        "default_exercise_bench_press_dumbbell" to Res.string.default_exercise_bench_press_dumbbell_guide,
        "default_exercise_overhead_press_barbell" to Res.string.default_exercise_overhead_press_barbell_guide,
        "default_exercise_overhead_press_dumbbell" to Res.string.default_exercise_overhead_press_dumbbell_guide,
        "default_exercise_pull_ups" to Res.string.default_exercise_pull_ups_guide,
        "default_exercise_push_ups_bodyweight" to Res.string.default_exercise_push_ups_bodyweight_guide,
        "default_exercise_push_ups_handles" to Res.string.default_exercise_push_ups_handles_guide,
        "default_exercise_lunges_bodyweight" to Res.string.default_exercise_lunges_bodyweight_guide,
        "default_exercise_lunges_dumbbell" to Res.string.default_exercise_lunges_dumbbell_guide,
        "default_exercise_lunges_barbell" to Res.string.default_exercise_lunges_barbell_guide,
        "default_exercise_sit_ups" to Res.string.default_exercise_sit_ups_guide,
        "default_exercise_plank" to Res.string.default_exercise_plank_guide,
        "default_exercise_jumping_jacks" to Res.string.default_exercise_jumping_jacks_guide,
        "default_exercise_side_lunges_bodyweight" to Res.string.default_exercise_side_lunges_bodyweight_guide,
        "default_exercise_side_lunges_dumbbell" to Res.string.default_exercise_side_lunges_dumbbell_guide,
        "default_exercise_burpees" to Res.string.default_exercise_burpees_guide,
        "default_exercise_mountain_climbers" to Res.string.default_exercise_mountain_climbers_guide,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.string.default_exercise_rack_pulls_barbell_power_rack_guide,
        "default_exercise_squat_barbell_power_rack" to Res.string.default_exercise_squat_barbell_power_rack_guide,
        "default_exercise_bench_press_barbell_power_rack" to Res.string.default_exercise_bench_press_barbell_power_rack_guide,
        "default_exercise_overhead_press_barbell_power_rack" to Res.string.default_exercise_overhead_press_barbell_power_rack_guide,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.string.default_exercise_squat_smith_machine_guide,
        "default_exercise_bench_press_smith_machine" to Res.string.default_exercise_bench_press_smith_machine_guide,
        "default_exercise_overhead_press_smith_machine" to Res.string.default_exercise_overhead_press_smith_machine_guide,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.string.default_exercise_chest_press_machine_guide,
        // Air Bike Exercises
        "default_exercise_air_bike_cardio" to Res.string.default_exercise_air_bike_cardio_guide,
        // Treadmill Exercises
        "default_exercise_treadmill_running" to Res.string.default_exercise_treadmill_running_guide,
        "default_exercise_treadmill_walking" to Res.string.default_exercise_treadmill_walking_guide,
        // Seated Row Machine Exercises
        "default_exercise_seated_row_machine" to Res.string.default_exercise_seated_row_machine_guide,
        // Rowing Machine Exercises
        "default_exercise_rowing_machine_cardio" to Res.string.default_exercise_rowing_machine_cardio_guide,
        // Battle Ropes Exercises
        "default_exercise_battle_ropes_waves" to Res.string.default_exercise_battle_ropes_waves_guide,
        // Kettlebell Exercises
        "default_exercise_kettlebell_swings" to Res.string.default_exercise_kettlebell_swings_guide,
        "default_exercise_kettlebell_single_arm_rows" to Res.string.default_exercise_kettlebell_single_arm_rows_guide,
        "default_exercise_kettlebell_turkish_get_up" to Res.string.default_exercise_kettlebell_turkish_get_up_guide,
        "default_exercise_kettlebell_halo" to Res.string.default_exercise_kettlebell_halo_guide,
        "default_exercise_kettlebell_windmill" to Res.string.default_exercise_kettlebell_windmill_guide,
        "default_exercise_kettlebell_clean_and_press" to Res.string.default_exercise_kettlebell_clean_and_press_guide,
        "default_exercise_kettlebell_snatch" to Res.string.default_exercise_kettlebell_snatch_guide,
        "default_exercise_kettlebell_russian_twists" to Res.string.default_exercise_kettlebell_russian_twists_guide,
        "default_exercise_kettlebell_farmers_walk" to Res.string.default_exercise_kettlebell_farmers_walk_guide,
        "default_exercise_kettlebell_lunges" to Res.string.default_exercise_kettlebell_lunges_guide,
        "default_exercise_kettlebell_deadlifts" to Res.string.default_exercise_kettlebell_deadlifts_guide
    )

    val exerciseImageMap: Map<String, DrawableResource> = mapOf(
        "default_exercise_squat_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_squat_barbell" to Res.drawable.ic_launcher,
        "default_exercise_squat_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_goblet_squat_kettlebell" to Res.drawable.ic_launcher,
        "default_exercise_deadlift_barbell" to Res.drawable.ic_launcher,
        "default_exercise_romanian_deadlift_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_romanian_deadlift_barbell" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_barbell" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_barbell" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_pull_ups" to Res.drawable.ic_launcher,
        "default_exercise_push_ups_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_push_ups_handles" to Res.drawable.ic_launcher,
        "default_exercise_lunges_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_lunges_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_lunges_barbell" to Res.drawable.ic_launcher,
        "default_exercise_sit_ups" to Res.drawable.ic_launcher,
        "default_exercise_plank" to Res.drawable.ic_launcher,
        "default_exercise_jumping_jacks" to Res.drawable.ic_launcher,
        "default_exercise_side_lunges_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_side_lunges_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_burpees" to Res.drawable.ic_launcher,
        "default_exercise_mountain_climbers" to Res.drawable.ic_launcher,
        // Power Rack Exercises
        "default_exercise_rack_pulls_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_squat_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_barbell_power_rack" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_barbell_power_rack" to Res.drawable.ic_launcher,
        // Smith Machine Exercises
        "default_exercise_squat_smith_machine" to Res.drawable.ic_launcher,
        "default_exercise_bench_press_smith_machine" to Res.drawable.ic_launcher,
        "default_exercise_overhead_press_smith_machine" to Res.drawable.ic_launcher,
        // Chest Press Machine Exercises
        "default_exercise_chest_press_machine" to Res.drawable.ic_launcher,
        // Air Bike Exercises
        "default_exercise_air_bike_cardio" to Res.drawable.ic_launcher,
        // Treadmill Exercises
        "default_exercise_treadmill_running" to Res.drawable.ic_launcher,
        "default_exercise_treadmill_walking" to Res.drawable.ic_launcher,
        // Seated Row Machine Exercises
        "default_exercise_seated_row_machine" to Res.drawable.ic_launcher,
        // Rowing Machine Exercises
        "default_exercise_rowing_machine_cardio" to Res.drawable.ic_launcher,
        // Battle Ropes Exercises
        "default_exercise_battle_ropes_waves" to Res.drawable.ic_launcher,
        // Kettlebell Exercises
        "default_exercise_kettlebell_swings" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_single_arm_rows" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_turkish_get_up" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_halo" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_windmill" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_clean_and_press" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_snatch" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_russian_twists" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_farmers_walk" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_lunges" to Res.drawable.ic_launcher,
        "default_exercise_kettlebell_deadlifts" to Res.drawable.ic_launcher
    )
}
