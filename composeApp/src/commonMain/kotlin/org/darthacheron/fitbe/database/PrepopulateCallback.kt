package org.darthacheron.fitbe.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.workouts.equipment.EquipmentDao
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.toDefaultTrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.exercises.DefaultExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.ExerciseDao
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.toDefaultExerciseEntity
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateDao
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
        equipmentList.forEach { equipment ->
            val equipment =
                TrainingEquipmentEntity(
                    id = equipment.id,
                    name = equipment.key,
                    imageUri = equipment.key,
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
            val exercise =
                ExerciseEntity(
                    id = exerciseData.id,
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
                        DefaultExerciseEquipmentCrossRef(exerciseId = exercise.id, equipmentId = mappedEquipmentId)
                    }
                }
            if (crossRefs.isNotEmpty()) {
                exerciseDao.insertDefaultExerciseEquipmentCrossRefs(crossRefs)
                exerciseDao.insertCrossRefs(
                    crossRefs.map {
                        ExerciseEquipmentCrossRef(exerciseId = exercise.id, equipmentId = it.equipmentId)
                    }
                )
            }
        }
    }
}
