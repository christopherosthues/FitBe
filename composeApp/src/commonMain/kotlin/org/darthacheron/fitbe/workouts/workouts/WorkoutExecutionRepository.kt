package org.darthacheron.fitbe.workouts.workouts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WorkoutExecutionRepository(
    private val workoutExecutionSessionDao: WorkoutExecutionSessionDao
) {

    // WorkoutExecutionSession operations
    suspend fun upsertWorkoutExecutionSession(session: WorkoutExecutionSession) {
        workoutExecutionSessionDao.upsertWorkoutExecutionSession(session.toEntity())
        // Also upsert its performed sets if any
        if (session.performedSets.isNotEmpty()) {
            workoutExecutionSessionDao.upsertPerformedSets(session.performedSets.map { it.toEntity() })
        }
    }

    suspend fun deleteWorkoutExecutionSession(session: WorkoutExecutionSession) {
        // Deleting the session should cascade delete its performed sets due to ForeignKey constraints
        workoutExecutionSessionDao.deleteWorkoutExecutionSession(session.toEntity())
    }

    fun getWorkoutExecutionSessionWithSets(sessionId: Uuid): Flow<WorkoutExecutionSession?> {
        return workoutExecutionSessionDao.getWorkoutExecutionSessionWithSets(sessionId).map {
            it?.toWorkoutExecutionSession()
        }
    }

    fun getWorkoutExecutionSessionsByProfileId(profileId: Uuid): Flow<List<WorkoutExecutionSession>> {
        // This could be heavy if there are many sessions with many sets.
        // Consider if a lighter version (without sets) is needed for list views.
        // For now, fetching with sets for completeness when a session is selected.
        // This requires a new DAO method or modification of this logic to fetch sets separately if needed.
        // For simplicity, this example will just map entity results if not fetching with sets by default.
        return workoutExecutionSessionDao.getWorkoutExecutionSessionsByProfileId(profileId).map { entities ->
            entities.map { entity -> 
                // This map will not include performedSets unless fetched by getWorkoutExecutionSessionWithSets
                // You might need a more complex mapping here, fetching sets for each session, or a new DAO method.
                // For now, returning sessions without sets if using this specific DAO method.
                entity.toWorkoutExecutionSession() 
            }
        }
    }
    
    // Example: To get sessions with sets, you would iterate and call the detail fetch or have a DAO method for List<WorkoutExecutionSessionWithSets>
    // For a list view, it's often better to fetch session headers and then details on demand.

    fun getLastWorkoutExecutionSessionByProfileId(profileId: Uuid): Flow<WorkoutExecutionSession?> {
        return workoutExecutionSessionDao.getLastWorkoutExecutionSessionByProfileId(profileId).map { entity ->
            entity?.let { workoutExecutionSessionDao.getWorkoutExecutionSessionWithSets(it.id) }?.map { it?.toWorkoutExecutionSession() } ?: kotlinx.coroutines.flow.flowOf(null)
        }.kotlinx.coroutines.flow.flatMapConcat { it ?: kotlinx.coroutines.flow.flowOf(null) } // Flatten Flow<Flow<T?>>
    }

    fun getWorkoutExecutionSessionsByProfileIdAndDateRange(profileId: Uuid, startTime: Long, endTime: Long): Flow<List<WorkoutExecutionSession>> {
        return workoutExecutionSessionDao.getWorkoutExecutionSessionsByProfileIdAndDateRange(profileId, startTime, endTime).map { entities ->
            entities.map { it.toWorkoutExecutionSession() } // Similar to getWorkoutExecutionSessionsByProfileId, sets not included here by default
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

    // To get all performances of a specific exercise across all sessions:
    // Need a new DAO method: @Query("SELECT * FROM performed_sets WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    // fun getPerformedSetsByExerciseId(exerciseId: Uuid): Flow<List<PerformedSetEntity>>
    /*
    fun getPerformedSetsByExerciseId(exerciseId: Uuid): Flow<List<PerformedSet>> {
        return workoutExecutionSessionDao.getPerformedSetsByExerciseId(exerciseId).map { entities ->
            entities.map { it.toPerformedSet() }
        }
    }
    */
}
