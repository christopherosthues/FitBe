package org.darthacheron.fitbe.exercises.exercises

import kotlin.uuid.ExperimentalUuidApi
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.exercises.equipment.TrainingEquipment
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseWithEquipment(
    override val id: Uuid,
    override val name: String,
    override val guide: String,
    override val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    override val imageUri: String?,
    override val default: Boolean = false,
    override val dateUtc: LocalDate,
    val equipmentList: List<TrainingEquipment>
) : Exercise(id, name, guide, targetMuscleGroups, imageUri, default, dateUtc)
