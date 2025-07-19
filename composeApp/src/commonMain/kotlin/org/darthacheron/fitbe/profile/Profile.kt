package org.darthacheron.fitbe.profile

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Profile(
    val id: Uuid,
    val name: String, // Added name for profile identification
    val targetKcal: UInt,
    val targetBeverageInMilliliter: UInt,
    val targetWeight: Double,
    val targetSleepHours: UInt,
    val targetSleepMinutes: UInt,
    val targetSteps: UInt
)

// Implement the profiles feature. There already exists the model class with the required properties. Use for the sleep time the TimeInput.