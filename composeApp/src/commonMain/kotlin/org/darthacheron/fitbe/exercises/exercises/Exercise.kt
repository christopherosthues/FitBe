package org.darthacheron.fitbe.exercises.exercises

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Exercise(
    val id: Uuid,
    val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val default: Boolean = false,
    val dateUtc: LocalDate
)
