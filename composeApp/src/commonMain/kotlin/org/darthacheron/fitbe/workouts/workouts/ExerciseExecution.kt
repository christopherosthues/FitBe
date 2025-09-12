package org.darthacheron.fitbe.workouts.workouts

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseExecution(
    val id: Uuid,
    val profileId: Uuid,
    val exerciseId: Uuid,
    val timestamp: Instant,
    val repetitions: Int? = null,
    val weightKg: Double? = null,      // Weight in kilograms
    val distanceKm: Double? = null,    // Distance in kilometers
    val durationSeconds: Long? = null,
    val notes: String? = null,
    val setId: Uuid? = null            // Optional: if part of a set
)
