package org.darthacheron.fitbe.exercises

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "training_equipment", indices = [Index(value = ["name"], unique = true)])
data class TrainingEquipmentEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "name") val name: String,
    val default: Boolean = false,
    val dateUtc: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
) {
    fun toTrainingEquipment(): TrainingEquipment {
        return TrainingEquipment(
            id = id,
            name = name,
            default = default,
            dateUtc = dateUtc
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun toEntity(equipment: TrainingEquipment): TrainingEquipmentEntity {
    return TrainingEquipmentEntity(
        id = equipment.id,
        name = equipment.name,
        default = equipment.default,
        dateUtc = equipment.dateUtc
    )
}
