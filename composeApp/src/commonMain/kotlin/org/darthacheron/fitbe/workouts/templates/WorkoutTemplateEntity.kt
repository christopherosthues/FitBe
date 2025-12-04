package org.darthacheron.fitbe.workouts.templates

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Entity(tableName = "workout_templates")
data class WorkoutTemplateEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val description: String? = null,
    val imageUri: String? = null,
    val default: Boolean = false,
    val lastModified: Instant
) {
    fun toWorkoutTemplate(): WorkoutTemplate =
        WorkoutTemplate(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUri = this.imageUri,
            default = this.default,
            lastModified = this.lastModified
        )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
fun WorkoutTemplate.toWorkoutTemplateEntity(): WorkoutTemplateEntity =
    WorkoutTemplateEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUri = this.imageUri,
        default = this.default,
        lastModified = this.lastModified
    )