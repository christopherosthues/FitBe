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
    primaryKeys = ["exercise_id", "equipment_id"], // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrainingEquipmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["exercise_id"]),
        Index(value = ["equipment_id"])
    ]
)
data class ExerciseEquipmentCrossRef(
    @ColumnInfo(name = "exercise_id") val exerciseId: Uuid,
    @ColumnInfo(name = "equipment_id") val equipmentId: Uuid
)
