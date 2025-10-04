package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class BodyWeight(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val weightInKg: Double,
    val bodyFatPercentage: Double?,
    val muscleMassInKg: Double?,
    val boneMassInKg: Double?,
    val bodyWaterInPercentage: Double?,
    val date: Instant
)