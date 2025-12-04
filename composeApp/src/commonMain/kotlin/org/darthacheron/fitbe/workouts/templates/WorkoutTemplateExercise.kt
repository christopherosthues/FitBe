package org.darthacheron.fitbe.workouts.templates

import org.darthacheron.fitbe.workouts.exercises.Exercise
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class WorkoutTemplateExercise(
    val id: Uuid = Uuid.random(),
    val workoutTemplateId: Uuid,
    val exercise: Exercise, // Optionally, you might want to hold the resolved Exercise object here
    val exerciseOrder: Int,
    val lastModified: Instant,
    val sets: List<WorkoutTemplateSet> = emptyList()
)