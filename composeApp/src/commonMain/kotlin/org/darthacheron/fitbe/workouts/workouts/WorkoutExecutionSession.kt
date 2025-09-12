package org.darthacheron.fitbe.workouts.workouts

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.darthacheron.fitbe.workouts.workouts.PerformedSet // Correct import

@OptIn(ExperimentalUuidApi::class)
data class WorkoutExecutionSession(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val workoutTemplateId: Uuid? = null, // Optional link to a template
    val name: String, // Can be from template or custom
    val scheduledTimestamp: Instant? = null, // When the workout is scheduled
    val startTimestamp: Instant? = null, // When the workout actually started, null if only scheduled
    val endTimestamp: Instant? = null,
    val notes: String? = null, // Overall notes for the execution session
    val performedSets: List<PerformedSet> = emptyList()
)
