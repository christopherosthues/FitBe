package org.darthacheron.fitbe.workouts.templates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.workouts.exercises.Exercise
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
class WorkoutTemplateRepository(
    private val workoutTemplateDao: WorkoutTemplateDao
) {
    private var _dummyTemplates: List<WorkoutTemplate>

    init {
        _dummyTemplates = dummyTemplates()
    }

    private fun dummyTemplates(): List<WorkoutTemplate> {
        val dummyTemplate1Id = Uuid.random()
        val dummyTemplate2Id = Uuid.random()

        val dummyTemplates =
            listOf(
                WorkoutTemplate(
                    id = dummyTemplate1Id,
                    name = "Full Body Blast (Dummy)",
                    description = "A comprehensive workout for all major muscle groups.",
                    imageUri = null,
                    default = true,
                    exercises =
                        listOf(
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Bench Press",
                                        guide = "A classic chest exercise.",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.WEIGHT_REPS,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 0,
                                sets = emptyList()
                            ),
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Skull Crushers",
                                        guide = "Guide",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.WEIGHT_REPS,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 1,
                                sets = emptyList()
                            ),
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Jumping Jacks",
                                        guide = "Guide",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.REPS_ONLY,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 2,
                                sets = emptyList()
                            )
                        )
                ),
                WorkoutTemplate(
                    id = dummyTemplate2Id,
                    name = "Upper Body Strength (Dummy)",
                    description = "Focuses on building strength in the upper body.",
                    imageUri = null,
                    default = false,
                    exercises =
                        listOf(
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Bench Press 2",
                                        guide = "A classic chest exercise.",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.WEIGHT_REPS,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 0,
                                sets = emptyList()
                            ),
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Skull Crushers 2",
                                        guide = "Guide",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.WEIGHT_REPS,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 1,
                                sets = emptyList()
                            ),
                            WorkoutTemplateExercise(
                                id = Uuid.random(),
                                workoutTemplateId = dummyTemplate1Id,
                                exercise =
                                    Exercise(
                                        id = Uuid.random(),
                                        name = "Jumping Jacks 2",
                                        guide = "Guide",
                                        imageUri = null,
                                        targetMuscleGroups = emptyList(),
                                        default = false,
                                        recommendedFor = emptyList(),
                                        exerciseType = ExerciseType.REPS_ONLY,
                                        dateUtc =
                                            Clock.System
                                                .now()
                                                .toLocalDateTime(TimeZone.UTC)
                                                .date
                                    ),
                                exerciseOrder = 2,
                                sets = emptyList()
                            )
                        )
                )
            )
        return dummyTemplates
    }

    // WorkoutTemplate operations
//    suspend fun upsertWorkoutTemplate(template: WorkoutTemplate) {
//        workoutTemplateDao.upsertWorkoutTemplate(template.toEntity())
//        // Upsert exercises and their sets if they are part of the domain model
//        template.exercises.forEach { exercise ->
//            workoutTemplateDao.upsertWorkoutTemplateExercise(exercise.toEntity())
//            if (exercise.sets.isNotEmpty()) {
//                workoutTemplateDao.upsertWorkoutTemplateSets(exercise.sets.map { it.toEntity() })
//            }
//        }
//    }

    suspend fun deleteWorkoutTemplate(template: WorkoutTemplate) {
        // Deleting the template should cascade delete its exercises and sets due to ForeignKey constraints
        workoutTemplateDao.deleteWorkoutTemplate(template.toWorkoutTemplateEntity())
    }

    fun getWorkoutTemplateWithExercisesAndSets(templateId: Uuid): Flow<WorkoutTemplate?> {
        return flowOf(_dummyTemplates.firstOrNull { it.id == templateId })
//        return workoutTemplateDao.getWorkoutTemplateWithExercisesAndSets(templateId).map {
//            it?.toWorkoutTemplate()
//        }
    }

//    fun getAllWorkoutTemplatesWithExercisesAndSets(): Flow<List<WorkoutTemplate>> {
//        return workoutTemplateDao.getAllWorkoutTemplatesWithExercisesAndSets().map { entities ->
//            entities.map { it.toWorkoutTemplate() }
//        }
//    }

    fun getAllWorkoutTemplatesWithExercises(): Flow<List<WorkoutTemplate>> {
        // Return dummy data here. Comment productive code out, don't delete it
//        return workoutTemplateDao.getAllWorkoutTemplatesWithExercises().map { entities ->
//            entities.map { it.toWorkoutTemplate() }
//        }
        return flowOf(_dummyTemplates)
    }

    // Simpler fetch for just template headers (e.g., for a list view)
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplate>> =
        workoutTemplateDao.getAllWorkoutTemplates().map { entities ->
            entities.map { it.toWorkoutTemplate() }
        }

    // WorkoutTemplateExercise operations (usually managed via the parent WorkoutTemplate)
//    suspend fun upsertWorkoutTemplateExercise(exercise: WorkoutTemplateExercise) {
//        workoutTemplateDao.upsertWorkoutTemplateExercise(exercise.toEntity())
//    }
//
//    suspend fun deleteWorkoutTemplateExercise(exercise: WorkoutTemplateExercise) {
//        workoutTemplateDao.deleteWorkoutTemplateExercise(exercise.toEntity())
//    }
//
//    fun getExercisesForTemplate(templateId: Uuid): Flow<List<WorkoutTemplateExercise>> {
//        return workoutTemplateDao.getExercisesForTemplate(templateId).map { entities ->
//            entities.map { it.toWorkoutTemplateExercise() }
//        }
//    }

    // WorkoutTemplateSet operations (usually managed via the parent WorkoutTemplateExercise)
//    suspend fun upsertWorkoutTemplateSet(set: WorkoutTemplateSet) {
//        workoutTemplateDao.upsertWorkoutTemplateSet(set.toEntity())
//    }
//
//    suspend fun deleteWorkoutTemplateSet(set: WorkoutTemplateSet) {
//        workoutTemplateDao.deleteWorkoutTemplateSet(set.toEntity())
//    }
//
//    fun getSetsForTemplateExercise(templateExerciseId: Uuid): Flow<List<WorkoutTemplateSet>> {
//        return workoutTemplateDao.getSetsForTemplateExercise(templateExerciseId).map { entities ->
//            entities.map { it.toWorkoutTemplateSet() }
//        }
//    }

    // Favorite methods
    suspend fun addFavorite(
        profileId: Uuid,
        workoutTemplateId: Uuid
    ) {
        workoutTemplateDao.addFavorite(ProfileFavoriteWorkoutTemplateCrossRef(profileId, workoutTemplateId))
    }

    suspend fun removeFavorite(
        profileId: Uuid,
        workoutTemplateId: Uuid
    ) {
        workoutTemplateDao.removeFavorite(ProfileFavoriteWorkoutTemplateCrossRef(profileId, workoutTemplateId))
    }

    fun getFavoriteWorkoutTemplateIds(profileId: Uuid): Flow<List<Uuid>> =
        workoutTemplateDao.getFavoriteWorkoutTemplateIds(profileId)

    fun isFavorite(
        profileId: Uuid,
        workoutTemplateId: Uuid
    ): Flow<Boolean> = workoutTemplateDao.isFavorite(profileId, workoutTemplateId)
}