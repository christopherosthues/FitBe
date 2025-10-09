package org.darthacheron.fitbe.workouts.equipment

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
@Entity(tableName = "default_training_equipment", indices = [Index(value = ["name"], unique = true)])
data class DefaultTrainingEquipmentEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null,
    val dateUtc: LocalDate = Clock.System
        .now()
        .toLocalDateTime(TimeZone.UTC)
        .date,
) {
    fun toTrainingEquipmentEntity(): TrainingEquipmentEntity =
        TrainingEquipmentEntity(
        id = id,
        name = name,
        imageUri = imageUri,
        default = true,
        dateUtc = dateUtc
    )

    fun toTrainingEquipment(): TrainingEquipment =
        TrainingEquipment(
        id = id,
        name = name,
        imageUri = imageUri,
        default = true,
        dateUtc = dateUtc
    )
}

@OptIn(ExperimentalUuidApi::class)
fun TrainingEquipmentEntity.toDefaultTrainingEquipmentEntity(): DefaultTrainingEquipmentEntity =
    DefaultTrainingEquipmentEntity(
        id = this.id,
        name = this.name,
        imageUri = this.imageUri,
        dateUtc = this.dateUtc
    )