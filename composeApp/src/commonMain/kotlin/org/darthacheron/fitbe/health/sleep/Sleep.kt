package org.darthacheron.fitbe.health.sleep

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Instant
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid = Uuid.random(),
    @Transient val profileId: Uuid = Uuid.random(),
    val start: Instant,
    val end: Instant,
    val lastModified: Instant
) {
    val duration = end - start

    val totalMinutes = duration.inWholeMinutes.toInt()

    val minutes = duration.inWholeMinutes.toInt() % 60

    val hours = duration.inWholeMinutes.toInt() / 60
}