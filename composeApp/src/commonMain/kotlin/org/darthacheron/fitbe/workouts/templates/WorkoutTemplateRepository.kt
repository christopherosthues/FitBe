package org.darthacheron.fitbe.workouts.templates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WorkoutTemplateRepository(
    private val workoutTemplateDao: WorkoutTemplateDao
) {

    // WorkoutTemplate operations
    suspend fun upsertWorkoutTemplate(template: WorkoutTemplate) {
        workoutTemplateDao.upsertWorkoutTemplate(template.toEntity())
        // Upsert exercises and their sets if they are part of the domain model
        template.exercises.forEach { exercise ->
            workoutTemplateDao.upsertWorkoutTemplateExercise(exercise.toEntity())
            if (exercise.sets.isNotEmpty()) {
                workoutTemplateDao.upsertWorkoutTemplateSets(exercise.sets.map { it.toEntity() })
            }
        }
    }

    suspend fun deleteWorkoutTemplate(template: WorkoutTemplate) {
        // Deleting the template should cascade delete its exercises and sets due to ForeignKey constraints
        workoutTemplateDao.deleteWorkoutTemplate(template.toEntity())
    }

    fun getWorkoutTemplateWithExercisesAndSets(templateId: Uuid): Flow<WorkoutTemplate?> {
        return workoutTemplateDao.getWorkoutTemplateWithExercisesAndSets(templateId).map {
            it?.toWorkoutTemplate()
        }
    }

    fun getAllWorkoutTemplatesWithExercisesAndSets(): Flow<List<WorkoutTemplate>> {
        return workoutTemplateDao.getAllWorkoutTemplatesWithExercisesAndSets().map { entities ->
            entities.map { it.toWorkoutTemplate() }
        }
    }

    // Simpler fetch for just template headers (e.g., for a list view)
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplate>> {
        return workoutTemplateDao.getAllWorkoutTemplates().map { entities ->
            entities.map { it.toWorkoutTemplate() } // These will not have exercises/sets populated by default
        }
    }

    // WorkoutTemplateExercise operations (usually managed via the parent WorkoutTemplate)
    suspend fun upsertWorkoutTemplateExercise(exercise: WorkoutTemplateExercise) {
        workoutTemplateDao.upsertWorkoutTemplateExercise(exercise.toEntity())
    }

    suspend fun deleteWorkoutTemplateExercise(exercise: WorkoutTemplateExercise) {
        workoutTemplateDao.deleteWorkoutTemplateExercise(exercise.toEntity())
    }

    fun getExercisesForTemplate(templateId: Uuid): Flow<List<WorkoutTemplateExercise>> {
        return workoutTemplateDao.getExercisesForTemplate(templateId).map { entities ->
            entities.map { it.toWorkoutTemplateExercise() }
        }
    }

    // WorkoutTemplateSet operations (usually managed via the parent WorkoutTemplateExercise)
    suspend fun upsertWorkoutTemplateSet(set: WorkoutTemplateSet) {
        workoutTemplateDao.upsertWorkoutTemplateSet(set.toEntity())
    }

    suspend fun deleteWorkoutTemplateSet(set: WorkoutTemplateSet) {
        workoutTemplateDao.deleteWorkoutTemplateSet(set.toEntity())
    }

    fun getSetsForTemplateExercise(templateExerciseId: Uuid): Flow<List<WorkoutTemplateSet>> {
        return workoutTemplateDao.getSetsForTemplateExercise(templateExerciseId).map { entities ->
            entities.map { it.toWorkoutTemplateSet() }
        }
    }

    // Favorite methods
    suspend fun addFavorite(profileId: Uuid, workoutTemplateId: Uuid) {
        workoutTemplateDao.addFavorite(ProfileFavoriteWorkoutTemplateCrossRef(profileId, workoutTemplateId))
    }

    suspend fun removeFavorite(profileId: Uuid, workoutTemplateId: Uuid) {
        workoutTemplateDao.removeFavorite(ProfileFavoriteWorkoutTemplateCrossRef(profileId, workoutTemplateId))
    }

    fun getFavoriteWorkoutTemplateIds(profileId: Uuid): Flow<List<Uuid>> =
        workoutTemplateDao.getFavoriteWorkoutTemplateIds(profileId)

    fun isFavorite(profileId: Uuid, workoutTemplateId: Uuid): Flow<Boolean> =
        workoutTemplateDao.isFavorite(profileId, workoutTemplateId)
}
