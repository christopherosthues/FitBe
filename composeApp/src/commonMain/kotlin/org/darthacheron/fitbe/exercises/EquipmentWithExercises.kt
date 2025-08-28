package org.darthacheron.fitbe.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EquipmentWithExercises( // Reusing the name from one-to-many, but the @Relation is different
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
)
