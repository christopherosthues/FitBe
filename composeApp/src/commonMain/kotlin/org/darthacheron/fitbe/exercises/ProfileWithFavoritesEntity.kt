package org.darthacheron.fitbe.exercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.darthacheron.fitbe.profile.ProfileEntity
import org.darthacheron.fitbe.profile.toEntity

data class ProfileWithFavoritesEntity(
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
) {
    fun toProfileWithFavorites(): ProfileWithFavorites {
        return ProfileWithFavorites(
            profile = profile.toProfile(),
            favoriteExercises = favoriteExercises.map { it.toExercise() }
        )
    }
}

fun toEntity(profileWithFavorites: ProfileWithFavorites): ProfileWithFavoritesEntity {
    return ProfileWithFavoritesEntity(
        profile = toEntity(profileWithFavorites.profile),
        favoriteExercises = profileWithFavorites.favoriteExercises.map { toEntity(it) }
    )
}
