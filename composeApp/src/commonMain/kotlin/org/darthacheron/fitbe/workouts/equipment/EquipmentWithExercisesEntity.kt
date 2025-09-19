package org.darthacheron.fitbe.workouts.equipment

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.toEntity
import kotlin.uuid.ExperimentalUuidApi

data class EquipmentWithExercisesEntity( // Reusing the name from one-to-many, but the @Relation is different
    @Embedded val equipment: TrainingEquipmentEntity,
    @Relation(
        parentColumn = "id", // From TrainingEquipmentEntity (the @Embedded entity)
        entityColumn = "id",   // From ExerciseEntity (the entity in the List)
        associateBy = Junction(
            value = ExerciseEquipmentCrossRef::class,
            parentColumn = "equipmentId", // Column in ExerciseEquipmentCrossRef matching TrainingEquipmentEntity's id
            entityColumn = "exerciseId"   // Column in ExerciseEquipmentCrossRef matching ExerciseEntity's id
        )
    )
    val exercises: List<ExerciseEntity>
) {
    @OptIn(ExperimentalUuidApi::class)
    fun toEquipmentWithExercises(): EquipmentWithExercises {
        return EquipmentWithExercises(
            id = equipment.id,
            name = equipment.name,
            default = equipment.default,
            dateUtc = equipment.dateUtc,
            imageUri = equipment.imageUri,
            exercises = exercises.map { it.toExercise() }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(equipmentWithExercises: EquipmentWithExercises): EquipmentWithExercisesEntity {
    return EquipmentWithExercisesEntity(
        equipment = TrainingEquipmentEntity(
            id = equipmentWithExercises.id,
            name = equipmentWithExercises.name,
            imageUri = equipmentWithExercises.imageUri,
            default = equipmentWithExercises.default,
            dateUtc = equipmentWithExercises.dateUtc
        ),
        exercises = equipmentWithExercises.exercises.map { it.toEntity() }
    )
}

