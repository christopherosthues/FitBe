package org.darthacheron.fitbe.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.profile.ProfileEntity

data class ProfileWithFavorites(
    @Embedded val profile: ProfileEntity, // Assuming ProfileEntity.kt
    @Relation(
        parentColumn = "id", // Primary key of ProfileEntity
        entityColumn = "id",   // Primary key of ExerciseEntity
        associateBy = Junction(
            value = ProfileFavoriteExerciseCrossRef::class,
            parentColumn = "profileId", // Column in CrossRef matching ProfileEntity's id
            entityColumn = "exerciseId"   // Column in CrossRef matching ExerciseEntity's id
        )
    )
    val favoriteExercises: List<ExerciseEntity>
)