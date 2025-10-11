package org.darthacheron.fitbe.workouts.exercises

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_knees_guide
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_knees_name
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_ab_wheel_rollouts_standing_name
import fitbe.composeapp.generated.resources.default_exercise_air_bike_cardio_guide
import fitbe.composeapp.generated.resources.default_exercise_air_bike_cardio_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_glute_kickbacks_guide
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_glute_kickbacks_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_reverse_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_ankle_weight_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_around_the_world_plate_name
import fitbe.composeapp.generated.resources.default_exercise_assisted_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_assisted_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_battle_ropes_waves_guide
import fitbe.composeapp.generated.resources.default_exercise_battle_ropes_waves_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_bench_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_bicycle_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_bicycle_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_calf_focus_guide
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_calf_focus_name
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_guide
import fitbe.composeapp.generated.resources.default_exercise_box_jumps_name
import fitbe.composeapp.generated.resources.default_exercise_burpees_guide
import fitbe.composeapp.generated.resources.default_exercise_burpees_name
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_kneeling_guide
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_kneeling_name
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_standing_guide
import fitbe.composeapp.generated.resources.default_exercise_cable_crunches_standing_name
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
import fitbe.composeapp.generated.resources.default_exercise_captains_chair_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_captains_chair_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_chest_press_machine_name
import fitbe.composeapp.generated.resources.default_exercise_crunches_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_crunches_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_crunches_weighted_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_crunches_weighted_plate_name
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_decline_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_decline_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_decline_push_ups_feet_on_plyo_box_guide
import fitbe.composeapp.generated.resources.default_exercise_decline_push_ups_feet_on_plyo_box_name
import fitbe.composeapp.generated.resources.default_exercise_dip_station_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_dip_station_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_dumbbell_side_bends_guide
import fitbe.composeapp.generated.resources.default_exercise_dumbbell_side_bends_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_close_grip_bench_press_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_close_grip_bench_press_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_preacher_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_preacher_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_reverse_curl_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_reverse_curl_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_skullcrusher_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_skullcrusher_name
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_upright_row_guide
import fitbe.composeapp.generated.resources.default_exercise_ez_bar_upright_row_name
import fitbe.composeapp.generated.resources.default_exercise_flutter_kicks_guide
import fitbe.composeapp.generated.resources.default_exercise_flutter_kicks_name
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_guide
import fitbe.composeapp.generated.resources.default_exercise_goblet_squat_kettlebell_name
import fitbe.composeapp.generated.resources.default_exercise_hanging_knee_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_hanging_knee_raises_name
import fitbe.composeapp.generated.resources.default_exercise_hanging_leg_raises_guide
import fitbe.composeapp.generated.resources.default_exercise_hanging_leg_raises_name
import fitbe.composeapp.generated.resources.default_exercise_heel_touches_guide
import fitbe.composeapp.generated.resources.default_exercise_heel_touches_name
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_bench_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_incline_reverse_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_incline_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_jump_rope_guide
import fitbe.composeapp.generated.resources.default_exercise_jump_rope_name
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_guide
import fitbe.composeapp.generated.resources.default_exercise_jumping_jacks_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_clean_and_press_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_clean_and_press_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_deadlifts_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_deadlifts_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_farmers_walk_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_farmers_walk_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_halo_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_halo_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_lunges_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_lunges_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_russian_twists_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_russian_twists_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_single_arm_rows_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_single_arm_rows_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_snatch_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_snatch_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_swings_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_swings_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_turkish_get_up_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_turkish_get_up_name
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_windmill_guide
import fitbe.composeapp.generated.resources.default_exercise_kettlebell_windmill_name
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_attachment_guide
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_attachment_name
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_corner_guide
import fitbe.composeapp.generated.resources.default_exercise_landmine_twists_corner_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_dumbbell_between_feet_guide
import fitbe.composeapp.generated.resources.default_exercise_lying_leg_raises_dumbbell_between_feet_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_overhead_slams_guide
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_overhead_slams_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_russian_twists_guide
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_russian_twists_name
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_woodchoppers_guide
import fitbe.composeapp.generated.resources.default_exercise_medicine_ball_woodchoppers_name
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_guide
import fitbe.composeapp.generated.resources.default_exercise_mountain_climbers_name
import fitbe.composeapp.generated.resources.default_exercise_negative_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_negative_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_overhead_press_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_plank_guide
import fitbe.composeapp.generated.resources.default_exercise_plank_name
import fitbe.composeapp.generated.resources.default_exercise_plate_front_raise_guide
import fitbe.composeapp.generated.resources.default_exercise_plate_front_raise_name
import fitbe.composeapp.generated.resources.default_exercise_plate_halos_guide
import fitbe.composeapp.generated.resources.default_exercise_plate_halos_name
import fitbe.composeapp.generated.resources.default_exercise_plyo_box_step_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_plyo_box_step_ups_name
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_pull_ups_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_guide
import fitbe.composeapp.generated.resources.default_exercise_push_ups_handles_name
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_rack_pulls_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_reverse_crunches_guide
import fitbe.composeapp.generated.resources.default_exercise_reverse_crunches_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_romanian_deadlift_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_rowing_machine_cardio_guide
import fitbe.composeapp.generated.resources.default_exercise_rowing_machine_cardio_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_bear_hug_squats_guide
import fitbe.composeapp.generated.resources.default_exercise_sandbag_bear_hug_squats_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_cleans_guide
import fitbe.composeapp.generated.resources.default_exercise_sandbag_cleans_name
import fitbe.composeapp.generated.resources.default_exercise_sandbag_shoulder_to_shoulder_press_guide
import fitbe.composeapp.generated.resources.default_exercise_sandbag_shoulder_to_shoulder_press_name
import fitbe.composeapp.generated.resources.default_exercise_seated_row_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_seated_row_machine_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_side_lunges_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_plate_guide
import fitbe.composeapp.generated.resources.default_exercise_sit_ups_weighted_plate_name
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_ground_to_overhead_guide
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_ground_to_overhead_name
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_over_shoulder_toss_guide
import fitbe.composeapp.generated.resources.default_exercise_slam_ball_over_shoulder_toss_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_barbell_power_rack_name
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_bodyweight_name
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_dumbbell_name
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_guide
import fitbe.composeapp.generated.resources.default_exercise_squat_smith_machine_name
import fitbe.composeapp.generated.resources.default_exercise_treadmill_running_guide
import fitbe.composeapp.generated.resources.default_exercise_treadmill_running_name
import fitbe.composeapp.generated.resources.default_exercise_treadmill_walking_guide
import fitbe.composeapp.generated.resources.default_exercise_treadmill_walking_name
import fitbe.composeapp.generated.resources.default_exercise_triceps_dips_dip_station_guide
import fitbe.composeapp.generated.resources.default_exercise_triceps_dips_dip_station_name
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_shots_guide
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_shots_name
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_squat_and_press_no_throw_guide
import fitbe.composeapp.generated.resources.default_exercise_wall_ball_squat_and_press_no_throw_name
import fitbe.composeapp.generated.resources.default_exercise_weighted_dips_dip_station_guide
import fitbe.composeapp.generated.resources.default_exercise_weighted_dips_dip_station_name
import fitbe.composeapp.generated.resources.default_exercise_weighted_plank_plate_on_back_guide
import fitbe.composeapp.generated.resources.default_exercise_weighted_plank_plate_on_back_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_cable_high_to_low_guide
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_cable_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_dumbbell_high_to_low_guide
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_dumbbell_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_kettlebell_high_to_low_guide
import fitbe.composeapp.generated.resources.default_exercise_woodchoppers_kettlebell_high_to_low_name
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_arm_circles_guide
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_arm_circles_name
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_shadow_boxing_guide
import fitbe.composeapp.generated.resources.default_exercise_wrist_weight_shadow_boxing_name
import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DefaultExerciseResProvider {
//    PUSH_UPS
//    RUSSIAN_TWISTS
//    TRICEP_DIPS_BENCH
//    T_BAR_ROWS
//    V_UPS

    const val AB_WHEEL_ROLLOUTS_KNEES = "default_exercise_ab_wheel_rollouts_knees"
    const val AB_WHEEL_ROLLOUTS_STANDING = "default_exercise_ab_wheel_rollouts_standing"
    const val AIR_BIKE_CARDIO = "default_exercise_air_bike_cardio"
    const val ANKLE_WEIGHT_GLUTE_KICKBACKS = "default_exercise_ankle_weight_glute_kickbacks"
    const val ANKLE_WEIGHT_LEG_RAISES = "default_exercise_ankle_weight_leg_raises"
    const val ANKLE_WEIGHT_REVERSE_CRUNCHES = "default_exercise_ankle_weight_reverse_crunches"
    const val AROUND_THE_WORLD_DUMBBELL = "default_exercise_around_the_world_dumbbell"
    const val AROUND_THE_WORLD_PLATE = "default_exercise_around_the_world_plate"
    const val ASSISTED_PULL_UPS = "default_exercise_assisted_pull_ups"
    const val BATTLE_ROPES_WAVES = "default_exercise_battle_ropes_waves"
    const val BENCH_PRESS_BARBELL = "default_exercise_bench_press_barbell"
    const val BENCH_PRESS_BARBELL_POWER_RACK = "default_exercise_bench_press_barbell_power_rack"
    const val BENCH_PRESS_DUMBBELL = "default_exercise_bench_press_dumbbell"
    const val BENCH_PRESS_SMITH_MACHINE = "default_exercise_bench_press_smith_machine"
    const val BICYCLE_CRUNCHES = "default_exercise_bicycle_crunches"
    const val BOX_JUMPS = "default_exercise_box_jumps"
    const val BOX_JUMPS_CALF_FOCUS = "default_exercise_box_jumps_calf_focus"
    const val BURPEES = "default_exercise_burpees"
    const val CABLE_CRUNCHES_KNEELING = "default_exercise_cable_crunches_kneeling"
    const val CABLE_CRUNCHES_STANDING = "default_exercise_cable_crunches_standing"
    const val CALF_RAISES_BARBELL_STANDING = "default_exercise_calf_raises_barbell_standing"
    const val CALF_RAISES_BODYWEIGHT_SEATED = "default_exercise_calf_raises_bodyweight_seated"
    const val CALF_RAISES_BODYWEIGHT_STANDING = "default_exercise_calf_raises_bodyweight_standing"
    const val CALF_RAISES_DUMBBELL_SEATED = "default_exercise_calf_raises_dumbbell_seated"
    const val CALF_RAISES_DUMBBELL_STANDING = "default_exercise_calf_raises_dumbbell_standing"
    const val CALF_RAISES_SINGLE_LEG_BODYWEIGHT = "default_exercise_calf_raises_single_leg_bodyweight"
    const val CALF_RAISES_SINGLE_LEG_DUMBBELL = "default_exercise_calf_raises_single_leg_dumbbell"
    const val CALF_RAISES_SMITH_MACHINE_STANDING = "default_exercise_calf_raises_smith_machine_standing"
    const val CAPTAINS_CHAIR_LEG_RAISES = "default_exercise_captains_chair_leg_raises"
    const val CHEST_PRESS_MACHINE = "default_exercise_chest_press_machine"
    const val CRUNCHES_BODYWEIGHT = "default_exercise_crunches_bodyweight"
    const val CRUNCHES_WEIGHTED_PLATE = "default_exercise_crunches_weighted_plate"
    const val DEADLIFT_BARBELL = "default_exercise_deadlift_barbell"
    const val DECLINE_CRUNCHES = "default_exercise_decline_crunches"
    const val DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX = "default_exercise_decline_push_ups_feet_on_plyo_box"
    const val DIP_STATION_LEG_RAISES = "default_exercise_dip_station_leg_raises"
    const val DUMBBELL_SIDE_BENDS = "default_exercise_dumbbell_side_bends"
    const val EZ_BAR_CLOSE_GRIP_BENCH_PRESS = "default_exercise_ez_bar_close_grip_bench_press"
    const val EZ_BAR_CURL = "default_exercise_ez_bar_curl"
    const val EZ_BAR_PREACHER_CURL = "default_exercise_ez_bar_preacher_curl"
    const val EZ_BAR_REVERSE_CURL = "default_exercise_ez_bar_reverse_curl"
    const val EZ_BAR_SKULLCRUSHER = "default_exercise_ez_bar_skullcrusher"
    const val EZ_BAR_UPRIGHT_ROW = "default_exercise_ez_bar_upright_row"
    const val FLUTTER_KICKS = "default_exercise_flutter_kicks"
    const val GOBLET_SQUAT_KETTLEBELL = "default_exercise_goblet_squat_kettlebell"
    const val HANGING_KNEE_RAISES = "default_exercise_hanging_knee_raises"
    const val HANGING_LEG_RAISES = "default_exercise_hanging_leg_raises"
    const val HEEL_TOUCHES = "default_exercise_heel_touches"
    const val INCLINE_BENCH_PRESS_BARBELL = "default_exercise_incline_bench_press_barbell"
    const val INCLINE_BENCH_PRESS_DUMBBELL = "default_exercise_incline_bench_press_dumbbell"
    const val INCLINE_REVERSE_CRUNCHES = "default_exercise_incline_reverse_crunches"
    const val JUMP_ROPE = "default_exercise_jump_rope"
    const val JUMPING_JACKS = "default_exercise_jumping_jacks"
    const val KETTLEBELL_CLEAN_AND_PRESS = "default_exercise_kettlebell_clean_and_press"
    const val KETTLEBELL_DEADLIFTS = "default_exercise_kettlebell_deadlifts"
    const val KETTLEBELL_FARMERS_WALK = "default_exercise_kettlebell_farmers_walk"
    const val KETTLEBELL_HALO = "default_exercise_kettlebell_halo"
    const val KETTLEBELL_LUNGES = "default_exercise_kettlebell_lunges"
    const val KETTLEBELL_RUSSIAN_TWISTS = "default_exercise_kettlebell_russian_twists"
    const val KETTLEBELL_SINGLE_ARM_ROWS = "default_exercise_kettlebell_single_arm_rows"
    const val KETTLEBELL_SNATCH = "default_exercise_kettlebell_snatch"
    const val KETTLEBELL_SWINGS = "default_exercise_kettlebell_swings"
    const val KETTLEBELL_TURKISH_GET_UP = "default_exercise_kettlebell_turkish_get_up"
    const val KETTLEBELL_WINDMILL = "default_exercise_kettlebell_windmill"
    const val LANDMINE_TWISTS_ATTACHMENT = "default_exercise_landmine_twists_attachment"
    const val LANDMINE_TWISTS_CORNER = "default_exercise_landmine_twists_corner"
    const val LUNGES_BARBELL = "default_exercise_lunges_barbell"
    const val LUNGES_BODYWEIGHT = "default_exercise_lunges_bodyweight"
    const val LUNGES_DUMBBELL = "default_exercise_lunges_dumbbell"
    const val LYING_LEG_RAISES_BODYWEIGHT = "default_exercise_lying_leg_raises_bodyweight"
    const val LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET = "default_exercise_lying_leg_raises_dumbbell_between_feet"
    const val MEDICINE_BALL_OVERHEAD_SLAMS = "default_exercise_medicine_ball_overhead_slams"
    const val MEDICINE_BALL_RUSSIAN_TWISTS = "default_exercise_medicine_ball_russian_twists"
    const val MEDICINE_BALL_WOODCHOPPERS = "default_exercise_medicine_ball_woodchoppers"
    const val MOUNTAIN_CLIMBERS = "default_exercise_mountain_climbers"
    const val NEGATIVE_PULL_UPS = "default_exercise_negative_pull_ups"
    const val OVERHEAD_PRESS_BARBELL = "default_exercise_overhead_press_barbell"
    const val OVERHEAD_PRESS_BARBELL_POWER_RACK = "default_exercise_overhead_press_barbell_power_rack"
    const val OVERHEAD_PRESS_DUMBBELL = "default_exercise_overhead_press_dumbbell"
    const val OVERHEAD_PRESS_SMITH_MACHINE = "default_exercise_overhead_press_smith_machine"
    const val PLANK = "default_exercise_plank"
    const val PLATE_FRONT_RAISE = "default_exercise_plate_front_raise"
    const val PLATE_HALOS = "default_exercise_plate_halos"
    const val PLYO_BOX_STEP_UPS = "default_exercise_plyo_box_step_ups"
    const val PULL_UPS = "default_exercise_pull_ups"
    const val PUSH_UPS_BODYWEIGHT = "default_exercise_push_ups_bodyweight"
    const val PUSH_UPS_HANDLES = "default_exercise_push_ups_handles"
    const val RACK_PULLS_BARBELL_POWER_RACK = "default_exercise_rack_pulls_barbell_power_rack"
    const val REVERSE_CRUNCHES = "default_exercise_reverse_crunches"
    const val ROMANIAN_DEADLIFT_BARBELL = "default_exercise_romanian_deadlift_barbell"
    const val ROMANIAN_DEADLIFT_DUMBBELL = "default_exercise_romanian_deadlift_dumbbell"
    const val ROWING_MACHINE_CARDIO = "default_exercise_rowing_machine_cardio"
    const val SANDBAG_BEAR_HUG_SQUATS = "default_exercise_sandbag_bear_hug_squats"
    const val SANDBAG_CLEANS = "default_exercise_sandbag_cleans"
    const val SANDBAG_SHOULDER_TO_SHOULDER_PRESS = "default_exercise_sandbag_shoulder_to_shoulder_press"
    const val SEATED_ROW_MACHINE = "default_exercise_seated_row_machine"
    const val SIDE_LUNGES_BODYWEIGHT = "default_exercise_side_lunges_bodyweight"
    const val SIDE_LUNGES_DUMBBELL = "default_exercise_side_lunges_dumbbell"
    const val SIT_UPS = "default_exercise_sit_ups"
    const val SIT_UPS_WEIGHTED_DUMBBELL = "default_exercise_sit_ups_weighted_dumbbell"
    const val SIT_UPS_WEIGHTED_PLATE = "default_exercise_sit_ups_weighted_plate"
    const val SLAM_BALL_GROUND_TO_OVERHEAD = "default_exercise_slam_ball_ground_to_overhead"
    const val SLAM_BALL_OVER_SHOULDER_TOSS = "default_exercise_slam_ball_over_shoulder_toss"
    const val SQUAT_BARBELL = "default_exercise_squat_barbell"
    const val SQUAT_BARBELL_POWER_RACK = "default_exercise_squat_barbell_power_rack"
    const val SQUAT_BODYWEIGHT = "default_exercise_squat_bodyweight"
    const val SQUAT_DUMBBELL = "default_exercise_squat_dumbbell"
    const val SQUAT_SMITH_MACHINE = "default_exercise_squat_smith_machine"
    const val TREADMILL_RUNNING = "default_exercise_treadmill_running"
    const val TREADMILL_WALKING = "default_exercise_treadmill_walking"
    const val TRICEPS_DIPS_DIP_STATION = "default_exercise_triceps_dips_dip_station"
    const val WALL_BALL_SHOTS = "default_exercise_wall_ball_shots"
    const val WALL_BALL_SQUAT_AND_PRESS_NO_THROW = "default_exercise_wall_ball_squat_and_press_no_throw"
    const val WEIGHTED_DIPS_DIP_STATION = "default_exercise_weighted_dips_dip_station"
    const val WEIGHTED_PLANK_PLATE_ON_BACK = "default_exercise_weighted_plank_plate_on_back"
    const val WOODCHOPPERS_CABLE_HIGH_TO_LOW = "default_exercise_woodchoppers_cable_high_to_low"
    const val WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW = "default_exercise_woodchoppers_dumbbell_high_to_low"
    const val WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW = "default_exercise_woodchoppers_kettlebell_high_to_low"
    const val WRIST_WEIGHT_ARM_CIRCLES = "default_exercise_wrist_weight_arm_circles"
    const val WRIST_WEIGHT_SHADOW_BOXING = "default_exercise_wrist_weight_shadow_boxing"

    val exerciseNameMap: Map<String, StringResource> =
        mapOf(
            AB_WHEEL_ROLLOUTS_KNEES to Res.string.default_exercise_ab_wheel_rollouts_knees_name,
            AB_WHEEL_ROLLOUTS_STANDING to Res.string.default_exercise_ab_wheel_rollouts_standing_name,
            AIR_BIKE_CARDIO to Res.string.default_exercise_air_bike_cardio_name,
            ANKLE_WEIGHT_GLUTE_KICKBACKS to Res.string.default_exercise_ankle_weight_glute_kickbacks_name,
            ANKLE_WEIGHT_LEG_RAISES to Res.string.default_exercise_ankle_weight_leg_raises_name,
            ANKLE_WEIGHT_REVERSE_CRUNCHES to Res.string.default_exercise_ankle_weight_reverse_crunches_name,
            AROUND_THE_WORLD_DUMBBELL to Res.string.default_exercise_around_the_world_dumbbell_name,
            AROUND_THE_WORLD_PLATE to Res.string.default_exercise_around_the_world_plate_name,
            ASSISTED_PULL_UPS to Res.string.default_exercise_assisted_pull_ups_name,
            BATTLE_ROPES_WAVES to Res.string.default_exercise_battle_ropes_waves_name,
            BENCH_PRESS_BARBELL to Res.string.default_exercise_bench_press_barbell_name,
            BENCH_PRESS_BARBELL_POWER_RACK to Res.string.default_exercise_bench_press_barbell_power_rack_name,
            BENCH_PRESS_DUMBBELL to Res.string.default_exercise_bench_press_dumbbell_name,
            BENCH_PRESS_SMITH_MACHINE to Res.string.default_exercise_bench_press_smith_machine_name,
            BICYCLE_CRUNCHES to Res.string.default_exercise_bicycle_crunches_name,
            BOX_JUMPS to Res.string.default_exercise_box_jumps_name,
            BOX_JUMPS_CALF_FOCUS to Res.string.default_exercise_box_jumps_calf_focus_name,
            BURPEES to Res.string.default_exercise_burpees_name,
            CABLE_CRUNCHES_KNEELING to Res.string.default_exercise_cable_crunches_kneeling_name,
            CABLE_CRUNCHES_STANDING to Res.string.default_exercise_cable_crunches_standing_name,
            CALF_RAISES_BARBELL_STANDING to Res.string.default_exercise_calf_raises_barbell_standing_name,
            CALF_RAISES_BODYWEIGHT_SEATED to Res.string.default_exercise_calf_raises_bodyweight_seated_name,
            CALF_RAISES_BODYWEIGHT_STANDING to Res.string.default_exercise_calf_raises_bodyweight_standing_name,
            CALF_RAISES_DUMBBELL_SEATED to Res.string.default_exercise_calf_raises_dumbbell_seated_name,
            CALF_RAISES_DUMBBELL_STANDING to Res.string.default_exercise_calf_raises_dumbbell_standing_name,
            CALF_RAISES_SINGLE_LEG_BODYWEIGHT to Res.string.default_exercise_calf_raises_single_leg_bodyweight_name,
            CALF_RAISES_SINGLE_LEG_DUMBBELL to Res.string.default_exercise_calf_raises_single_leg_dumbbell_name,
            CALF_RAISES_SMITH_MACHINE_STANDING to Res.string.default_exercise_calf_raises_smith_machine_standing_name,
            CAPTAINS_CHAIR_LEG_RAISES to Res.string.default_exercise_captains_chair_leg_raises_name,
            CHEST_PRESS_MACHINE to Res.string.default_exercise_chest_press_machine_name,
            CRUNCHES_BODYWEIGHT to Res.string.default_exercise_crunches_bodyweight_name,
            CRUNCHES_WEIGHTED_PLATE to Res.string.default_exercise_crunches_weighted_plate_name,
            DEADLIFT_BARBELL to Res.string.default_exercise_deadlift_barbell_name,
            DECLINE_CRUNCHES to Res.string.default_exercise_decline_crunches_name,
            DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX to Res.string.default_exercise_decline_push_ups_feet_on_plyo_box_name,
            DIP_STATION_LEG_RAISES to Res.string.default_exercise_dip_station_leg_raises_name,
            DUMBBELL_SIDE_BENDS to Res.string.default_exercise_dumbbell_side_bends_name,
            EZ_BAR_CLOSE_GRIP_BENCH_PRESS to Res.string.default_exercise_ez_bar_close_grip_bench_press_name,
            EZ_BAR_CURL to Res.string.default_exercise_ez_bar_curl_name,
            EZ_BAR_PREACHER_CURL to Res.string.default_exercise_ez_bar_preacher_curl_name,
            EZ_BAR_REVERSE_CURL to Res.string.default_exercise_ez_bar_reverse_curl_name,
            EZ_BAR_SKULLCRUSHER to Res.string.default_exercise_ez_bar_skullcrusher_name,
            EZ_BAR_UPRIGHT_ROW to Res.string.default_exercise_ez_bar_upright_row_name,
            FLUTTER_KICKS to Res.string.default_exercise_flutter_kicks_name,
            GOBLET_SQUAT_KETTLEBELL to Res.string.default_exercise_goblet_squat_kettlebell_name,
            HANGING_KNEE_RAISES to Res.string.default_exercise_hanging_knee_raises_name,
            HANGING_LEG_RAISES to Res.string.default_exercise_hanging_leg_raises_name,
            HEEL_TOUCHES to Res.string.default_exercise_heel_touches_name,
            INCLINE_BENCH_PRESS_BARBELL to Res.string.default_exercise_incline_bench_press_barbell_name,
            INCLINE_BENCH_PRESS_DUMBBELL to Res.string.default_exercise_incline_bench_press_dumbbell_name,
            INCLINE_REVERSE_CRUNCHES to Res.string.default_exercise_incline_reverse_crunches_name,
            JUMP_ROPE to Res.string.default_exercise_jump_rope_name,
            JUMPING_JACKS to Res.string.default_exercise_jumping_jacks_name,
            KETTLEBELL_CLEAN_AND_PRESS to Res.string.default_exercise_kettlebell_clean_and_press_name,
            KETTLEBELL_DEADLIFTS to Res.string.default_exercise_kettlebell_deadlifts_name,
            KETTLEBELL_FARMERS_WALK to Res.string.default_exercise_kettlebell_farmers_walk_name,
            KETTLEBELL_HALO to Res.string.default_exercise_kettlebell_halo_name,
            KETTLEBELL_LUNGES to Res.string.default_exercise_kettlebell_lunges_name,
            KETTLEBELL_RUSSIAN_TWISTS to Res.string.default_exercise_kettlebell_russian_twists_name,
            KETTLEBELL_SINGLE_ARM_ROWS to Res.string.default_exercise_kettlebell_single_arm_rows_name,
            KETTLEBELL_SNATCH to Res.string.default_exercise_kettlebell_snatch_name,
            KETTLEBELL_SWINGS to Res.string.default_exercise_kettlebell_swings_name,
            KETTLEBELL_TURKISH_GET_UP to Res.string.default_exercise_kettlebell_turkish_get_up_name,
            KETTLEBELL_WINDMILL to Res.string.default_exercise_kettlebell_windmill_name,
            LANDMINE_TWISTS_ATTACHMENT to Res.string.default_exercise_landmine_twists_attachment_name,
            LANDMINE_TWISTS_CORNER to Res.string.default_exercise_landmine_twists_corner_name,
            LUNGES_BARBELL to Res.string.default_exercise_lunges_barbell_name,
            LUNGES_BODYWEIGHT to Res.string.default_exercise_lunges_bodyweight_name,
            LUNGES_DUMBBELL to Res.string.default_exercise_lunges_dumbbell_name,
            LYING_LEG_RAISES_BODYWEIGHT to Res.string.default_exercise_lying_leg_raises_bodyweight_name,
            LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET to Res.string.default_exercise_lying_leg_raises_dumbbell_between_feet_name,
            MEDICINE_BALL_OVERHEAD_SLAMS to Res.string.default_exercise_medicine_ball_overhead_slams_name,
            MEDICINE_BALL_RUSSIAN_TWISTS to Res.string.default_exercise_medicine_ball_russian_twists_name,
            MEDICINE_BALL_WOODCHOPPERS to Res.string.default_exercise_medicine_ball_woodchoppers_name,
            MOUNTAIN_CLIMBERS to Res.string.default_exercise_mountain_climbers_name,
            NEGATIVE_PULL_UPS to Res.string.default_exercise_negative_pull_ups_name,
            OVERHEAD_PRESS_BARBELL to Res.string.default_exercise_overhead_press_barbell_name,
            OVERHEAD_PRESS_BARBELL_POWER_RACK to Res.string.default_exercise_overhead_press_barbell_power_rack_name,
            OVERHEAD_PRESS_DUMBBELL to Res.string.default_exercise_overhead_press_dumbbell_name,
            OVERHEAD_PRESS_SMITH_MACHINE to Res.string.default_exercise_overhead_press_smith_machine_name,
            PLANK to Res.string.default_exercise_plank_name,
            PLATE_FRONT_RAISE to Res.string.default_exercise_plate_front_raise_name,
            PLATE_HALOS to Res.string.default_exercise_plate_halos_name,
            PLYO_BOX_STEP_UPS to Res.string.default_exercise_plyo_box_step_ups_name,
            PULL_UPS to Res.string.default_exercise_pull_ups_name,
            PUSH_UPS_BODYWEIGHT to Res.string.default_exercise_push_ups_bodyweight_name,
            PUSH_UPS_HANDLES to Res.string.default_exercise_push_ups_handles_name,
            RACK_PULLS_BARBELL_POWER_RACK to Res.string.default_exercise_rack_pulls_barbell_power_rack_name,
            REVERSE_CRUNCHES to Res.string.default_exercise_reverse_crunches_name,
            ROMANIAN_DEADLIFT_BARBELL to Res.string.default_exercise_romanian_deadlift_barbell_name,
            ROMANIAN_DEADLIFT_DUMBBELL to Res.string.default_exercise_romanian_deadlift_dumbbell_name,
            ROWING_MACHINE_CARDIO to Res.string.default_exercise_rowing_machine_cardio_name,
            SANDBAG_BEAR_HUG_SQUATS to Res.string.default_exercise_sandbag_bear_hug_squats_name,
            SANDBAG_CLEANS to Res.string.default_exercise_sandbag_cleans_name,
            SANDBAG_SHOULDER_TO_SHOULDER_PRESS to Res.string.default_exercise_sandbag_shoulder_to_shoulder_press_name,
            SEATED_ROW_MACHINE to Res.string.default_exercise_seated_row_machine_name,
            SIDE_LUNGES_BODYWEIGHT to Res.string.default_exercise_side_lunges_bodyweight_name,
            SIDE_LUNGES_DUMBBELL to Res.string.default_exercise_side_lunges_dumbbell_name,
            SIT_UPS to Res.string.default_exercise_sit_ups_name,
            SIT_UPS_WEIGHTED_DUMBBELL to Res.string.default_exercise_sit_ups_weighted_dumbbell_name,
            SIT_UPS_WEIGHTED_PLATE to Res.string.default_exercise_sit_ups_weighted_plate_name,
            SLAM_BALL_GROUND_TO_OVERHEAD to Res.string.default_exercise_slam_ball_ground_to_overhead_name,
            SLAM_BALL_OVER_SHOULDER_TOSS to Res.string.default_exercise_slam_ball_over_shoulder_toss_name,
            SQUAT_BARBELL to Res.string.default_exercise_squat_barbell_name,
            SQUAT_BARBELL_POWER_RACK to Res.string.default_exercise_squat_barbell_power_rack_name,
            SQUAT_BODYWEIGHT to Res.string.default_exercise_squat_bodyweight_name,
            SQUAT_DUMBBELL to Res.string.default_exercise_squat_dumbbell_name,
            SQUAT_SMITH_MACHINE to Res.string.default_exercise_squat_smith_machine_name,
            TREADMILL_RUNNING to Res.string.default_exercise_treadmill_running_name,
            TREADMILL_WALKING to Res.string.default_exercise_treadmill_walking_name,
            TRICEPS_DIPS_DIP_STATION to Res.string.default_exercise_triceps_dips_dip_station_name,
            WALL_BALL_SHOTS to Res.string.default_exercise_wall_ball_shots_name,
            WALL_BALL_SQUAT_AND_PRESS_NO_THROW to Res.string.default_exercise_wall_ball_squat_and_press_no_throw_name,
            WEIGHTED_DIPS_DIP_STATION to Res.string.default_exercise_weighted_dips_dip_station_name,
            WEIGHTED_PLANK_PLATE_ON_BACK to Res.string.default_exercise_weighted_plank_plate_on_back_name,
            WOODCHOPPERS_CABLE_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_cable_high_to_low_name,
            WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_dumbbell_high_to_low_name,
            WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_kettlebell_high_to_low_name,
            WRIST_WEIGHT_ARM_CIRCLES to Res.string.default_exercise_wrist_weight_arm_circles_name,
            WRIST_WEIGHT_SHADOW_BOXING to Res.string.default_exercise_wrist_weight_shadow_boxing_name
        )

    val exerciseGuideMap: Map<String, StringResource> =
        mapOf(
            AB_WHEEL_ROLLOUTS_KNEES to Res.string.default_exercise_ab_wheel_rollouts_knees_guide,
            AB_WHEEL_ROLLOUTS_STANDING to Res.string.default_exercise_ab_wheel_rollouts_standing_guide,
            AIR_BIKE_CARDIO to Res.string.default_exercise_air_bike_cardio_guide,
            ANKLE_WEIGHT_GLUTE_KICKBACKS to Res.string.default_exercise_ankle_weight_glute_kickbacks_guide,
            ANKLE_WEIGHT_LEG_RAISES to Res.string.default_exercise_ankle_weight_leg_raises_guide,
            ANKLE_WEIGHT_REVERSE_CRUNCHES to Res.string.default_exercise_ankle_weight_reverse_crunches_guide,
            AROUND_THE_WORLD_DUMBBELL to Res.string.default_exercise_around_the_world_dumbbell_guide,
            AROUND_THE_WORLD_PLATE to Res.string.default_exercise_around_the_world_plate_guide,
            ASSISTED_PULL_UPS to Res.string.default_exercise_assisted_pull_ups_guide,
            BATTLE_ROPES_WAVES to Res.string.default_exercise_battle_ropes_waves_guide,
            BENCH_PRESS_BARBELL to Res.string.default_exercise_bench_press_barbell_guide,
            BENCH_PRESS_BARBELL_POWER_RACK to Res.string.default_exercise_bench_press_barbell_power_rack_guide,
            BENCH_PRESS_DUMBBELL to Res.string.default_exercise_bench_press_dumbbell_guide,
            BENCH_PRESS_SMITH_MACHINE to Res.string.default_exercise_bench_press_smith_machine_guide,
            BICYCLE_CRUNCHES to Res.string.default_exercise_bicycle_crunches_guide,
            BOX_JUMPS to Res.string.default_exercise_box_jumps_guide,
            BOX_JUMPS_CALF_FOCUS to Res.string.default_exercise_box_jumps_calf_focus_guide,
            BURPEES to Res.string.default_exercise_burpees_guide,
            CABLE_CRUNCHES_KNEELING to Res.string.default_exercise_cable_crunches_kneeling_guide,
            CABLE_CRUNCHES_STANDING to Res.string.default_exercise_cable_crunches_standing_guide,
            CALF_RAISES_BARBELL_STANDING to Res.string.default_exercise_calf_raises_barbell_standing_guide,
            CALF_RAISES_BODYWEIGHT_SEATED to Res.string.default_exercise_calf_raises_bodyweight_seated_guide,
            CALF_RAISES_BODYWEIGHT_STANDING to Res.string.default_exercise_calf_raises_bodyweight_standing_guide,
            CALF_RAISES_DUMBBELL_SEATED to Res.string.default_exercise_calf_raises_dumbbell_seated_guide,
            CALF_RAISES_DUMBBELL_STANDING to Res.string.default_exercise_calf_raises_dumbbell_standing_guide,
            CALF_RAISES_SINGLE_LEG_BODYWEIGHT to Res.string.default_exercise_calf_raises_single_leg_bodyweight_guide,
            CALF_RAISES_SINGLE_LEG_DUMBBELL to Res.string.default_exercise_calf_raises_single_leg_dumbbell_guide,
            CALF_RAISES_SMITH_MACHINE_STANDING to Res.string.default_exercise_calf_raises_smith_machine_standing_guide,
            CAPTAINS_CHAIR_LEG_RAISES to Res.string.default_exercise_captains_chair_leg_raises_guide,
            CHEST_PRESS_MACHINE to Res.string.default_exercise_chest_press_machine_guide,
            CRUNCHES_BODYWEIGHT to Res.string.default_exercise_crunches_bodyweight_guide,
            CRUNCHES_WEIGHTED_PLATE to Res.string.default_exercise_crunches_weighted_plate_guide,
            DEADLIFT_BARBELL to Res.string.default_exercise_deadlift_barbell_guide,
            DECLINE_CRUNCHES to Res.string.default_exercise_decline_crunches_guide,
            DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX to Res.string.default_exercise_decline_push_ups_feet_on_plyo_box_guide,
            DIP_STATION_LEG_RAISES to Res.string.default_exercise_dip_station_leg_raises_guide,
            DUMBBELL_SIDE_BENDS to Res.string.default_exercise_dumbbell_side_bends_guide,
            EZ_BAR_CLOSE_GRIP_BENCH_PRESS to Res.string.default_exercise_ez_bar_close_grip_bench_press_guide,
            EZ_BAR_CURL to Res.string.default_exercise_ez_bar_curl_guide,
            EZ_BAR_PREACHER_CURL to Res.string.default_exercise_ez_bar_preacher_curl_guide,
            EZ_BAR_REVERSE_CURL to Res.string.default_exercise_ez_bar_reverse_curl_guide,
            EZ_BAR_SKULLCRUSHER to Res.string.default_exercise_ez_bar_skullcrusher_guide,
            EZ_BAR_UPRIGHT_ROW to Res.string.default_exercise_ez_bar_upright_row_guide,
            FLUTTER_KICKS to Res.string.default_exercise_flutter_kicks_guide,
            GOBLET_SQUAT_KETTLEBELL to Res.string.default_exercise_goblet_squat_kettlebell_guide,
            HANGING_KNEE_RAISES to Res.string.default_exercise_hanging_knee_raises_guide,
            HANGING_LEG_RAISES to Res.string.default_exercise_hanging_leg_raises_guide,
            HEEL_TOUCHES to Res.string.default_exercise_heel_touches_guide,
            INCLINE_BENCH_PRESS_BARBELL to Res.string.default_exercise_incline_bench_press_barbell_guide,
            INCLINE_BENCH_PRESS_DUMBBELL to Res.string.default_exercise_incline_bench_press_dumbbell_guide,
            INCLINE_REVERSE_CRUNCHES to Res.string.default_exercise_incline_reverse_crunches_guide,
            JUMP_ROPE to Res.string.default_exercise_jump_rope_guide,
            JUMPING_JACKS to Res.string.default_exercise_jumping_jacks_guide,
            KETTLEBELL_CLEAN_AND_PRESS to Res.string.default_exercise_kettlebell_clean_and_press_guide,
            KETTLEBELL_DEADLIFTS to Res.string.default_exercise_kettlebell_deadlifts_guide,
            KETTLEBELL_FARMERS_WALK to Res.string.default_exercise_kettlebell_farmers_walk_guide,
            KETTLEBELL_HALO to Res.string.default_exercise_kettlebell_halo_guide,
            KETTLEBELL_LUNGES to Res.string.default_exercise_kettlebell_lunges_guide,
            KETTLEBELL_RUSSIAN_TWISTS to Res.string.default_exercise_kettlebell_russian_twists_guide,
            KETTLEBELL_SINGLE_ARM_ROWS to Res.string.default_exercise_kettlebell_single_arm_rows_guide,
            KETTLEBELL_SNATCH to Res.string.default_exercise_kettlebell_snatch_guide,
            KETTLEBELL_SWINGS to Res.string.default_exercise_kettlebell_swings_guide,
            KETTLEBELL_TURKISH_GET_UP to Res.string.default_exercise_kettlebell_turkish_get_up_guide,
            KETTLEBELL_WINDMILL to Res.string.default_exercise_kettlebell_windmill_guide,
            LANDMINE_TWISTS_ATTACHMENT to Res.string.default_exercise_landmine_twists_attachment_guide,
            LANDMINE_TWISTS_CORNER to Res.string.default_exercise_landmine_twists_corner_guide,
            LUNGES_BARBELL to Res.string.default_exercise_lunges_barbell_guide,
            LUNGES_BODYWEIGHT to Res.string.default_exercise_lunges_bodyweight_guide,
            LUNGES_DUMBBELL to Res.string.default_exercise_lunges_dumbbell_guide,
            LYING_LEG_RAISES_BODYWEIGHT to Res.string.default_exercise_lying_leg_raises_bodyweight_guide,
            LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET to Res.string.default_exercise_lying_leg_raises_dumbbell_between_feet_guide,
            MEDICINE_BALL_OVERHEAD_SLAMS to Res.string.default_exercise_medicine_ball_overhead_slams_guide,
            MEDICINE_BALL_RUSSIAN_TWISTS to Res.string.default_exercise_medicine_ball_russian_twists_guide,
            MEDICINE_BALL_WOODCHOPPERS to Res.string.default_exercise_medicine_ball_woodchoppers_guide,
            MOUNTAIN_CLIMBERS to Res.string.default_exercise_mountain_climbers_guide,
            NEGATIVE_PULL_UPS to Res.string.default_exercise_negative_pull_ups_guide,
            OVERHEAD_PRESS_BARBELL to Res.string.default_exercise_overhead_press_barbell_guide,
            OVERHEAD_PRESS_BARBELL_POWER_RACK to Res.string.default_exercise_overhead_press_barbell_power_rack_guide,
            OVERHEAD_PRESS_DUMBBELL to Res.string.default_exercise_overhead_press_dumbbell_guide,
            OVERHEAD_PRESS_SMITH_MACHINE to Res.string.default_exercise_overhead_press_smith_machine_guide,
            PLANK to Res.string.default_exercise_plank_guide,
            PLATE_FRONT_RAISE to Res.string.default_exercise_plate_front_raise_guide,
            PLATE_HALOS to Res.string.default_exercise_plate_halos_guide,
            PLYO_BOX_STEP_UPS to Res.string.default_exercise_plyo_box_step_ups_guide,
            PULL_UPS to Res.string.default_exercise_pull_ups_guide,
            PUSH_UPS_BODYWEIGHT to Res.string.default_exercise_push_ups_bodyweight_guide,
            PUSH_UPS_HANDLES to Res.string.default_exercise_push_ups_handles_guide,
            RACK_PULLS_BARBELL_POWER_RACK to Res.string.default_exercise_rack_pulls_barbell_power_rack_guide,
            REVERSE_CRUNCHES to Res.string.default_exercise_reverse_crunches_guide,
            ROMANIAN_DEADLIFT_BARBELL to Res.string.default_exercise_romanian_deadlift_barbell_guide,
            ROMANIAN_DEADLIFT_DUMBBELL to Res.string.default_exercise_romanian_deadlift_dumbbell_guide,
            ROWING_MACHINE_CARDIO to Res.string.default_exercise_rowing_machine_cardio_guide,
            SANDBAG_BEAR_HUG_SQUATS to Res.string.default_exercise_sandbag_bear_hug_squats_guide,
            SANDBAG_CLEANS to Res.string.default_exercise_sandbag_cleans_guide,
            SANDBAG_SHOULDER_TO_SHOULDER_PRESS to Res.string.default_exercise_sandbag_shoulder_to_shoulder_press_guide,
            SEATED_ROW_MACHINE to Res.string.default_exercise_seated_row_machine_guide,
            SIDE_LUNGES_BODYWEIGHT to Res.string.default_exercise_side_lunges_bodyweight_guide,
            SIDE_LUNGES_DUMBBELL to Res.string.default_exercise_side_lunges_dumbbell_guide,
            SIT_UPS to Res.string.default_exercise_sit_ups_guide,
            SIT_UPS_WEIGHTED_DUMBBELL to Res.string.default_exercise_sit_ups_weighted_dumbbell_guide,
            SIT_UPS_WEIGHTED_PLATE to Res.string.default_exercise_sit_ups_weighted_plate_guide,
            SLAM_BALL_GROUND_TO_OVERHEAD to Res.string.default_exercise_slam_ball_ground_to_overhead_guide,
            SLAM_BALL_OVER_SHOULDER_TOSS to Res.string.default_exercise_slam_ball_over_shoulder_toss_guide,
            SQUAT_BARBELL to Res.string.default_exercise_squat_barbell_guide,
            SQUAT_BARBELL_POWER_RACK to Res.string.default_exercise_squat_barbell_power_rack_guide,
            SQUAT_BODYWEIGHT to Res.string.default_exercise_squat_bodyweight_guide,
            SQUAT_DUMBBELL to Res.string.default_exercise_squat_dumbbell_guide,
            SQUAT_SMITH_MACHINE to Res.string.default_exercise_squat_smith_machine_guide,
            TREADMILL_RUNNING to Res.string.default_exercise_treadmill_running_guide,
            TREADMILL_WALKING to Res.string.default_exercise_treadmill_walking_guide,
            TRICEPS_DIPS_DIP_STATION to Res.string.default_exercise_triceps_dips_dip_station_guide,
            WALL_BALL_SHOTS to Res.string.default_exercise_wall_ball_shots_guide,
            WALL_BALL_SQUAT_AND_PRESS_NO_THROW to Res.string.default_exercise_wall_ball_squat_and_press_no_throw_guide,
            WEIGHTED_DIPS_DIP_STATION to Res.string.default_exercise_weighted_dips_dip_station_guide,
            WEIGHTED_PLANK_PLATE_ON_BACK to Res.string.default_exercise_weighted_plank_plate_on_back_guide,
            WOODCHOPPERS_CABLE_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_cable_high_to_low_guide,
            WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_dumbbell_high_to_low_guide,
            WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW to Res.string.default_exercise_woodchoppers_kettlebell_high_to_low_guide,
            WRIST_WEIGHT_ARM_CIRCLES to Res.string.default_exercise_wrist_weight_arm_circles_guide,
            WRIST_WEIGHT_SHADOW_BOXING to Res.string.default_exercise_wrist_weight_shadow_boxing_guide
        )

    val exerciseImageMap: Map<String, DrawableResource> =
        mapOf(
            AB_WHEEL_ROLLOUTS_KNEES to Res.drawable.ic_launcher,
            AB_WHEEL_ROLLOUTS_STANDING to Res.drawable.ic_launcher,
            AIR_BIKE_CARDIO to Res.drawable.ic_launcher,
            ANKLE_WEIGHT_GLUTE_KICKBACKS to Res.drawable.ic_launcher,
            ANKLE_WEIGHT_LEG_RAISES to Res.drawable.ic_launcher,
            ANKLE_WEIGHT_REVERSE_CRUNCHES to Res.drawable.ic_launcher,
            AROUND_THE_WORLD_DUMBBELL to Res.drawable.ic_launcher,
            AROUND_THE_WORLD_PLATE to Res.drawable.ic_launcher,
            ASSISTED_PULL_UPS to Res.drawable.ic_launcher,
            BATTLE_ROPES_WAVES to Res.drawable.ic_launcher,
            BENCH_PRESS_BARBELL to Res.drawable.ic_launcher,
            BENCH_PRESS_BARBELL_POWER_RACK to Res.drawable.ic_launcher,
            BENCH_PRESS_DUMBBELL to Res.drawable.ic_launcher,
            BENCH_PRESS_SMITH_MACHINE to Res.drawable.ic_launcher,
            BICYCLE_CRUNCHES to Res.drawable.ic_launcher,
            BOX_JUMPS to Res.drawable.ic_launcher,
            BOX_JUMPS_CALF_FOCUS to Res.drawable.ic_launcher,
            BURPEES to Res.drawable.ic_launcher,
            CABLE_CRUNCHES_KNEELING to Res.drawable.ic_launcher,
            CABLE_CRUNCHES_STANDING to Res.drawable.ic_launcher,
            CALF_RAISES_BARBELL_STANDING to Res.drawable.ic_launcher,
            CALF_RAISES_BODYWEIGHT_SEATED to Res.drawable.ic_launcher,
            CALF_RAISES_BODYWEIGHT_STANDING to Res.drawable.ic_launcher,
            CALF_RAISES_DUMBBELL_SEATED to Res.drawable.ic_launcher,
            CALF_RAISES_DUMBBELL_STANDING to Res.drawable.ic_launcher,
            CALF_RAISES_SINGLE_LEG_BODYWEIGHT to Res.drawable.ic_launcher,
            CALF_RAISES_SINGLE_LEG_DUMBBELL to Res.drawable.ic_launcher,
            CALF_RAISES_SMITH_MACHINE_STANDING to Res.drawable.ic_launcher,
            CAPTAINS_CHAIR_LEG_RAISES to Res.drawable.ic_launcher,
            CHEST_PRESS_MACHINE to Res.drawable.ic_launcher,
            CRUNCHES_BODYWEIGHT to Res.drawable.ic_launcher,
            CRUNCHES_WEIGHTED_PLATE to Res.drawable.ic_launcher,
            DEADLIFT_BARBELL to Res.drawable.ic_launcher,
            DECLINE_CRUNCHES to Res.drawable.ic_launcher,
            DECLINE_PUSH_UPS_FEET_ON_PLYO_BOX to Res.drawable.ic_launcher,
            DIP_STATION_LEG_RAISES to Res.drawable.ic_launcher,
            DUMBBELL_SIDE_BENDS to Res.drawable.ic_launcher,
            EZ_BAR_CLOSE_GRIP_BENCH_PRESS to Res.drawable.ic_launcher,
            EZ_BAR_CURL to Res.drawable.ic_launcher,
            EZ_BAR_PREACHER_CURL to Res.drawable.ic_launcher,
            EZ_BAR_REVERSE_CURL to Res.drawable.ic_launcher,
            EZ_BAR_SKULLCRUSHER to Res.drawable.ic_launcher,
            EZ_BAR_UPRIGHT_ROW to Res.drawable.ic_launcher,
            FLUTTER_KICKS to Res.drawable.ic_launcher,
            GOBLET_SQUAT_KETTLEBELL to Res.drawable.ic_launcher,
            HANGING_KNEE_RAISES to Res.drawable.ic_launcher,
            HANGING_LEG_RAISES to Res.drawable.ic_launcher,
            HEEL_TOUCHES to Res.drawable.ic_launcher,
            INCLINE_BENCH_PRESS_BARBELL to Res.drawable.ic_launcher,
            INCLINE_BENCH_PRESS_DUMBBELL to Res.drawable.ic_launcher,
            INCLINE_REVERSE_CRUNCHES to Res.drawable.ic_launcher,
            JUMP_ROPE to Res.drawable.ic_launcher,
            JUMPING_JACKS to Res.drawable.ic_launcher,
            KETTLEBELL_CLEAN_AND_PRESS to Res.drawable.ic_launcher,
            KETTLEBELL_DEADLIFTS to Res.drawable.ic_launcher,
            KETTLEBELL_FARMERS_WALK to Res.drawable.ic_launcher,
            KETTLEBELL_HALO to Res.drawable.ic_launcher,
            KETTLEBELL_LUNGES to Res.drawable.ic_launcher,
            KETTLEBELL_RUSSIAN_TWISTS to Res.drawable.ic_launcher,
            KETTLEBELL_SINGLE_ARM_ROWS to Res.drawable.ic_launcher,
            KETTLEBELL_SNATCH to Res.drawable.ic_launcher,
            KETTLEBELL_SWINGS to Res.drawable.ic_launcher,
            KETTLEBELL_TURKISH_GET_UP to Res.drawable.ic_launcher,
            KETTLEBELL_WINDMILL to Res.drawable.ic_launcher,
            LANDMINE_TWISTS_ATTACHMENT to Res.drawable.ic_launcher,
            LANDMINE_TWISTS_CORNER to Res.drawable.ic_launcher,
            LUNGES_BARBELL to Res.drawable.ic_launcher,
            LUNGES_BODYWEIGHT to Res.drawable.ic_launcher,
            LUNGES_DUMBBELL to Res.drawable.ic_launcher,
            LYING_LEG_RAISES_BODYWEIGHT to Res.drawable.ic_launcher,
            LYING_LEG_RAISES_DUMBBELL_BETWEEN_FEET to Res.drawable.ic_launcher,
            MEDICINE_BALL_OVERHEAD_SLAMS to Res.drawable.ic_launcher,
            MEDICINE_BALL_RUSSIAN_TWISTS to Res.drawable.ic_launcher,
            MEDICINE_BALL_WOODCHOPPERS to Res.drawable.ic_launcher,
            MOUNTAIN_CLIMBERS to Res.drawable.ic_launcher,
            NEGATIVE_PULL_UPS to Res.drawable.ic_launcher,
            OVERHEAD_PRESS_BARBELL to Res.drawable.ic_launcher,
            OVERHEAD_PRESS_BARBELL_POWER_RACK to Res.drawable.ic_launcher,
            OVERHEAD_PRESS_DUMBBELL to Res.drawable.ic_launcher,
            OVERHEAD_PRESS_SMITH_MACHINE to Res.drawable.ic_launcher,
            PLANK to Res.drawable.ic_launcher,
            PLATE_FRONT_RAISE to Res.drawable.ic_launcher,
            PLATE_HALOS to Res.drawable.ic_launcher,
            PLYO_BOX_STEP_UPS to Res.drawable.ic_launcher,
            PULL_UPS to Res.drawable.ic_launcher,
            PUSH_UPS_BODYWEIGHT to Res.drawable.ic_launcher,
            PUSH_UPS_HANDLES to Res.drawable.ic_launcher,
            RACK_PULLS_BARBELL_POWER_RACK to Res.drawable.ic_launcher,
            REVERSE_CRUNCHES to Res.drawable.ic_launcher,
            ROMANIAN_DEADLIFT_BARBELL to Res.drawable.ic_launcher,
            ROMANIAN_DEADLIFT_DUMBBELL to Res.drawable.ic_launcher,
            ROWING_MACHINE_CARDIO to Res.drawable.ic_launcher,
            SANDBAG_BEAR_HUG_SQUATS to Res.drawable.ic_launcher,
            SANDBAG_CLEANS to Res.drawable.ic_launcher,
            SANDBAG_SHOULDER_TO_SHOULDER_PRESS to Res.drawable.ic_launcher,
            SEATED_ROW_MACHINE to Res.drawable.ic_launcher,
            SIDE_LUNGES_BODYWEIGHT to Res.drawable.ic_launcher,
            SIDE_LUNGES_DUMBBELL to Res.drawable.ic_launcher,
            SIT_UPS to Res.drawable.ic_launcher,
            SIT_UPS_WEIGHTED_DUMBBELL to Res.drawable.ic_launcher,
            SIT_UPS_WEIGHTED_PLATE to Res.drawable.ic_launcher,
            SLAM_BALL_GROUND_TO_OVERHEAD to Res.drawable.ic_launcher,
            SLAM_BALL_OVER_SHOULDER_TOSS to Res.drawable.ic_launcher,
            SQUAT_BARBELL to Res.drawable.ic_launcher,
            SQUAT_BARBELL_POWER_RACK to Res.drawable.ic_launcher,
            SQUAT_BODYWEIGHT to Res.drawable.ic_launcher,
            SQUAT_DUMBBELL to Res.drawable.ic_launcher,
            SQUAT_SMITH_MACHINE to Res.drawable.ic_launcher,
            TREADMILL_RUNNING to Res.drawable.ic_launcher,
            TREADMILL_WALKING to Res.drawable.ic_launcher,
            TRICEPS_DIPS_DIP_STATION to Res.drawable.ic_launcher,
            WALL_BALL_SHOTS to Res.drawable.ic_launcher,
            WALL_BALL_SQUAT_AND_PRESS_NO_THROW to Res.drawable.ic_launcher,
            WEIGHTED_DIPS_DIP_STATION to Res.drawable.ic_launcher,
            WEIGHTED_PLANK_PLATE_ON_BACK to Res.drawable.ic_launcher,
            WOODCHOPPERS_CABLE_HIGH_TO_LOW to Res.drawable.ic_launcher,
            WOODCHOPPERS_DUMBBELL_HIGH_TO_LOW to Res.drawable.ic_launcher,
            WOODCHOPPERS_KETTLEBELL_HIGH_TO_LOW to Res.drawable.ic_launcher,
            WRIST_WEIGHT_ARM_CIRCLES to Res.drawable.ic_launcher,
            WRIST_WEIGHT_SHADOW_BOXING to Res.drawable.ic_launcher
        )
}