package org.darthacheron.fitbe.exercises

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
            entity = ProfileEntity::class, // Assuming ProfileEntity.kt exists
            parentColumns = ["id"], // Assuming 'id' is the primary key in ProfileEntity
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"], // Assuming 'id' is the primary key in ExerciseEntity
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseId"])] // Index for faster queries by exercise
)
data class ProfileFavoriteExerciseCrossRef(
    val profileId: Uuid, // Or whatever type your ProfileEntity ID is
    val exerciseId: Uuid // Or whatever type your ExerciseEntity ID is
)