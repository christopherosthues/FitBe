package org.darthacheron.fitbe.workouts.templates

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplate(
    val id: Uuid = Uuid.random(), // Default to random for new instances
    val name: String,
    val description: String? = null,
    // This will be populated by joining with WorkoutTemplateExercise and WorkoutTemplateSet
    val exercises: List<WorkoutTemplateExercise> = emptyList()
)
