package org.darthacheron.fitbe.exercises.exercises

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.darthacheron.fitbe.exercises.equipment.DefaultTrainingEquipmentEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "default_exercise_equipment_cross_ref",
    primaryKeys = ["exerciseId", "equipmentId"], // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = DefaultExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DefaultTrainingEquipmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["exerciseId"]),
        Index(value = ["equipmentId"])
    ]
)
data class DefaultExerciseEquipmentCrossRef(
    val exerciseId: Uuid,
    val equipmentId: Uuid
)