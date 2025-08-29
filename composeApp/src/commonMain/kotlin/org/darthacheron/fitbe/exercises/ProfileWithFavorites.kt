package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi
import org.darthacheron.fitbe.profile.Profile

@OptIn(ExperimentalUuidApi::class)
data class ProfileWithFavorites(
    val profile: Profile,
    val favoriteExercises: List<Exercise>
)
