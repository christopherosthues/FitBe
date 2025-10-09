package org.darthacheron.fitbe.health.sleep

import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.Instant

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val start: Instant,
    val end: Instant
)