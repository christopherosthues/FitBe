package org.darthacheron.fitbe.workouts.templates

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateSet(
    val id: Uuid = Uuid(),
    val workoutTemplateExerciseId: Uuid,
    val setOrder: Int,
    val targetRepetitions: Int? = null,
    val targetWeightKg: Double? = null,
    val targetDistanceKm: Double? = null,
    val targetDurationSeconds: Long? = null,
    val targetRestDurationSeconds: Int? = null, // Recommended rest time after this set
    val notes: String? = null // e.g., RPE
)
