package org.darthacheron.fitbe.workouts.equipment

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.default_training_equipment_ab_wheel
import fitbe.composeapp.generated.resources.default_training_equipment_air_bike
import fitbe.composeapp.generated.resources.default_training_equipment_ankle_weights
import fitbe.composeapp.generated.resources.default_training_equipment_back_extension_machine
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
import fitbe.composeapp.generated.resources.default_training_equipment_jump_rope
import fitbe.composeapp.generated.resources.default_training_equipment_kettlebell
import fitbe.composeapp.generated.resources.default_training_equipment_landmine_attachment
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
import fitbe.composeapp.generated.resources.default_training_equipment_seated_back_extension_machine
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
import fitbe.composeapp.generated.resources.default_training_equipment_yoga_mat
import fitbe.composeapp.generated.resources.ic_default_training_equipment_ab_wheel
import fitbe.composeapp.generated.resources.ic_default_training_equipment_air_bike
import fitbe.composeapp.generated.resources.ic_default_training_equipment_ankle_weights
import fitbe.composeapp.generated.resources.ic_default_training_equipment_balance_pad
import fitbe.composeapp.generated.resources.ic_default_training_equipment_barbell
import fitbe.composeapp.generated.resources.ic_default_training_equipment_battle_ropes
import fitbe.composeapp.generated.resources.ic_default_training_equipment_bench
import fitbe.composeapp.generated.resources.ic_default_training_equipment_bodyweight
import fitbe.composeapp.generated.resources.ic_default_training_equipment_bosu_ball
import fitbe.composeapp.generated.resources.ic_default_training_equipment_chest_press_machine
import fitbe.composeapp.generated.resources.ic_default_training_equipment_dumbbell
import fitbe.composeapp.generated.resources.ic_default_training_equipment_ez_curl_bar
import fitbe.composeapp.generated.resources.ic_default_training_equipment_foam_roller
import fitbe.composeapp.generated.resources.ic_default_training_equipment_gymnastic_rings
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
import fitbe.composeapp.generated.resources.ic_default_traning_equipment_jump_rope
import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object DefaultEquipmentResProvider {
    const val AB_WHEEL = "default_training_equipment_ab_wheel"
    const val AIR_BIKE = "default_training_equipment_air_bike"
    const val ANKLE_WEIGHTS = "default_training_equipment_ankle_weights"
    const val BACK_EXTENSION_MACHINE = "default_training_equipment_back_extension_machine"
    const val BALANCE_PAD = "default_training_equipment_balance_pad"
    const val BARBELL = "default_training_equipment_barbell"
    const val BATTLE_ROPES = "default_training_equipment_battle_ropes"
    const val BENCH = "default_training_equipment_bench"
    const val BODYWEIGHT = "default_training_equipment_bodyweight"
    const val BOSU_BALL = "default_training_equipment_bosu_ball"
    const val BULGARIAN_BAG = "default_training_equipment_bulgarian_bag"
    const val CABLE_MACHINE = "default_training_equipment_cable_machine"
    const val CHEST_PRESS_MACHINE = "default_training_equipment_chest_press_machine"
    const val DIP_BARS = "default_training_equipment_dip_bars"
    const val DIP_STATION = "default_training_equipment_dip_station"
    const val DUMBBELL = "default_training_equipment_dumbbell"
    const val ELLIPTICAL_TRAINER = "default_training_equipment_elliptical_trainer"
    const val EZ_CURL_BAR = "default_training_equipment_ez_curl_bar"
    const val FOAM_ROLLER = "default_training_equipment_foam_roller"
    const val GYMNASTIC_RINGS = "default_training_equipment_gymnastic_rings"
    const val JUMP_ROPE = "default_training_equipment_jump_rope"
    const val KETTLEBELL = "default_training_equipment_kettlebell"
    const val LANDMINE_ATTACHMENT = "default_training_equipment_landmine_attachment"
    const val LAT_PULLDOWN_MACHINE = "default_training_equipment_lat_pulldown_machine"
    const val LEG_CURL_MACHINE = "default_training_equipment_leg_curl_machine"
    const val LEG_EXTENSION_MACHINE = "default_training_equipment_leg_extension_machine"
    const val LEG_PRESS_MACHINE = "default_training_equipment_leg_press_machine"
    const val MEDICINE_BALL = "default_training_equipment_medicine_ball"
    const val PARALLETTES = "default_training_equipment_parallettes"
    const val PLYO_BOX = "default_training_equipment_plyo_box"
    const val POWER_RACK = "default_training_equipment_power_rack"
    const val PULL_UP_BAR = "default_training_equipment_pull_up_bar"
    const val PUSH_UP_HANDLES = "default_training_equipment_push_up_handles"
    const val RESISTANCE_BANDS = "default_training_equipment_resistance_bands"
    const val ROWING_MACHINE = "default_training_equipment_rowing_machine"
    const val SANDBAG = "default_training_equipment_sandbag"
    const val SEATED_BACK_EXTENSION_MACHINE = "default_training_equipment_seated_back_extension_machine"
    const val SEATED_ROW_MACHINE = "default_training_equipment_seated_row_machine"
    const val SHOULDER_PRESS_MACHINE = "default_training_equipment_shoulder_press_machine"
    const val SLAM_BALL = "default_training_equipment_slam_ball"
    const val SMITH_MACHINE = "default_training_equipment_smith_machine"
    const val SPOTTER_ARMS = "default_training_equipment_spotter_arms"
    const val SQUAT_RACK = "default_training_equipment_squat_rack"
    const val STAIR_CLIMBER = "default_training_equipment_stair_climber"
    const val STATIONARY_BIKE = "default_training_equipment_stationary_bike"
    const val SUSPENSION_TRAINER = "default_training_equipment_suspension_trainer"
    const val TREADMILL = "default_training_equipment_treadmill"
    const val WALL_BALL = "default_training_equipment_wall_ball"
    const val WEIGHT_PLATES = "default_training_equipment_weight_plates"
    const val WRIST_WEIGHTS = "default_training_equipment_wrist_weights"
    const val YOGA_MAT = "default_training_equipment_yoga_mat"

    val equipmentNameMap: Map<String, StringResource> =
        mapOf(
            AB_WHEEL to Res.string.default_training_equipment_ab_wheel,
            AIR_BIKE to Res.string.default_training_equipment_air_bike,
            ANKLE_WEIGHTS to Res.string.default_training_equipment_ankle_weights,
            BACK_EXTENSION_MACHINE to Res.string.default_training_equipment_back_extension_machine,
            BALANCE_PAD to Res.string.default_training_equipment_balance_pad,
            BARBELL to Res.string.default_training_equipment_barbell,
            BATTLE_ROPES to Res.string.default_training_equipment_battle_ropes,
            BENCH to Res.string.default_training_equipment_bench,
            BODYWEIGHT to Res.string.default_training_equipment_bodyweight,
            BOSU_BALL to Res.string.default_training_equipment_bosu_ball,
            BULGARIAN_BAG to Res.string.default_training_equipment_bulgarian_bag,
            CABLE_MACHINE to Res.string.default_training_equipment_cable_machine,
            CHEST_PRESS_MACHINE to Res.string.default_training_equipment_chest_press_machine,
            DIP_BARS to Res.string.default_training_equipment_dip_bars,
            DIP_STATION to Res.string.default_training_equipment_dip_station,
            DUMBBELL to Res.string.default_training_equipment_dumbbell,
            ELLIPTICAL_TRAINER to Res.string.default_training_equipment_elliptical_trainer,
            EZ_CURL_BAR to Res.string.default_training_equipment_ez_curl_bar,
            FOAM_ROLLER to Res.string.default_training_equipment_foam_roller,
            GYMNASTIC_RINGS to Res.string.default_training_equipment_gymnastic_rings,
            JUMP_ROPE to Res.string.default_training_equipment_jump_rope,
            KETTLEBELL to Res.string.default_training_equipment_kettlebell,
            LANDMINE_ATTACHMENT to Res.string.default_training_equipment_landmine_attachment,
            LAT_PULLDOWN_MACHINE to Res.string.default_training_equipment_lat_pulldown_machine,
            LEG_CURL_MACHINE to Res.string.default_training_equipment_leg_curl_machine,
            LEG_EXTENSION_MACHINE to Res.string.default_training_equipment_leg_extension_machine,
            LEG_PRESS_MACHINE to Res.string.default_training_equipment_leg_press_machine,
            MEDICINE_BALL to Res.string.default_training_equipment_medicine_ball,
            PARALLETTES to Res.string.default_training_equipment_parallettes,
            PLYO_BOX to Res.string.default_training_equipment_plyo_box,
            POWER_RACK to Res.string.default_training_equipment_power_rack,
            PULL_UP_BAR to Res.string.default_training_equipment_pull_up_bar,
            PUSH_UP_HANDLES to Res.string.default_training_equipment_push_up_handles,
            RESISTANCE_BANDS to Res.string.default_training_equipment_resistance_bands,
            ROWING_MACHINE to Res.string.default_training_equipment_rowing_machine,
            SANDBAG to Res.string.default_training_equipment_sandbag,
            SEATED_BACK_EXTENSION_MACHINE to Res.string.default_training_equipment_seated_back_extension_machine,
            SEATED_ROW_MACHINE to Res.string.default_training_equipment_seated_row_machine,
            SHOULDER_PRESS_MACHINE to Res.string.default_training_equipment_shoulder_press_machine,
            SLAM_BALL to Res.string.default_training_equipment_slam_ball,
            SMITH_MACHINE to Res.string.default_training_equipment_smith_machine,
            SPOTTER_ARMS to Res.string.default_training_equipment_spotter_arms,
            SQUAT_RACK to Res.string.default_training_equipment_squat_rack,
            STAIR_CLIMBER to Res.string.default_training_equipment_stair_climber,
            STATIONARY_BIKE to Res.string.default_training_equipment_stationary_bike,
            SUSPENSION_TRAINER to Res.string.default_training_equipment_suspension_trainer,
            TREADMILL to Res.string.default_training_equipment_treadmill,
            WALL_BALL to Res.string.default_training_equipment_wall_ball,
            WEIGHT_PLATES to Res.string.default_training_equipment_weight_plates,
            WRIST_WEIGHTS to Res.string.default_training_equipment_wrist_weights,
            YOGA_MAT to Res.string.default_training_equipment_yoga_mat
        )

    val equipmentImageMap: Map<String, DrawableResource?> =
        mapOf(
            AB_WHEEL to Res.drawable.ic_default_training_equipment_ab_wheel,
            AIR_BIKE to Res.drawable.ic_default_training_equipment_air_bike,
            ANKLE_WEIGHTS to Res.drawable.ic_default_training_equipment_ankle_weights,
            BACK_EXTENSION_MACHINE to Res.drawable.ic_launcher,
            BALANCE_PAD to Res.drawable.ic_default_training_equipment_balance_pad,
            BARBELL to Res.drawable.ic_default_training_equipment_barbell,
            BATTLE_ROPES to Res.drawable.ic_default_training_equipment_battle_ropes,
            BENCH to Res.drawable.ic_default_training_equipment_bench,
            BODYWEIGHT to Res.drawable.ic_default_training_equipment_bodyweight,
            BOSU_BALL to Res.drawable.ic_default_training_equipment_bosu_ball,
            BULGARIAN_BAG to Res.drawable.ic_launcher,
            CABLE_MACHINE to Res.drawable.ic_launcher,
            CHEST_PRESS_MACHINE to Res.drawable.ic_default_training_equipment_chest_press_machine,
            DIP_BARS to Res.drawable.ic_launcher,
            DIP_STATION to Res.drawable.ic_launcher,
            DUMBBELL to Res.drawable.ic_default_training_equipment_dumbbell,
            ELLIPTICAL_TRAINER to Res.drawable.ic_launcher,
            EZ_CURL_BAR to Res.drawable.ic_default_training_equipment_ez_curl_bar,
            FOAM_ROLLER to Res.drawable.ic_default_training_equipment_foam_roller,
            GYMNASTIC_RINGS to Res.drawable.ic_default_training_equipment_gymnastic_rings,
            JUMP_ROPE to Res.drawable.ic_default_traning_equipment_jump_rope,
            KETTLEBELL to Res.drawable.ic_default_training_equipment_kettlebell,
            LANDMINE_ATTACHMENT to Res.drawable.ic_launcher,
            LAT_PULLDOWN_MACHINE to Res.drawable.ic_launcher,
            LEG_CURL_MACHINE to Res.drawable.ic_launcher,
            LEG_EXTENSION_MACHINE to Res.drawable.ic_launcher,
            LEG_PRESS_MACHINE to Res.drawable.ic_launcher,
            MEDICINE_BALL to Res.drawable.ic_default_training_equipment_medicine_ball,
            PARALLETTES to Res.drawable.ic_launcher,
            PLYO_BOX to Res.drawable.ic_launcher,
            POWER_RACK to Res.drawable.ic_default_training_equipment_power_rack,
            PULL_UP_BAR to Res.drawable.ic_default_training_equipment_pull_up_bar,
            PUSH_UP_HANDLES to Res.drawable.ic_launcher,
            RESISTANCE_BANDS to Res.drawable.ic_launcher,
            ROWING_MACHINE to Res.drawable.ic_default_training_equipment_rowing_machine,
            SANDBAG to Res.drawable.ic_default_training_equipment_sandbag,
            SEATED_BACK_EXTENSION_MACHINE to Res.drawable.ic_launcher,
            SEATED_ROW_MACHINE to Res.drawable.ic_default_training_equipment_seated_row_machine,
            SHOULDER_PRESS_MACHINE to Res.drawable.ic_launcher,
            SLAM_BALL to Res.drawable.ic_launcher,
            SMITH_MACHINE to Res.drawable.ic_launcher,
            SPOTTER_ARMS to Res.drawable.ic_launcher,
            SQUAT_RACK to Res.drawable.ic_default_training_equipment_squat_rack,
            STAIR_CLIMBER to Res.drawable.ic_launcher,
            STATIONARY_BIKE to Res.drawable.ic_launcher,
            SUSPENSION_TRAINER to Res.drawable.ic_launcher,
            TREADMILL to Res.drawable.ic_default_training_equipment_treadmill,
            WALL_BALL to Res.drawable.ic_launcher,
            WEIGHT_PLATES to Res.drawable.ic_default_training_equipment_weight_plates,
            WRIST_WEIGHTS to Res.drawable.ic_launcher,
            YOGA_MAT to Res.drawable.ic_launcher
        )
}
