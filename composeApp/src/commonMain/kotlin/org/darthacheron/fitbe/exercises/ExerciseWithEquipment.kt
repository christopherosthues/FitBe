package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseWithEquipment(
    val id: Uuid,
    val name: String,
    val guide: String,
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val default: Boolean = false,
    val dateUtc: LocalDate,
    val equipmentList: List<TrainingEquipment>
)
