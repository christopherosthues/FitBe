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
    val description: String? = null
) {
    fun toWorkoutTemplate(): WorkoutTemplate {
        return WorkoutTemplate(
            id = this.id,
            name = this.name,
            description = this.description
            // exercises will be loaded separately
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
fun WorkoutTemplate.toEntity(): WorkoutTemplateEntity {
    return WorkoutTemplateEntity(
        id = this.id,
        name = this.name,
        description = this.description
    )
}
