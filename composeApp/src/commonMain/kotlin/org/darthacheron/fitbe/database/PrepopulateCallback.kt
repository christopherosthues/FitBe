package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.exercises.equipment.EquipmentDao
import org.darthacheron.fitbe.exercises.exercises.ExerciseDao
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.exercises.equipment.fromTrainingEquipmentEntity
import org.darthacheron.fitbe.exercises.exercises.ExerciseEntity
import org.darthacheron.fitbe.exercises.exercises.MuscleGroup
import org.darthacheron.fitbe.exercises.exercises.fromExerciseEntity
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

internal val exerciseList = listOf(
    Pair("default_exercise_squat", listOf(MuscleGroup.QUADS)),
    Pair("default_exercise_deadlift", listOf(MuscleGroup.GLUTES)),
    Pair("default_exercise_bench_press", listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS)),
    Pair("default_exercise_overhead_press", listOf(MuscleGroup.SHOULDERS)),
    Pair("default_exercise_pull_ups", listOf(MuscleGroup.BACK)),
    Pair("default_exercise_push_ups", listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS, MuscleGroup.BACK, MuscleGroup.CHEST)),
    Pair("default_exercise_lunges", listOf(MuscleGroup.CALVES, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS)),
    Pair("default_exercise_sit_ups", listOf(MuscleGroup.ABS)),
    Pair("default_exercise_plank", listOf(MuscleGroup.ABS, MuscleGroup.CHEST, MuscleGroup.BACK)),
    Pair("default_exercise_jumping_jacks", listOf(MuscleGroup.ABS, MuscleGroup.CHEST, MuscleGroup.BACK)),
    Pair("default_exercise_side_lunges", listOf(MuscleGroup.CALVES, MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS)),
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
                imageUri = "ic_$equipmentKey",
                default = true,
            )
            equipmentDao.upsertEquipment(equipment)
            equipmentDao.insertDefaultEquipment(
                fromTrainingEquipmentEntity(equipment)
            )
        }
    }

    private suspend fun populateDefaultExercises(exerciseDao: ExerciseDao) {
        exerciseList.forEach { exerciseKey ->
            val exercise = ExerciseEntity(
                name = exerciseKey.first,
                guide = "${exerciseKey}_guide",
                targetMuscleGroups = exerciseKey.second,
                default = true,
            )
            exerciseDao.upsertExercise(exercise)
            exerciseDao.insertDefaultExercise(
                fromExerciseEntity(exercise)
            )
        }
    }
}
