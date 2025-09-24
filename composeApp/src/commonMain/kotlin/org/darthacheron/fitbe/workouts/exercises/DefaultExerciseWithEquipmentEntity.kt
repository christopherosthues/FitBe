package org.darthacheron.fitbe.workouts.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.workouts.equipment.DefaultTrainingEquipmentEntity
import kotlin.uuid.ExperimentalUuidApi

class DefaultExerciseWithEquipmentEntity(
    @Embedded val exercise: DefaultExerciseEntity,
    @Relation(
        parentColumn = "id", // From ExerciseEntity (the @Embedded entity)
        entityColumn = "id",   // From TrainingEquipmentEntity (the entity in the List)
        associateBy = Junction(
            value = DefaultExerciseEquipmentCrossRef::class,
            parentColumn = "exerciseId", // Column in ExerciseEquipmentCrossRef matching ExerciseEntity's id
            entityColumn = "equipmentId" // Column in ExerciseEquipmentCrossRef matching TrainingEquipmentEntity's id
        )
    )
    val equipmentList: List<DefaultTrainingEquipmentEntity>
) {
    @OptIn(ExperimentalUuidApi::class)
    fun toExerciseWithEquipment(): ExerciseWithEquipment {
        return ExerciseWithEquipment(
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
}
