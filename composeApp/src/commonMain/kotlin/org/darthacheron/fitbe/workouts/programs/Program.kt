package org.darthacheron.fitbe.workouts.programs

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Program(
    val id: Uuid = Uuid.random(),
    val name: String,
    val imageUri: String? = null,
    val default: Boolean = false,
    // This will be populated by joining with WorkoutTemplateExercise and WorkoutTemplateSet
    val workouts: List<String> = emptyList()
)