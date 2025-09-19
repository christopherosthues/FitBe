package org.darthacheron.fitbe.workouts.equipment

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_training_equipment_ab_wheel // Added
import fitbe.composeapp.generated.resources.default_training_equipment_air_bike
import fitbe.composeapp.generated.resources.default_training_equipment_ankle_weights
import fitbe.composeapp.generated.resources.default_training_equipment_balance_pad
import fitbe.composeapp.generated.resources.default_training_equipment_barbell
import fitbe.composeapp.generated.resources.default_training_equipment_battle_ropes
import fitbe.composeapp.generated.resources.default_training_equipment_bench
import fitbe.composeapp.generated.resources.default_training_equipment_bodyweight
import fitbe.composeapp.generated.resources.default_training_equipment_bosu_ball
import fitbe.composeapp.generated.resources.default_training_equipment_bulgarian_bag
import fitbe.composeapp.generated.resources.default_training_equipment_cable_machine
import fitbe.composeapp.generated.resources.default_training_equipment_chest_press_machine
import fitbe.composeapp.generated.resources.default_training_equipment_dip_bars
import fitbe.composeapp.generated.resources.default_training_equipment_dip_station
import fitbe.composeapp.generated.resources.default_training_equipment_dumbbell
import fitbe.composeapp.generated.resources.default_training_equipment_elliptical_trainer
import fitbe.composeapp.generated.resources.default_training_equipment_ez_curl_bar
import fitbe.composeapp.generated.resources.default_training_equipment_foam_roller
import fitbe.composeapp.generated.resources.default_training_equipment_gymnastic_rings
import fitbe.composeapp.generated.resources.default_training_equipment_jump_rope // Added
import fitbe.composeapp.generated.resources.default_training_equipment_kettlebell
import fitbe.composeapp.generated.resources.default_training_equipment_lat_pulldown_machine
import fitbe.composeapp.generated.resources.default_training_equipment_leg_curl_machine
import fitbe.composeapp.generated.resources.default_training_equipment_leg_extension_machine
import fitbe.composeapp.generated.resources.default_training_equipment_leg_press_machine
import fitbe.composeapp.generated.resources.default_training_equipment_medicine_ball
import fitbe.composeapp.generated.resources.default_training_equipment_parallettes
import fitbe.composeapp.generated.resources.default_training_equipment_plyo_box
import fitbe.composeapp.generated.resources.default_training_equipment_power_rack
import fitbe.composeapp.generated.resources.default_training_equipment_pull_up_bar
import fitbe.composeapp.generated.resources.default_training_equipment_push_up_handles
import fitbe.composeapp.generated.resources.default_training_equipment_resistance_bands
import fitbe.composeapp.generated.resources.default_training_equipment_rowing_machine
import fitbe.composeapp.generated.resources.default_training_equipment_sandbag
import fitbe.composeapp.generated.resources.default_training_equipment_seated_row_machine
import fitbe.composeapp.generated.resources.default_training_equipment_shoulder_press_machine
import fitbe.composeapp.generated.resources.default_training_equipment_slam_ball
import fitbe.composeapp.generated.resources.default_training_equipment_smith_machine
import fitbe.composeapp.generated.resources.default_training_equipment_spotter_arms
import fitbe.composeapp.generated.resources.default_training_equipment_squat_rack
import fitbe.composeapp.generated.resources.default_training_equipment_stair_climber
import fitbe.composeapp.generated.resources.default_training_equipment_stationary_bike
import fitbe.composeapp.generated.resources.default_training_equipment_suspension_trainer
import fitbe.composeapp.generated.resources.default_training_equipment_treadmill
import fitbe.composeapp.generated.resources.default_training_equipment_wall_ball
import fitbe.composeapp.generated.resources.default_training_equipment_weight_plates
import fitbe.composeapp.generated.resources.default_training_equipment_wrist_weights
import fitbe.composeapp.generated.resources.default_training_equipment_yoga_mat // Added
import fitbe.composeapp.generated.resources.ic_default_training_equipment_barbell
import fitbe.composeapp.generated.resources.ic_default_training_equipment_bench
import fitbe.composeapp.generated.resources.ic_default_training_equipment_bodyweight
import fitbe.composeapp.generated.resources.ic_default_training_equipment_dumbbell
import fitbe.composeapp.generated.resources.ic_default_training_equipment_ez_curl_bar
import fitbe.composeapp.generated.resources.ic_default_training_equipment_kettlebell
import fitbe.composeapp.generated.resources.ic_default_training_equipment_medicine_ball
import fitbe.composeapp.generated.resources.ic_default_training_equipment_power_rack
import fitbe.composeapp.generated.resources.ic_default_training_equipment_pull_up_bar
import fitbe.composeapp.generated.resources.ic_default_training_equipment_rowing_machine
import fitbe.composeapp.generated.resources.ic_default_training_equipment_sandbag
import fitbe.composeapp.generated.resources.ic_default_training_equipment_seated_row_machine
import fitbe.composeapp.generated.resources.ic_default_training_equipment_squat_rack
import fitbe.composeapp.generated.resources.ic_default_training_equipment_treadmill
import fitbe.composeapp.generated.resources.ic_default_training_equipment_weight_plates
import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DefaultEquipmentResProvider {
    val equipmentNameMap: Map<String, StringResource> = mapOf(
        "default_training_equipment_barbell" to Res.string.default_training_equipment_barbell,
        "default_training_equipment_dumbbell" to Res.string.default_training_equipment_dumbbell,
        "default_training_equipment_bodyweight" to Res.string.default_training_equipment_bodyweight,
        "default_training_equipment_kettlebell" to Res.string.default_training_equipment_kettlebell,
        "default_training_equipment_dip_station" to Res.string.default_training_equipment_dip_station,
        "default_training_equipment_bench" to Res.string.default_training_equipment_bench,
        "default_training_equipment_rowing_machine" to Res.string.default_training_equipment_rowing_machine,
        "default_training_equipment_chest_press_machine" to Res.string.default_training_equipment_chest_press_machine,
        "default_training_equipment_ez_curl_bar" to Res.string.default_training_equipment_ez_curl_bar,
        "default_training_equipment_weight_plates" to Res.string.default_training_equipment_weight_plates,
        "default_training_equipment_ankle_weights" to Res.string.default_training_equipment_ankle_weights,
        "default_training_equipment_wrist_weights" to Res.string.default_training_equipment_wrist_weights,
        "default_training_equipment_cable_machine" to Res.string.default_training_equipment_cable_machine,
        "default_training_equipment_lat_pulldown_machine" to Res.string.default_training_equipment_lat_pulldown_machine,
        "default_training_equipment_leg_press_machine" to Res.string.default_training_equipment_leg_press_machine,
        "default_training_equipment_leg_extension_machine" to Res.string.default_training_equipment_leg_extension_machine,
        "default_training_equipment_leg_curl_machine" to Res.string.default_training_equipment_leg_curl_machine,
        "default_training_equipment_smith_machine" to Res.string.default_training_equipment_smith_machine,
        "default_training_equipment_shoulder_press_machine" to Res.string.default_training_equipment_shoulder_press_machine,
        "default_training_equipment_seated_row_machine" to Res.string.default_training_equipment_seated_row_machine,
        "default_training_equipment_pull_up_bar" to Res.string.default_training_equipment_pull_up_bar,
        "default_training_equipment_dip_bars" to Res.string.default_training_equipment_dip_bars,
        "default_training_equipment_push_up_handles" to Res.string.default_training_equipment_push_up_handles,
        "default_training_equipment_parallettes" to Res.string.default_training_equipment_parallettes,
        "default_training_equipment_gymnastic_rings" to Res.string.default_training_equipment_gymnastic_rings,
        "default_training_equipment_suspension_trainer" to Res.string.default_training_equipment_suspension_trainer,
        "default_training_equipment_plyo_box" to Res.string.default_training_equipment_plyo_box,
        "default_training_equipment_medicine_ball" to Res.string.default_training_equipment_medicine_ball,
        "default_training_equipment_slam_ball" to Res.string.default_training_equipment_slam_ball,
        "default_training_equipment_wall_ball" to Res.string.default_training_equipment_wall_ball,
        "default_training_equipment_battle_ropes" to Res.string.default_training_equipment_battle_ropes,
        "default_training_equipment_sandbag" to Res.string.default_training_equipment_sandbag,
        "default_training_equipment_bulgarian_bag" to Res.string.default_training_equipment_bulgarian_bag,
        "default_training_equipment_resistance_bands" to Res.string.default_training_equipment_resistance_bands,
        "default_training_equipment_foam_roller" to Res.string.default_training_equipment_foam_roller,
        "default_training_equipment_balance_pad" to Res.string.default_training_equipment_balance_pad,
        "default_training_equipment_bosu_ball" to Res.string.default_training_equipment_bosu_ball,
        "default_training_equipment_treadmill" to Res.string.default_training_equipment_treadmill,
        "default_training_equipment_elliptical_trainer" to Res.string.default_training_equipment_elliptical_trainer,
        "default_training_equipment_stationary_bike" to Res.string.default_training_equipment_stationary_bike,
        "default_training_equipment_stair_climber" to Res.string.default_training_equipment_stair_climber,
        "default_training_equipment_air_bike" to Res.string.default_training_equipment_air_bike,
        "default_training_equipment_power_rack" to Res.string.default_training_equipment_power_rack,
        "default_training_equipment_squat_rack" to Res.string.default_training_equipment_squat_rack,
        "default_training_equipment_spotter_arms" to Res.string.default_training_equipment_spotter_arms,
        "default_training_equipment_yoga_mat" to Res.string.default_training_equipment_yoga_mat,
        "default_training_equipment_jump_rope" to Res.string.default_training_equipment_jump_rope,
        "default_training_equipment_ab_wheel" to Res.string.default_training_equipment_ab_wheel
    )

    val equipmentImageMap: Map<String, DrawableResource?> = mapOf(
        "default_training_equipment_air_bike" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_air_bike,
        "default_training_equipment_ankle_weights" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_ankle_weights,
        "default_training_equipment_balance_pad" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_balance_pad,
        "default_training_equipment_barbell" to Res.drawable.ic_default_training_equipment_barbell,
        "default_training_equipment_battle_ropes" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_battle_ropes,
        "default_training_equipment_bench" to Res.drawable.ic_default_training_equipment_bench,
        "default_training_equipment_bodyweight" to Res.drawable.ic_default_training_equipment_bodyweight,
        "default_training_equipment_bosu_ball" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_bosu_ball,
        "default_training_equipment_bulgarian_bag" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_bulgarian_bag,
        "default_training_equipment_cable_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_cable_machine,
        "default_training_equipment_chest_press_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_chest_press_machine,
        "default_training_equipment_dip_bars" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_dip_bars,
        "default_training_equipment_dip_station" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_dip_station,
        "default_training_equipment_dumbbell" to Res.drawable.ic_default_training_equipment_dumbbell,
        "default_training_equipment_elliptical_trainer" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_elliptical_trainer,
        "default_training_equipment_ez_curl_bar" to Res.drawable.ic_default_training_equipment_ez_curl_bar,
        "default_training_equipment_foam_roller" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_foam_roller,
        "default_training_equipment_gymnastic_rings" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_gymnastic_rings,
        "default_training_equipment_kettlebell" to Res.drawable.ic_default_training_equipment_kettlebell,
        "default_training_equipment_lat_pulldown_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_lat_pulldown_machine,
        "default_training_equipment_leg_curl_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_leg_curl_machine,
        "default_training_equipment_leg_extension_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_leg_extension_machine,
        "default_training_equipment_leg_press_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_leg_press_machine,
        "default_training_equipment_medicine_ball" to Res.drawable.ic_default_training_equipment_medicine_ball,
        "default_training_equipment_parallettes" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_parallettes,
        "default_training_equipment_plyo_box" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_plyo_box,
        "default_training_equipment_power_rack" to Res.drawable.ic_default_training_equipment_power_rack,
        "default_training_equipment_pull_up_bar" to Res.drawable.ic_default_training_equipment_pull_up_bar,
        "default_training_equipment_push_up_handles" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_push_up_handles,
        "default_training_equipment_resistance_bands" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_resistance_bands,
        "default_training_equipment_rowing_machine" to Res.drawable.ic_default_training_equipment_rowing_machine,
        "default_training_equipment_sandbag" to Res.drawable.ic_default_training_equipment_sandbag,
        "default_training_equipment_seated_row_machine" to Res.drawable.ic_default_training_equipment_seated_row_machine,
        "default_training_equipment_shoulder_press_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_shoulder_press_machine,
        "default_training_equipment_slam_ball" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_slam_ball,
        "default_training_equipment_smith_machine" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_smith_machine,
        "default_training_equipment_spotter_arms" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_spotter_arms
        "default_training_equipment_squat_rack" to Res.drawable.ic_default_training_equipment_squat_rack,
        "default_training_equipment_stair_climber" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_stair_climber,
        "default_training_equipment_stationary_bike" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_stationary_bike,
        "default_training_equipment_suspension_trainer" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_suspension_trainer,
        "default_training_equipment_treadmill" to Res.drawable.ic_default_training_equipment_treadmill,
        "default_training_equipment_wall_ball" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_wall_ball,
        "default_training_equipment_weight_plates" to Res.drawable.ic_default_training_equipment_weight_plates,
        "default_training_equipment_wrist_weights" to Res.drawable.ic_launcher, // Res.drawable.ic_default_training_equipment_wrist_weights,
        "default_training_equipment_yoga_mat" to Res.drawable.ic_launcher,
        "default_training_equipment_jump_rope" to Res.drawable.ic_launcher,
        "default_training_equipment_ab_wheel" to Res.drawable.ic_launcher
    )
}
