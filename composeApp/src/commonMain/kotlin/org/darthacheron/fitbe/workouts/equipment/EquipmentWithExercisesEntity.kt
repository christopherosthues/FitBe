package org.darthacheron.fitbe.workouts.equipment

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.workouts.exercises.ExerciseEntity
import org.darthacheron.fitbe.workouts.exercises.ExerciseEquipmentCrossRef
import org.darthacheron.fitbe.workouts.exercises.toExerciseEntity
import kotlin.uuid.ExperimentalUuidApi

data class EquipmentWithExercisesEntity(
    @Embedded val equipment: TrainingEquipmentEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = ExerciseEquipmentCrossRef::class,
                parentColumn = "equipmentId",
                entityColumn = "exerciseId"
            )
    )
    val exercises: List<ExerciseEntity>
) {
    @OptIn(ExperimentalUuidApi::class)
    fun toEquipmentWithExercises(): EquipmentWithExercises =
        EquipmentWithExercises(
            id = equipment.id,
            name = equipment.name,
            default = equipment.default,
            dateUtc = equipment.dateUtc,
            imageUri = equipment.imageUri,
            exercises = exercises.map { it.toExercise() }
        )
}

@OptIn(ExperimentalUuidApi::class)
fun EquipmentWithExercises.toEquipmentWithExercisesEntity(): EquipmentWithExercisesEntity =
    EquipmentWithExercisesEntity(
        equipment =
            TrainingEquipmentEntity(
                id = this.id,
                name = this.name,
                imageUri = this.imageUri,
                default = this.default,
                dateUtc = this.dateUtc
            ),
        exercises = this.exercises.map { it.toExerciseEntity() }
    )