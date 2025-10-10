package org.darthacheron.fitbe.workouts.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.workouts.equipment.DefaultTrainingEquipmentEntity
import kotlin.uuid.ExperimentalUuidApi

class DefaultExerciseWithEquipmentEntity(
    @Embedded val exercise: DefaultExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = DefaultExerciseEquipmentCrossRef::class,
                parentColumn = "exerciseId",
                entityColumn = "equipmentId"
            )
    )
    val equipmentList: List<DefaultTrainingEquipmentEntity>
) {
    @OptIn(ExperimentalUuidApi::class)
    fun toExerciseWithEquipment(): ExerciseWithEquipment =
        ExerciseWithEquipment(
            id = exercise.id,
            name = exercise.name,
            guide = exercise.guide,
            targetMuscleGroups = exercise.targetMuscleGroups,
            imageUri = exercise.imageUri,
            default = true,
            recommendedFor = exercise.recommendedFor,
            exerciseType = exercise.exerciseType,
            dateUtc = exercise.dateUtc,
            equipmentList = equipmentList.map { it.toTrainingEquipment() }
        )
}