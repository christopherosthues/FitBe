package org.darthacheron.fitbe.workouts.exercises

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "profile_favorite_exercise_cross_ref",
    primaryKeys = ["profileId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseId"])]
)
data class ProfileFavoriteExerciseCrossRef(
    val profileId: Uuid,
    val exerciseId: Uuid
)