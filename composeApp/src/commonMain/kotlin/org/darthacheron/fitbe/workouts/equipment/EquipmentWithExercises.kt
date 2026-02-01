package org.darthacheron.fitbe.workouts.equipment

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.workouts.exercises.Exercise
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class EquipmentWithExercises(
    override val id: Uuid,
    override val name: String,
    override val default: Boolean = false,
    override val imageUri: String? = null,
    override val dateUtc: LocalDate,
    override val lastModified: Instant?,
    val exercises: List<Exercise>
) : TrainingEquipment(id, name, imageUri, default, dateUtc, lastModified)