package org.darthacheron.fitbe.exercises.exercises

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class Exercise(
    open val id: Uuid,
    open val name: String,
    open val guide: String,
    open val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    open val default: Boolean = false,
    open val dateUtc: LocalDate
)
