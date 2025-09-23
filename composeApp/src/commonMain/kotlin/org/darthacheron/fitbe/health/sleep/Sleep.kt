package org.darthacheron.fitbe.health.sleep

import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid,
    val profileId: Uuid,
    val hours: UInt,
    val minutes: UInt,
    val dateUtc: LocalDate,
)