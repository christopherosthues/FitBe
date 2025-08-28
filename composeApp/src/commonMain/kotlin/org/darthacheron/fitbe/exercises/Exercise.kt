package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Exercise(
    val id: Uuid,
    val name: String,
    val guide: String,
    val equipments: List<TrainingEquipment>,
    val default: Boolean = false
)
