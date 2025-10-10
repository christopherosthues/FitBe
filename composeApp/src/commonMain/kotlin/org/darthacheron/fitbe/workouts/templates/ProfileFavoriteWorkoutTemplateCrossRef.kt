package org.darthacheron.fitbe.workouts.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "profile_favorite_workout_template_cross_ref",
    primaryKeys = ["profileId", "workoutTemplateId"],
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutTemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutTemplateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["profileId"]),
        Index(value = ["workoutTemplateId"])
    ]
)
data class ProfileFavoriteWorkoutTemplateCrossRef(
    val profileId: Uuid,
    val workoutTemplateId: Uuid
)