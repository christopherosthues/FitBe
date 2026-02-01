package org.darthacheron.fitbe.health.steps

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Steps(
    val id: Uuid = Uuid.random(),
    @Transient val profileId: Uuid = Uuid.random(),
    val steps: UInt,
    val date: Instant,
    val lastModified: Instant? = null
)