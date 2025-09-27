package org.darthacheron.fitbe.workouts.templates

import org.darthacheron.fitbe.workouts.exercises.Exercise
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateExerciseWithDetails(
    val exercise: Exercise,
    val exerciseOrder: Int,
    val notes: String? = null,
    val sets: List<WorkoutTemplateSet> = emptyList()
)