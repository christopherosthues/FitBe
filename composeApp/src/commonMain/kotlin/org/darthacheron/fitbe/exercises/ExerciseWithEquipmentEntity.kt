package org.darthacheron.fitbe.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlin.uuid.ExperimentalUuidApi

data class ExerciseWithEquipmentEntity(
    @Embedded val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "id", // From ExerciseEntity (the @Embedded entity)
        entityColumn = "id",   // From TrainingEquipmentEntity (the entity in the List)
        associateBy = Junction(
            value = ExerciseEquipmentCrossRef::class,
            parentColumn = "exerciseId", // Column in ExerciseEquipmentCrossRef matching ExerciseEntity's id
            entityColumn = "equipmentId" // Column in ExerciseEquipmentCrossRef matching TrainingEquipmentEntity's id
        )
    )
    val equipmentList: List<TrainingEquipmentEntity>
) {
    fun toExerciseWithEquipment(): ExerciseWithEquipment {
        return ExerciseWithEquipment(
            exercise = exercise.toExercise(),
            equipmentList = equipmentList.map { it.toTrainingEquipment() }
        )
    }
}

fun toEntity(exerciseWithEquipment: ExerciseWithEquipment): ExerciseWithEquipmentEntity {
    return ExerciseWithEquipmentEntity(
        exercise = toEntity(exerciseWithEquipment.exercise),
        equipmentList = exerciseWithEquipment.equipmentList.map { toEntity(it) }
    )
}
