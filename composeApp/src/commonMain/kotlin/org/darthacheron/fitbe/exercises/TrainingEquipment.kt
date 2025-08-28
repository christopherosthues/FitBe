package org.darthacheron.fitbe.exercises

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TrainingEquipment(
    val id: Uuid = Uuid.random(),
    val name: String,
    val weight: Double? = null
)
