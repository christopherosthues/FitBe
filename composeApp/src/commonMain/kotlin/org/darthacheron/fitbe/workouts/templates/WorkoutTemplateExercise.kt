package org.darthacheron.fitbe.workouts.templates

import org.darthacheron.fitbe.workouts.exercises.Exercise
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutTemplateExercise(
    val id: Uuid = Uuid.random(),
    val workoutTemplateId: Uuid,
    val exercise: Exercise, // Optionally, you might want to hold the resolved Exercise object here
    val exerciseOrder: Int,
    val sets: List<WorkoutTemplateSet> = emptyList()
)