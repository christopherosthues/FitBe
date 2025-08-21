package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Steps(
    val id: Uuid,
    val profileId: Uuid,
    val steps: Int,
    val dateUtc: LocalDate
)
