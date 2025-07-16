package org.darthacheron.fitbe.health.sleep

import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid,
    val hours: Int,
    val minutes: Int,
    val dateUtc: Instant,
    val dateLocal: Instant,
)