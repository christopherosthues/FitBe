package org.darthacheron.fitbe.exercises.equipment

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.exercises.exercises.Exercise
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class EquipmentWithExercises(
    override val id: Uuid,
    override val name: String,
    override val default: Boolean = false,
    override val imageUri: String? = null,
    override val dateUtc: LocalDate,
    val exercises: List<Exercise>
) : TrainingEquipment(id, name, imageUri, default, dateUtc)
