package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class ExerciseWithEquipment(
    val exercise: Exercise,
    val equipmentList: List<TrainingEquipment>
)
