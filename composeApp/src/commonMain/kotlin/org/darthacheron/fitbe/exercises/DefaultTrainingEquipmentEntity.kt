package org.darthacheron.fitbe.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "default_training_equipment")
data class DefaultTrainingEquipmentEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid, // Should match the id of the corresponding TrainingEquipmentEntity
    @ColumnInfo(name = "name") val name: String,
    val dateUtc: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
) {
    fun toTrainingEquipmentEntity(): TrainingEquipmentEntity {
        return TrainingEquipmentEntity(
            id = id,
            name = name,
            default = true, // Default equipment is always true here
            dateUtc = dateUtc
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun fromTrainingEquipmentEntity(entity: TrainingEquipmentEntity): DefaultTrainingEquipmentEntity {
    return DefaultTrainingEquipmentEntity(
        id = entity.id,
        name = entity.name,
        dateUtc = entity.dateUtc
    )
}