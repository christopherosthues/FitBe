package org.darthacheron.fitbe.health.weight

import org.darthacheron.fitbe.settings.WeightUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class BodyWeight(
    val id: Uuid,
    val weightInKg: Double,
    val bodyFatPercentage: Double,
    val muscleMassInKg: Double,
    val boneMassInKg: Double,
    val bodyWaterInPercentage: Double,
    val dateUtc: String,
    val dateLocal: String
)