package org.darthacheron.fitbe.health.sleep

import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Sleep(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dateUtc: LocalDate,
)