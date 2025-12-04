package org.darthacheron.fitbe.health.weight

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class BodyWeight(
    val id: Uuid = Uuid.random(),
    @Transient val profileId: Uuid = Uuid.random(),
    val weightInKg: Double,
    val bodyFatPercentage: Double?,
    val muscleMassInKg: Double?,
    val boneMassInKg: Double?,
    val bodyWaterInPercentage: Double?,
    val date: Instant,
    val lastModified: Instant
)