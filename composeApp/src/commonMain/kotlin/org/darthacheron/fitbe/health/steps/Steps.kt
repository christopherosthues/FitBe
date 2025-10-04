package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Steps(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val steps: UInt,
    val date: Instant
)
