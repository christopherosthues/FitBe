package org.darthacheron.fitbe.exercises

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class EquipmentWithExercises(
    val id: Uuid,
    val name: String,
    val default: Boolean = false,
    val dateUtc: LocalDate,
    val exercises: List<Exercise>
)
