package org.darthacheron.fitbe.workouts.workouts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant // Added import
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WorkoutExecutionRepository(
    private val workoutExecutionSessionDao: WorkoutExecutionSessionDao
) {

    /**
     * Upserts a WorkoutExecutionSession. 
     * If it's a new session being scheduled, ensure `scheduledTimestamp` is set and `startTimestamp` is null.
     * If it's an ongoing or completed session, `startTimestamp` should be set.
     * Also upserts its performed sets if any.
     */
    suspend fun upsertWorkoutExecutionSession(session: WorkoutExecutionSession) {
        workoutExecutionSessionDao.upsertWorkoutExecutionSession(session.toEntity())
        if (session.performedSets.isNotEmpty()) {
            workoutExecutionSessionDao.upsertPerformedSets(session.performedSets.map { it.toEntity() })
        }
    }

    /**
     * Schedules a workout execution session. 
     * The session's `scheduledTimestamp` must be set, and `startTimestamp` should be null.
     * Its performed sets (which define the planned exercises and sets for the scheduled workout) will also be saved.
     */
    suspend fun scheduleWorkoutExecutionSession(session: WorkoutExecutionSession) {
        require(session.scheduledTimestamp != null) { "Scheduled timestamp must be set for scheduling a session." }
        require(session.startTimestamp == null) { "Start timestamp must be null for a new scheduled session." }
        upsertWorkoutExecutionSession(session) // Uses the general upsert
    }

    /**
     * Starts a previously scheduled workout.
     * Sets the `startTimestamp` of the session and re-upserts it.
     */
    suspend fun startScheduledWorkoutExecution(sessionId: Uuid, startTime: Instant) {
        val sessionWithSets = workoutExecutionSessionDao.getWorkoutExecutionSessionWithSets(sessionId).firstOrNull()
        require(sessionWithSets != null) { "Scheduled session with ID $sessionId not found." }
        require(sessionWithSets.session.scheduledTimestamp != null) { "Session with ID $sessionId was not scheduled." }
        require(sessionWithSets.session.startTimestamp == null) { "Session with ID $sessionId has already been started." }

        val updatedSession = sessionWithSets.toWorkoutExecutionSession().copy(startTimestamp = startTime)
        upsertWorkoutExecutionSession(updatedSession)
    }

    suspend fun deleteWorkoutExecutionSession(session: WorkoutExecutionSession) {
        workoutExecutionSessionDao.deleteWorkoutExecutionSession(session.toEntity())
    }
    
    suspend fun deleteWorkoutExecutionSessionById(sessionId: Uuid) {
        getWorkoutExecutionSessionWithSets(sessionId).firstOrNull()?.let {
            workoutExecutionSessionDao.deleteWorkoutExecutionSession(it.toEntity())
        }
    }

    fun getWorkoutExecutionSessionWithSets(sessionId: Uuid): Flow<WorkoutExecutionSession?> {
        return workoutExecutionSessionDao.getWorkoutExecutionSessionWithSets(sessionId).map {
            it?.toWorkoutExecutionSession()
        }
    }

    // --- Methods for COMPLETED or IN-PROGRESS sessions ---
    fun getCompletedOrInProgressSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getCompletedOrInProgressSessionsByProfileId(profileId).map { entities ->
            entities.map { it.toWorkoutExecutionSession() } // Sets not included by default for list performance
        }
    }

    fun getLastCompletedOrInProgressSessionWithSets(profileId: Uuid): Flow<WorkoutExecutionSession?> {
        return workoutExecutionSessionDao.getLastCompletedOrInProgressSessionByProfileId(profileId).map { entity ->
            entity?.let { workoutExecutionSessionDao.getWorkoutExecutionSessionWithSets(it.id) }?.map { it?.toWorkoutExecutionSession() } ?: flowOf(null)
        }.flatMapConcat { it ?: kotlinx.coroutines.flow.flowOf(null) }
    }

    fun getCompletedOrInProgressSessionsByProfileIdAndDateRange(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getCompletedOrInProgressSessionsByProfileIdAndDateRange(profileId, fromInstant, toInstant).map { entities ->
            entities.map { it.toWorkoutExecutionSession() } // Sets not included
        }
    }

    // --- Methods for SCHEDULED sessions ---
    fun getScheduledWorkoutExecutionSessions(profileId: Uuid): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getScheduledWorkoutExecutionSessions(profileId).map { entities ->
            entities.map { it.toWorkoutExecutionSession() } // Sets not included by default for list performance
        }
    }

    fun getScheduledWorkoutExecutionSessionsWithSets(profileId: Uuid): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getScheduledWorkoutExecutionSessionsWithSets(profileId).map { entitiesWithSets ->
            entitiesWithSets.map { it.toWorkoutExecutionSession() }
        }
    }

    fun getScheduledWorkoutExecutionSessionsByDateRange(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getScheduledWorkoutExecutionSessionsByDateRange(profileId, fromInstant, toInstant).map { entities ->
            entities.map { it.toWorkoutExecutionSession() } // Sets not included
        }
    }
    
    fun getScheduledWorkoutExecutionSessionsByDateRangeWithSets(profileId: Uuid, fromInstant: Instant, toInstant: Instant): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getScheduledWorkoutExecutionSessionsByDateRangeWithSets(profileId, fromInstant, toInstant).map { entitiesWithSets ->
            entitiesWithSets.map { it.toWorkoutExecutionSession() }
        }
    }

    // PerformedSet operations
    suspend fun upsertPerformedSet(performedSet: PerformedSet) {
        workoutExecutionSessionDao.upsertPerformedSet(performedSet.toEntity())
    }

    suspend fun upsertPerformedSets(performedSets: List<PerformedSet>) {
        workoutExecutionSessionDao.upsertPerformedSets(performedSets.map { it.toEntity() })
    }

    suspend fun deletePerformedSet(performedSet: PerformedSet) {
        workoutExecutionSessionDao.deletePerformedSet(performedSet.toEntity())
    }

    fun getPerformedSetsForWorkoutExecutionSession(workoutExecutionSessionId: Uuid): Flow<List<PerformedSet>> {
        return workoutExecutionSessionDao.getPerformedSetsForWorkoutExecutionSession(workoutExecutionSessionId).map { entities ->
            entities.map { it.toPerformedSet() }
        }
    }
}
