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
// EZ-Curl Bar Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_preacher_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_preacher_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_reverse_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_reverse_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_skullcrusher_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_skullcrusher_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_close_grip_bench_press_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_close_grip_bench_press_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_upright_row_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_upright_row_guide

// ... existing imports ...
// Dip Station / Dip Bars Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_triceps_dips_dip_station_name
import fitbe.composeapp.generated.resources.default_exercise_triceps_dips_dip_station_guide
import fitbe.composeapp.generated.resources.default_exercise_weighted_dips_dip_station_name
import fitbe.composeapp.generated.resources.default_exercise_weighted_dips_dip_station_guide
import fitbe.composeapp.generated.resources.default_exercise_dip_station_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_dip_station_leg_raises_guide
// Weight Plates Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_plate_halos_name
import fitbe.composeapp.generated.resources.default_exercise_plate_halos_guide
import fitbe.composeapp.generated.resources.default_exercise_plate_front_raise_name
import fitbe.composeapp.generated.resources.default_exercise_plate_front_raise_guide
import fitbe.composeapp.generated.resources.default_exercise_weighted_plank_plate_on_back_name
import fitbe.composeapp.generated.resources.default_exercise_weighted_plank_plate_on_back_guide
// Ankle Weights Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_glute_kickbacks_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_glute_kickbacks_guide
// Wrist Weights Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_shadow_boxing_name
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_shadow_boxing_guide
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_arm_circles_name
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_arm_circles_guide
// Plyo Box Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_name
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_guide
import fitbe.composeapp.generated.resources.default_exercise_plyo_box_step_ups_name
import fitbe.composeapp.generated.resources.default_exercise_plyo_box_step_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_decline_push_ups_feet_on_plyo_box_name
import fitbe.composeapp.generated.resources.default_exercise_decline_push_ups_feet_on_plyo_box_guide
// Medicine Ball Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_overhead_slams_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_overhead_slams_guide
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_russian_twists_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_russian_twists_guide
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_woodchoppers_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_woodchoppers_guide
// Slam Ball Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_over_shoulder_toss_name
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_over_shoulder_toss_guide
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_ground_to_overhead_name
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_ground_to_overhead_guide
// Wall Ball Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_shots_name
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_shots_guide
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_squat_and_press_no_throw_name
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_squat_and_press_no_throw_guide
// Sandbag Exercise Imports
import fitbe.composeapp.generated.resources.default_exercise_sandbag_bear_hug_squats_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_bear_hug_squats_guide
import fitbe.composeapp.generated.resources.default_exercise_sandbag_cleans_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_cleans_guide
import fitbe.composeapp.generated.resources.default_exercise_sandbag_shoulder_to_shoulder_press_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_shoulder_to_shoulder_press_guide
import fitbe.composeapp.generated.resources.default_exercise_assisted_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_assisted_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_negative_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_negative_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_crunches_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_crunches_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_crunches_weighted_plate_name
import fitbe.composeapp.generated.resources.default_exercise_crunches_weighted_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_decline_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_decline_crunches_guide
// --- Leg Raises (Lying) ---
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_dumbbell_between_feet_name
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_dumbbell_between_feet_guide
// --- Flutter Kicks ---
import fitbe.composeapp.generated.resources.default_exercise_flutter_kicks_name
import fitbe.composeapp.generated.resources.default_exercise_flutter_kicks_guide
// --- Bicycle Crunches ---
import fitbe.composeapp.generated.resources.default_exercise_bicycle_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_bicycle_crunches_guide
// --- Heel Touches (Penguin Crunches) ---
import fitbe.composeapp.generated.resources.default_exercise_heel_touches_name
import fitbe.composeapp.generated.resources.default_exercise_heel_touches_guide
// --- Reverse Crunches ---
import fitbe.composeapp.generated.resources.default_exercise_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_reverse_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_incline_reverse_crunches_guide
// --- Hanging Leg Raises & Variations ---
import fitbe.composeapp.generated.resources.default_exercise_hanging_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_hanging_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_hanging_knee_raises_name
import fitbe.composeapp.generated.resources.default_exercise_hanging_knee_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_captains_chair_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_captains_chair_leg_raises_guide
// --- Cable Crunches ---
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_kneeling_name
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_kneeling_guide
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_standing_name
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_standing_guide
// --- Dumbbell Side Bends ---
import fitbe.composeapp.generated.resources.default_exercise_dumbbell_side_bends_name
import fitbe.composeapp.generated.resources.default_exercise_dumbbell_side_bends_guide
// --- Weighted Sit-ups ---
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_plate_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_dumbbell_guide
// --- Woodchoppers ---
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_dumbbell_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_dumbbell_high_to_low_guide
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_cable_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_cable_high_to_low_guide
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_kettlebell_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_kettlebell_high_to_low_guide
// --- Landmine Twists ---
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_attachment_name
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_attachment_guide
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_corner_name
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_corner_guide
// --- Ab Wheel Rollouts ---
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_knees_name
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_knees_guide
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_standing_name
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_standing_guide
// --- Ankle Weights Exercises ---
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_reverse_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_plate_name
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_calf_focus_guide
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_calf_focus_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_barbell_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_barbell_standing_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_bodyweight_seated_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_bodyweight_seated_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_bodyweight_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_bodyweight_standing_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_dumbbell_seated_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_dumbbell_seated_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_dumbbell_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_dumbbell_standing_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_single_leg_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_single_leg_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_single_leg_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_single_leg_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_smith_machine_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_calf_raises_smith_machine_standing_name
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_jump_rope_guide
import fitbe.composeapp.generated.resources.default_exercise_jump_rope_name

import fitbe.composeapp.generated.resources.ic_launcher // Default image
//import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_curl // Specific image
// Add other specific EZ-Curl Bar images if they exist, e.g.:
// import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_preacher_curl 
// import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_reverse_curl
// import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_skullcrusher
// import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_close_grip_bench_press
// import fitbe.composeapp.generated.resources.ic_default_exercise_ez_bar_upright_row

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
        "default_exercise_kettlebell_deadlifts" to Res.string.default_exercise_kettlebell_deadlifts_name,
        // EZ-Curl Bar Exercises
        "default_exercise_ez_bar_curl" to Res.string.default_exercise_ez_bar_curl_name,
        "default_exercise_ez_bar_preacher_curl" to Res.string.default_exercise_ez_bar_preacher_curl_name,
        "default_exercise_ez_bar_reverse_curl" to Res.string.default_exercise_ez_bar_reverse_curl_name,
        "default_exercise_ez_bar_skullcrusher" to Res.string.default_exercise_ez_bar_skullcrusher_name,
        "default_exercise_ez_bar_close_grip_bench_press" to Res.string.default_exercise_ez_bar_close_grip_bench_press_name,
        "default_exercise_ez_bar_upright_row" to Res.string.default_exercise_ez_bar_upright_row_name,

        "default_exercise_triceps_dips_dip_station" to Res.string.default_exercise_triceps_dips_dip_station_name,
        "default_exercise_weighted_dips_dip_station" to Res.string.default_exercise_weighted_dips_dip_station_name,
        "default_exercise_dip_station_leg_raises" to Res.string.default_exercise_dip_station_leg_raises_name,
        "default_exercise_plate_halos" to Res.string.default_exercise_plate_halos_name,
        "default_exercise_plate_front_raise" to Res.string.default_exercise_plate_front_raise_name,
        "default_exercise_weighted_plank_plate_on_back" to Res.string.default_exercise_weighted_plank_plate_on_back_name,
        "default_exercise_ankle_weight_leg_raises" to Res.string.default_exercise_ankle_weight_leg_raises_name,
        "default_exercise_ankle_weight_glute_kickbacks" to Res.string.default_exercise_ankle_weight_glute_kickbacks_name,
        "default_exercise_wrist_weight_shadow_boxing" to Res.string.default_exercise_wrist_weight_shadow_boxing_name,
        "default_exercise_wrist_weight_arm_circles" to Res.string.default_exercise_wrist_weight_arm_circles_name,
        "default_exercise_box_jumps" to Res.string.default_exercise_box_jumps_name,
        "default_exercise_plyo_box_step_ups" to Res.string.default_exercise_plyo_box_step_ups_name,
        "default_exercise_decline_push_ups_feet_on_plyo_box" to Res.string.default_exercise_decline_push_ups_feet_on_plyo_box_name,
        "default_exercise_medicine_ball_overhead_slams" to Res.string.default_exercise_medicine_ball_overhead_slams_name,
        "default_exercise_medicine_ball_russian_twists" to Res.string.default_exercise_medicine_ball_russian_twists_name,
        "default_exercise_medicine_ball_woodchoppers" to Res.string.default_exercise_medicine_ball_woodchoppers_name,
        "default_exercise_slam_ball_over_shoulder_toss" to Res.string.default_exercise_slam_ball_over_shoulder_toss_name,
        "default_exercise_slam_ball_ground_to_overhead" to Res.string.default_exercise_slam_ball_ground_to_overhead_name,
        "default_exercise_wall_ball_shots" to Res.string.default_exercise_wall_ball_shots_name,
        "default_exercise_wall_ball_squat_and_press_no_throw" to Res.string.default_exercise_wall_ball_squat_and_press_no_throw_name,
        "default_exercise_sandbag_bear_hug_squats" to Res.string.default_exercise_sandbag_bear_hug_squats_name,
        "default_exercise_sandbag_cleans" to Res.string.default_exercise_sandbag_cleans_name,
        "default_exercise_sandbag_shoulder_to_shoulder_press" to Res.string.default_exercise_sandbag_shoulder_to_shoulder_press_name,
        "default_exercise_assisted_pull_ups" to Res.string.default_exercise_assisted_pull_ups_name,
        "default_exercise_negative_pull_ups" to Res.string.default_exercise_negative_pull_ups_name,
        "default_exercise_crunches_bodyweight" to Res.string.default_exercise_crunches_bodyweight_name,
        "default_exercise_crunches_weighted_plate" to Res.string.default_exercise_crunches_weighted_plate_name,
        "default_exercise_decline_crunches" to Res.string.default_exercise_decline_crunches_name,
        "default_exercise_lying_leg_raises_bodyweight" to Res.string.default_exercise_lying_leg_raises_bodyweight_name,
        "default_exercise_lying_leg_raises_dumbbell_between_feet" to Res.string.default_exercise_lying_leg_raises_dumbbell_between_feet_name,
        "default_exercise_flutter_kicks" to Res.string.default_exercise_flutter_kicks_name,
        "default_exercise_bicycle_crunches" to Res.string.default_exercise_bicycle_crunches_name,
        "default_exercise_heel_touches" to Res.string.default_exercise_heel_touches_name,
        "default_exercise_reverse_crunches" to Res.string.default_exercise_reverse_crunches_name,
        "default_exercise_incline_reverse_crunches" to Res.string.default_exercise_incline_reverse_crunches_name,
        "default_exercise_hanging_leg_raises" to Res.string.default_exercise_hanging_leg_raises_name,
        "default_exercise_hanging_knee_raises" to Res.string.default_exercise_hanging_knee_raises_name,
        "default_exercise_captains_chair_leg_raises" to Res.string.default_exercise_captains_chair_leg_raises_name,
        "default_exercise_cable_crunches_kneeling" to Res.string.default_exercise_cable_crunches_kneeling_name,
        "default_exercise_cable_crunches_standing" to Res.string.default_exercise_cable_crunches_standing_name,
        "default_exercise_dumbbell_side_bends" to Res.string.default_exercise_dumbbell_side_bends_name,
        "default_exercise_sit_ups_weighted_plate" to Res.string.default_exercise_sit_ups_weighted_plate_name,
        "default_exercise_sit_ups_weighted_dumbbell" to Res.string.default_exercise_sit_ups_weighted_dumbbell_name,
        "default_exercise_woodchoppers_dumbbell_high_to_low" to Res.string.default_exercise_woodchoppers_dumbbell_high_to_low_name,
        "default_exercise_woodchoppers_cable_high_to_low" to Res.string.default_exercise_woodchoppers_cable_high_to_low_name,
        "default_exercise_woodchoppers_kettlebell_high_to_low" to Res.string.default_exercise_woodchoppers_kettlebell_high_to_low_name,
        "default_exercise_landmine_twists_attachment" to Res.string.default_exercise_landmine_twists_attachment_name,
        "default_exercise_landmine_twists_corner" to Res.string.default_exercise_landmine_twists_corner_name,
        "default_exercise_ab_wheel_rollouts_knees" to Res.string.default_exercise_ab_wheel_rollouts_knees_name,
        "default_exercise_ab_wheel_rollouts_standing" to Res.string.default_exercise_ab_wheel_rollouts_standing_name,
        "default_exercise_ankle_weight_reverse_crunches" to Res.string.default_exercise_ankle_weight_reverse_crunches_name,
        "default_exercise_calf_raises_bodyweight_standing" to Res.string.default_exercise_calf_raises_bodyweight_standing_name,
        "default_exercise_calf_raises_dumbbell_standing" to Res.string.default_exercise_calf_raises_dumbbell_standing_name,
        "default_exercise_calf_raises_barbell_standing" to Res.string.default_exercise_calf_raises_barbell_standing_name,
        "default_exercise_calf_raises_smith_machine_standing" to Res.string.default_exercise_calf_raises_smith_machine_standing_name,
        "default_exercise_calf_raises_bodyweight_seated" to Res.string.default_exercise_calf_raises_bodyweight_seated_name,
        "default_exercise_calf_raises_dumbbell_seated" to Res.string.default_exercise_calf_raises_dumbbell_seated_name,
        "default_exercise_calf_raises_single_leg_bodyweight" to Res.string.default_exercise_calf_raises_single_leg_bodyweight_name,
        "default_exercise_calf_raises_single_leg_dumbbell" to Res.string.default_exercise_calf_raises_single_leg_dumbbell_name,
        "default_exercise_box_jumps_calf_focus" to Res.string.default_exercise_box_jumps_calf_focus_name,
        "default_exercise_jump_rope" to Res.string.default_exercise_jump_rope_name,
        "default_exercise_incline_bench_press_barbell" to Res.string.default_exercise_incline_bench_press_barbell_name,
        "default_exercise_incline_bench_press_dumbbell" to Res.string.default_exercise_incline_bench_press_dumbbell_name,
        "default_exercise_around_the_world_dumbbell" to Res.string.default_exercise_around_the_world_dumbbell_name,
        "default_exercise_around_the_world_plate" to Res.string.default_exercise_around_the_world_plate_name,
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
        "default_exercise_kettlebell_deadlifts" to Res.string.default_exercise_kettlebell_deadlifts_guide,
        // EZ-Curl Bar Exercises
        "default_exercise_ez_bar_curl" to Res.string.default_exercise_ez_bar_curl_guide,
        "default_exercise_ez_bar_preacher_curl" to Res.string.default_exercise_ez_bar_preacher_curl_guide,
        "default_exercise_ez_bar_reverse_curl" to Res.string.default_exercise_ez_bar_reverse_curl_guide,
        "default_exercise_ez_bar_skullcrusher" to Res.string.default_exercise_ez_bar_skullcrusher_guide,
        "default_exercise_ez_bar_close_grip_bench_press" to Res.string.default_exercise_ez_bar_close_grip_bench_press_guide,
        "default_exercise_ez_bar_upright_row" to Res.string.default_exercise_ez_bar_upright_row_guide,

        "default_exercise_triceps_dips_dip_station" to Res.string.default_exercise_triceps_dips_dip_station_guide,
        "default_exercise_weighted_dips_dip_station" to Res.string.default_exercise_weighted_dips_dip_station_guide,
        "default_exercise_dip_station_leg_raises" to Res.string.default_exercise_dip_station_leg_raises_guide,
        "default_exercise_plate_halos" to Res.string.default_exercise_plate_halos_guide,
        "default_exercise_plate_front_raise" to Res.string.default_exercise_plate_front_raise_guide,
        "default_exercise_weighted_plank_plate_on_back" to Res.string.default_exercise_weighted_plank_plate_on_back_guide,
        "default_exercise_ankle_weight_leg_raises" to Res.string.default_exercise_ankle_weight_leg_raises_guide,
        "default_exercise_ankle_weight_glute_kickbacks" to Res.string.default_exercise_ankle_weight_glute_kickbacks_guide,
        "default_exercise_wrist_weight_shadow_boxing" to Res.string.default_exercise_wrist_weight_shadow_boxing_guide,
        "default_exercise_wrist_weight_arm_circles" to Res.string.default_exercise_wrist_weight_arm_circles_guide,
        "default_exercise_box_jumps" to Res.string.default_exercise_box_jumps_guide,
        "default_exercise_plyo_box_step_ups" to Res.string.default_exercise_plyo_box_step_ups_guide,
        "default_exercise_decline_push_ups_feet_on_plyo_box" to Res.string.default_exercise_decline_push_ups_feet_on_plyo_box_guide,
        "default_exercise_medicine_ball_overhead_slams" to Res.string.default_exercise_medicine_ball_overhead_slams_guide,
        "default_exercise_medicine_ball_russian_twists" to Res.string.default_exercise_medicine_ball_russian_twists_guide,
        "default_exercise_medicine_ball_woodchoppers" to Res.string.default_exercise_medicine_ball_woodchoppers_guide,
        "default_exercise_slam_ball_over_shoulder_toss" to Res.string.default_exercise_slam_ball_over_shoulder_toss_guide,
        "default_exercise_slam_ball_ground_to_overhead" to Res.string.default_exercise_slam_ball_ground_to_overhead_guide,
        "default_exercise_wall_ball_shots" to Res.string.default_exercise_wall_ball_shots_guide,
        "default_exercise_wall_ball_squat_and_press_no_throw" to Res.string.default_exercise_wall_ball_squat_and_press_no_throw_guide,
        "default_exercise_sandbag_bear_hug_squats" to Res.string.default_exercise_sandbag_bear_hug_squats_guide,
        "default_exercise_sandbag_cleans" to Res.string.default_exercise_sandbag_cleans_guide,
        "default_exercise_sandbag_shoulder_to_shoulder_press" to Res.string.default_exercise_sandbag_shoulder_to_shoulder_press_guide,
        "default_exercise_assisted_pull_ups" to Res.string.default_exercise_assisted_pull_ups_guide,
        "default_exercise_negative_pull_ups" to Res.string.default_exercise_negative_pull_ups_guide,
        "default_exercise_crunches_bodyweight" to Res.string.default_exercise_crunches_bodyweight_guide,
        "default_exercise_crunches_weighted_plate" to Res.string.default_exercise_crunches_weighted_plate_guide,
        "default_exercise_decline_crunches" to Res.string.default_exercise_decline_crunches_guide,
        "default_exercise_lying_leg_raises_bodyweight" to Res.string.default_exercise_lying_leg_raises_bodyweight_guide,
        "default_exercise_lying_leg_raises_dumbbell_between_feet" to Res.string.default_exercise_lying_leg_raises_dumbbell_between_feet_guide,
        "default_exercise_flutter_kicks" to Res.string.default_exercise_flutter_kicks_guide,
        "default_exercise_bicycle_crunches" to Res.string.default_exercise_bicycle_crunches_guide,
        "default_exercise_heel_touches" to Res.string.default_exercise_heel_touches_guide,
        "default_exercise_reverse_crunches" to Res.string.default_exercise_reverse_crunches_guide,
        "default_exercise_incline_reverse_crunches" to Res.string.default_exercise_incline_reverse_crunches_guide,
        "default_exercise_hanging_leg_raises" to Res.string.default_exercise_hanging_leg_raises_guide,
        "default_exercise_hanging_knee_raises" to Res.string.default_exercise_hanging_knee_raises_guide,
        "default_exercise_captains_chair_leg_raises" to Res.string.default_exercise_captains_chair_leg_raises_guide,
        "default_exercise_cable_crunches_kneeling" to Res.string.default_exercise_cable_crunches_kneeling_guide,
        "default_exercise_cable_crunches_standing" to Res.string.default_exercise_cable_crunches_standing_guide,
        "default_exercise_dumbbell_side_bends" to Res.string.default_exercise_dumbbell_side_bends_guide,
        "default_exercise_sit_ups_weighted_plate" to Res.string.default_exercise_sit_ups_weighted_plate_guide,
        "default_exercise_sit_ups_weighted_dumbbell" to Res.string.default_exercise_sit_ups_weighted_dumbbell_guide,
        "default_exercise_woodchoppers_dumbbell_high_to_low" to Res.string.default_exercise_woodchoppers_dumbbell_high_to_low_guide,
        "default_exercise_woodchoppers_cable_high_to_low" to Res.string.default_exercise_woodchoppers_cable_high_to_low_guide,
        "default_exercise_woodchoppers_kettlebell_high_to_low" to Res.string.default_exercise_woodchoppers_kettlebell_high_to_low_guide,
        "default_exercise_landmine_twists_attachment" to Res.string.default_exercise_landmine_twists_attachment_guide,
        "default_exercise_landmine_twists_corner" to Res.string.default_exercise_landmine_twists_corner_guide,
        "default_exercise_ab_wheel_rollouts_knees" to Res.string.default_exercise_ab_wheel_rollouts_knees_guide,
        "default_exercise_ab_wheel_rollouts_standing" to Res.string.default_exercise_ab_wheel_rollouts_standing_guide,
        "default_exercise_ankle_weight_reverse_crunches" to Res.string.default_exercise_ankle_weight_reverse_crunches_guide,
        "default_exercise_calf_raises_bodyweight_standing" to Res.string.default_exercise_calf_raises_bodyweight_standing_guide,
        "default_exercise_calf_raises_dumbbell_standing" to Res.string.default_exercise_calf_raises_dumbbell_standing_guide,
        "default_exercise_calf_raises_barbell_standing" to Res.string.default_exercise_calf_raises_barbell_standing_guide,
        "default_exercise_calf_raises_smith_machine_standing" to Res.string.default_exercise_calf_raises_smith_machine_standing_guide,
        "default_exercise_calf_raises_bodyweight_seated" to Res.string.default_exercise_calf_raises_bodyweight_seated_guide,
        "default_exercise_calf_raises_dumbbell_seated" to Res.string.default_exercise_calf_raises_dumbbell_seated_guide,
        "default_exercise_calf_raises_single_leg_bodyweight" to Res.string.default_exercise_calf_raises_single_leg_bodyweight_guide,
        "default_exercise_calf_raises_single_leg_dumbbell" to Res.string.default_exercise_calf_raises_single_leg_dumbbell_guide,
        "default_exercise_box_jumps_calf_focus" to Res.string.default_exercise_box_jumps_calf_focus_guide,
        "default_exercise_jump_rope" to Res.string.default_exercise_jump_rope_guide,
        "default_exercise_incline_bench_press_barbell" to Res.string.default_exercise_incline_bench_press_barbell_guide,
        "default_exercise_incline_bench_press_dumbbell" to Res.string.default_exercise_incline_bench_press_dumbbell_guide,
        "default_exercise_around_the_world_dumbbell" to Res.string.default_exercise_around_the_world_dumbbell_guide,
        "default_exercise_around_the_world_plate" to Res.string.default_exercise_around_the_world_plate_guide,
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
        "default_exercise_kettlebell_deadlifts" to Res.drawable.ic_launcher,
        // EZ-Curl Bar Exercises
        "default_exercise_ez_bar_curl" to Res.drawable.ic_launcher, // Res.drawable.ic_default_exercise_ez_bar_curl, // Specific image
        "default_exercise_ez_bar_preacher_curl" to Res.drawable.ic_launcher, // Placeholder, replace if specific image exists
        "default_exercise_ez_bar_reverse_curl" to Res.drawable.ic_launcher, // Placeholder
        "default_exercise_ez_bar_skullcrusher" to Res.drawable.ic_launcher, // Placeholder
        "default_exercise_ez_bar_close_grip_bench_press" to Res.drawable.ic_launcher, // Placeholder
        "default_exercise_ez_bar_upright_row" to Res.drawable.ic_launcher, // Placeholder
        "default_exercise_triceps_dips_dip_station" to Res.drawable.ic_launcher,
        "default_exercise_weighted_dips_dip_station" to Res.drawable.ic_launcher,
        "default_exercise_dip_station_leg_raises" to Res.drawable.ic_launcher,
        "default_exercise_plate_halos" to Res.drawable.ic_launcher,
        "default_exercise_plate_front_raise" to Res.drawable.ic_launcher,
        "default_exercise_weighted_plank_plate_on_back" to Res.drawable.ic_launcher,
        "default_exercise_ankle_weight_leg_raises" to Res.drawable.ic_launcher,
        "default_exercise_ankle_weight_glute_kickbacks" to Res.drawable.ic_launcher,
        "default_exercise_wrist_weight_shadow_boxing" to Res.drawable.ic_launcher,
        "default_exercise_wrist_weight_arm_circles" to Res.drawable.ic_launcher,
        "default_exercise_box_jumps" to Res.drawable.ic_launcher,
        "default_exercise_plyo_box_step_ups" to Res.drawable.ic_launcher,
        "default_exercise_decline_push_ups_feet_on_plyo_box" to Res.drawable.ic_launcher,
        "default_exercise_medicine_ball_overhead_slams" to Res.drawable.ic_launcher,
        "default_exercise_medicine_ball_russian_twists" to Res.drawable.ic_launcher,
        "default_exercise_medicine_ball_woodchoppers" to Res.drawable.ic_launcher,
        "default_exercise_slam_ball_over_shoulder_toss" to Res.drawable.ic_launcher,
        "default_exercise_slam_ball_ground_to_overhead" to Res.drawable.ic_launcher,
        "default_exercise_wall_ball_shots" to Res.drawable.ic_launcher,
        "default_exercise_wall_ball_squat_and_press_no_throw" to Res.drawable.ic_launcher,
        "default_exercise_sandbag_bear_hug_squats" to Res.drawable.ic_launcher,
        "default_exercise_sandbag_cleans" to Res.drawable.ic_launcher,
        "default_exercise_sandbag_shoulder_to_shoulder_press" to Res.drawable.ic_launcher,
        "default_exercise_assisted_pull_ups" to Res.drawable.ic_launcher,
        "default_exercise_negative_pull_ups" to Res.drawable.ic_launcher,
        "default_exercise_crunches_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_crunches_weighted_plate" to Res.drawable.ic_launcher,
        "default_exercise_decline_crunches" to Res.drawable.ic_launcher,
        "default_exercise_lying_leg_raises_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_lying_leg_raises_dumbbell_between_feet" to Res.drawable.ic_launcher,
        "default_exercise_flutter_kicks" to Res.drawable.ic_launcher,
        "default_exercise_bicycle_crunches" to Res.drawable.ic_launcher,
        "default_exercise_heel_touches" to Res.drawable.ic_launcher,
        "default_exercise_reverse_crunches" to Res.drawable.ic_launcher,
        "default_exercise_incline_reverse_crunches" to Res.drawable.ic_launcher,
        "default_exercise_hanging_leg_raises" to Res.drawable.ic_launcher,
        "default_exercise_hanging_knee_raises" to Res.drawable.ic_launcher,
        "default_exercise_captains_chair_leg_raises" to Res.drawable.ic_launcher,
        "default_exercise_cable_crunches_kneeling" to Res.drawable.ic_launcher,
        "default_exercise_cable_crunches_standing" to Res.drawable.ic_launcher,
        "default_exercise_dumbbell_side_bends" to Res.drawable.ic_launcher,
        "default_exercise_sit_ups_weighted_plate" to Res.drawable.ic_launcher,
        "default_exercise_sit_ups_weighted_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_woodchoppers_dumbbell_high_to_low" to Res.drawable.ic_launcher,
        "default_exercise_woodchoppers_cable_high_to_low" to Res.drawable.ic_launcher,
        "default_exercise_woodchoppers_kettlebell_high_to_low" to Res.drawable.ic_launcher,
        "default_exercise_landmine_twists_attachment" to Res.drawable.ic_launcher,
        "default_exercise_landmine_twists_corner" to Res.drawable.ic_launcher,
        "default_exercise_ab_wheel_rollouts_knees" to Res.drawable.ic_launcher,
        "default_exercise_ab_wheel_rollouts_standing" to Res.drawable.ic_launcher,
        "default_exercise_ankle_weight_reverse_crunches" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_bodyweight_standing" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_dumbbell_standing" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_barbell_standing" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_smith_machine_standing" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_bodyweight_seated" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_dumbbell_seated" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_single_leg_bodyweight" to Res.drawable.ic_launcher,
        "default_exercise_calf_raises_single_leg_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_box_jumps_calf_focus" to Res.drawable.ic_launcher,
        "default_exercise_jump_rope" to Res.drawable.ic_launcher,
        "default_exercise_incline_bench_press_barbell" to Res.drawable.ic_launcher,
        "default_exercise_incline_bench_press_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_around_the_world_dumbbell" to Res.drawable.ic_launcher,
        "default_exercise_around_the_world_plate" to Res.drawable.ic_launcher,
        )
}
