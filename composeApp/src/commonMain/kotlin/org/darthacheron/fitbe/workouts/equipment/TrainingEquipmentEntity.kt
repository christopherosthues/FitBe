package org.darthacheron.fitbe.workouts.equipment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Entity(tableName = "training_equipment", indices = [Index(value = ["name"], unique = true)])
data class TrainingEquipmentEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null,
    val default: Boolean = false,
    val dateUtc: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.UTC)
            .date,
    val lastModified: Instant
) {
    fun toTrainingEquipment(): TrainingEquipment =
        TrainingEquipment(
            id = id,
            name = name,
            imageUri = imageUri,
            default = default,
            dateUtc = dateUtc,
            lastModified = lastModified
        )
}

@OptIn(ExperimentalUuidApi::class)
fun TrainingEquipment.toTrainingEquipmentEntity(): TrainingEquipmentEntity =
    TrainingEquipmentEntity(
        id = this.id,
        name = this.name,
        imageUri = this.imageUri,
        default = this.default,
        dateUtc = this.dateUtc,
        lastModified = this.lastModified
    )