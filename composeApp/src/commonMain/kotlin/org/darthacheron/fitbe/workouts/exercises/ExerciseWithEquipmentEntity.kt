package org.darthacheron.fitbe.workouts.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.workouts.equipment.toTrainingEquipmentEntity // Assuming this is for TrainingEquipment
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

data class ExerciseWithEquipmentEntity(
    @Embedded val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = ExerciseEquipmentCrossRef::class,
                parentColumn = "exerciseId",
                entityColumn = "equipmentId"
            )
    )
    val equipmentList: List<TrainingEquipmentEntity>
) {
    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    fun toExerciseWithEquipment(): ExerciseWithEquipment =
        ExerciseWithEquipment(
            id = exercise.id,
            name = exercise.name,
            guide = exercise.guide,
            targetMuscleGroups = exercise.targetMuscleGroups,
            imageUri = exercise.imageUri,
            default = exercise.default,
            recommendedFor = exercise.recommendedFor,
            exerciseType = exercise.exerciseType,
            dateUtc = exercise.dateUtc,
            lastModified = exercise.lastModified,
            equipmentList = equipmentList.map { it.toTrainingEquipment() }
        )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
fun toExerciseWithEquipmentEntity(exerciseWithEquipment: ExerciseWithEquipment): ExerciseWithEquipmentEntity =
    ExerciseWithEquipmentEntity(
        exercise =
            ExerciseEntity(
                id = exerciseWithEquipment.id,
                name = exerciseWithEquipment.name,
                guide = exerciseWithEquipment.guide,
                targetMuscleGroups = exerciseWithEquipment.targetMuscleGroups,
                imageUri = exerciseWithEquipment.imageUri,
                default = exerciseWithEquipment.default,
                recommendedFor = exerciseWithEquipment.recommendedFor,
                exerciseType = exerciseWithEquipment.exerciseType,
                dateUtc = exerciseWithEquipment.dateUtc,
                lastModified = exerciseWithEquipment.lastModified
            ),
        equipmentList = exerciseWithEquipment.equipmentList.map { it.toTrainingEquipmentEntity() }
    )