package org.darthacheron.fitbe.workouts.workouts

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PerformedSet(
    val id: Uuid = Uuid.random(),
    val workoutExecutionSessionId: Uuid,
    val exerciseId: Uuid,
    val setOrder: Int,
    val timestamp: Instant, // When the set was completed/logged

    // Target metrics (could be from template or ad-hoc)
    val targetRepetitions: Int? = null,
    val targetWeightKg: Double? = null,
    val targetDistanceKm: Double? = null,
    val targetDurationSeconds: Long? = null,

    // Actual performed metrics
    val actualRepetitions: Int? = null,
    val actualWeightKg: Double? = null,
    val actualDistanceKm: Double? = null,
    val actualDurationSeconds: Long? = null,

    val plannedRestDurationSeconds: Int? = null, // Rest time planned after this set
    // val actualRestDurationSeconds: Int? = null, // Optional: To track actual rest taken

    val notes: String? = null
)
