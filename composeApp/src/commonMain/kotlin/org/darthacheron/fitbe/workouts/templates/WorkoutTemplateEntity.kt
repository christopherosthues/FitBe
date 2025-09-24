package org.darthacheron.fitbe.workouts.templates

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "workout_templates")
data class WorkoutTemplateEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val description: String? = null,
    val imageUri: String? = null,
    val default: Boolean = false
) {
    fun toWorkoutTemplate(): WorkoutTemplate {
        return WorkoutTemplate(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUri = this.imageUri,
            default = this.default
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun WorkoutTemplate.toWorkoutTemplateEntity(): WorkoutTemplateEntity {
    return WorkoutTemplateEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUri = this.imageUri,
        default = this.default
    )
}
