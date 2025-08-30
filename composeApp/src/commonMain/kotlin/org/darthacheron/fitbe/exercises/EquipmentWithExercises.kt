package org.darthacheron.fitbe.exercises

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class EquipmentWithExercises(
    override val id: Uuid,
    override val name: String,
    override val default: Boolean = false,
    override val dateUtc: LocalDate,
    val exercises: List<Exercise>
) : TrainingEquipment(id, name, default, dateUtc)
