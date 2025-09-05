package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.exercises.ExerciseDao
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.exercises.equipment.fromTrainingEquipmentEntity
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

@OptIn(ExperimentalUuidApi::class)
class PrepopulateCallback(
    private val exerciseDaoProvider: () -> ExerciseDao
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        applicationScope.launch {
            val exerciseDao = exerciseDaoProvider()
            populateDatabase(exerciseDao)
        }
    }

    private suspend fun populateDatabase(exerciseDao: ExerciseDao) {
        equipmentList.forEach { equipmentKey ->
            val equipment = TrainingEquipmentEntity(
                name = equipmentKey, // This is now the full resource key
                imageUri = "ic_$equipmentKey",
                default = true,
            )
            exerciseDao.upsertEquipment(equipment)
            // The fromTrainingEquipmentEntity function will use equipment.name (the full resource key)
            // for DefaultTrainingEquipmentEntity.name
            exerciseDao.insertDefaultEquipment(
                fromTrainingEquipmentEntity(equipment)
            )
        }
    }
}
