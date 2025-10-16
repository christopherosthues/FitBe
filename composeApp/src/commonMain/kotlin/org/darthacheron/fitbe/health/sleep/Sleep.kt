package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val start: Instant,
    val end: Instant
) {
    val duration = (end - start).inWholeMinutes
}