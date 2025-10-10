package org.darthacheron.fitbe.workouts.templates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "default_workout_templates", indices = [Index(value = ["name"], unique = true)])
data class DefaultWorkoutTemplateEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid,
    @ColumnInfo(name = "name") val name: String,
    val description: String? = null,
    val imageUri: String? = null
) {
    fun toWorkoutTemplateEntity(): WorkoutTemplateEntity =
        WorkoutTemplateEntity(
            id = id,
            name = name,
            description = description,
            imageUri = imageUri,
            default = true
        )

    fun toWorkoutTemplate(): WorkoutTemplate =
        WorkoutTemplate(
            id = id,
            name = name,
            description = description,
            imageUri = imageUri,
            default = true
        )
}

@OptIn(ExperimentalUuidApi::class)
fun WorkoutTemplateEntity.toDefaultWorkoutTemplateEntity(entity: WorkoutTemplateEntity): DefaultWorkoutTemplateEntity {
    require(entity.default) { "Cannot convert a non-default WorkoutTemplateEntity to DefaultWorkoutTemplateEntity" }
    return DefaultWorkoutTemplateEntity(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        imageUri = entity.imageUri
    )
}