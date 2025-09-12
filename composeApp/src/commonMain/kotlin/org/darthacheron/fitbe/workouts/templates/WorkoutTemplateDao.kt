package org.darthacheron.fitbe.workouts.templates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
// Import for the relationship data class
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplateWithExercisesAndSets

@OptIn(ExperimentalUuidApi::class)
@Dao
interface WorkoutTemplateDao {

    // WorkoutTemplateEntity operations
    @Upsert
    suspend fun upsertWorkoutTemplate(workoutTemplate: WorkoutTemplateEntity)

    @Delete
    suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplateEntity)

    @Query("SELECT * FROM workout_templates WHERE id = :id")
    fun getWorkoutTemplateById(id: Uuid): Flow<WorkoutTemplateEntity?>

    @Query("SELECT * FROM workout_templates ORDER BY name ASC")
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateEntity>>

    // WorkoutTemplateExerciseEntity operations
    @Upsert
    suspend fun upsertWorkoutTemplateExercise(exercise: WorkoutTemplateExerciseEntity)

    @Upsert
    suspend fun upsertWorkoutTemplateExercises(exercises: List<WorkoutTemplateExerciseEntity>)

    @Delete
    suspend fun deleteWorkoutTemplateExercise(exercise: WorkoutTemplateExerciseEntity)

    @Query("SELECT * FROM workout_template_exercises WHERE workoutTemplateId = :workoutTemplateId ORDER BY exerciseOrder ASC")
    fun getExercisesForTemplate(workoutTemplateId: Uuid): Flow<List<WorkoutTemplateExerciseEntity>>

    // WorkoutTemplateSetEntity operations
    @Upsert
    suspend fun upsertWorkoutTemplateSet(set: WorkoutTemplateSetEntity)

    @Upsert
    suspend fun upsertWorkoutTemplateSets(sets: List<WorkoutTemplateSetEntity>)

    @Delete
    suspend fun deleteWorkoutTemplateSet(set: WorkoutTemplateSetEntity)

    @Query("SELECT * FROM workout_template_sets WHERE workoutTemplateExerciseId = :workoutTemplateExerciseId ORDER BY setOrder ASC")
    fun getSetsForTemplateExercise(workoutTemplateExerciseId: Uuid): Flow<List<WorkoutTemplateSetEntity>>

    // Transaction methods to get full template details
    @Transaction
    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    fun getWorkoutTemplateWithExercisesAndSets(templateId: Uuid): Flow<WorkoutTemplateWithExercisesAndSets?>

    @Transaction
    @Query("SELECT * FROM workout_templates ORDER BY name ASC")
    fun getAllWorkoutTemplatesWithExercisesAndSets(): Flow<List<WorkoutTemplateWithExercisesAndSets>>
}
