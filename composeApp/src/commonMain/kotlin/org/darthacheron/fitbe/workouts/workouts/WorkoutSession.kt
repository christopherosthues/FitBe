package org.darthacheron.fitbe.workouts.workouts

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class WorkoutSession(
    val id: Uuid,
    val profileId: Uuid,
    val startTimestamp: Instant,
    val endTimestamp: Instant? = null,
    val name: String? = null
)
