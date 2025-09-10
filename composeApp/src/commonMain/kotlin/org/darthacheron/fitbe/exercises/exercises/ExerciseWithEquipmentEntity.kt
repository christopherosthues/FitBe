package org.darthacheron.fitbe.exercises.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipmentEntity
import org.darthacheron.fitbe.exercises.equipment.toEntity
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
    @OptIn(ExperimentalUuidApi::class)
    fun toExerciseWithEquipment(): ExerciseWithEquipment {
        return ExerciseWithEquipment(
            id = exercise.id,
            name = exercise.name,
            guide = exercise.guide,
            targetMuscleGroups = exercise.targetMuscleGroups,
            imageUri = exercise.imageUri,
            default = exercise.default,
            dateUtc = exercise.dateUtc,
            equipmentList = equipmentList.map { it.toTrainingEquipment() }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(exerciseWithEquipment: ExerciseWithEquipment): ExerciseWithEquipmentEntity {
    return ExerciseWithEquipmentEntity(
        exercise = ExerciseEntity(
            id = exerciseWithEquipment.id,
            name = exerciseWithEquipment.name,
            guide = exerciseWithEquipment.guide,
            targetMuscleGroups = exerciseWithEquipment.targetMuscleGroups,
            imageUri = exerciseWithEquipment.imageUri,
            default = exerciseWithEquipment.default,
            dateUtc = exerciseWithEquipment.dateUtc
        ),
        equipmentList = exerciseWithEquipment.equipmentList.map { toEntity(it) }
    )
}
