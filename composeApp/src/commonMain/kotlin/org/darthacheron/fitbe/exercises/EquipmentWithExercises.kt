package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class EquipmentWithExercises(
    val equipment: TrainingEquipment,
    val exercises: List<Exercise>
)
