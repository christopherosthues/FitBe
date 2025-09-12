package org.darthacheron.fitbe.workouts.workouts

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseSet(
    val id: Uuid,
    val workoutSessionId: Uuid,
    val exerciseId: Uuid, // To quickly know what exercise this set was for
    val setOrder: Int     // To maintain the order of sets
)
