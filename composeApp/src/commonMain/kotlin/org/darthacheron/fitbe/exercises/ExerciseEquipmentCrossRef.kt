package org.darthacheron.fitbe.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "exercise_equipment_cross_ref",
    primaryKeys = ["exerciseId", "equipmentId"], // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrainingEquipmentEntity::class,
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
data class ExerciseEquipmentCrossRef(
    val exerciseId: Uuid,
    val equipmentId: Uuid
)
