package org.darthacheron.fitbe.workouts.templates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface WorkoutTemplateDao {
    @Upsert
    suspend fun upsertWorkoutTemplate(workoutTemplate: WorkoutTemplateEntity)

    @Delete
    suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplateEntity)

    @Query("SELECT * FROM workout_templates WHERE id = :id")
    fun getWorkoutTemplateById(id: Uuid): Flow<WorkoutTemplateEntity?>

    @Query("SELECT * FROM workout_templates ORDER BY name ASC")
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateEntity>>

    // DefaultWorkoutTemplateEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefaultWorkoutTemplate(defaultWorkoutTemplate: DefaultWorkoutTemplateEntity)

    @Query("SELECT * FROM default_workout_templates WHERE id = :id")
    fun getDefaultWorkoutTemplateById(id: Uuid): Flow<DefaultWorkoutTemplateEntity?>

    @Query("SELECT * FROM default_workout_templates ORDER BY name ASC")
    fun getAllDefaultWorkoutTemplates(): Flow<List<DefaultWorkoutTemplateEntity>>

    // WorkoutTemplateExerciseEntity operations
//    @Upsert
//    suspend fun upsertWorkoutTemplateExercise(exercise: WorkoutTemplateExerciseEntity)
//
//    @Upsert
//    suspend fun upsertWorkoutTemplateExercises(exercises: List<WorkoutTemplateExerciseEntity>)
//
//    @Delete
//    suspend fun deleteWorkoutTemplateExercise(exercise: WorkoutTemplateExerciseEntity)
//
//    @Query("SELECT * FROM workout_template_exercises WHERE workoutTemplateId = :workoutTemplateId ORDER BY exerciseOrder ASC")
//    fun getExercisesForTemplate(workoutTemplateId: Uuid): Flow<List<WorkoutTemplateExerciseEntity>>

    // WorkoutTemplateSetEntity operations
//    @Upsert
//    suspend fun upsertWorkoutTemplateSet(set: WorkoutTemplateSetEntity)
//
//    @Upsert
//    suspend fun upsertWorkoutTemplateSets(sets: List<WorkoutTemplateSetEntity>)
//
//    @Delete
//    suspend fun deleteWorkoutTemplateSet(set: WorkoutTemplateSetEntity)
//
//    @Query("SELECT * FROM workout_template_sets WHERE workoutTemplateExerciseId = :workoutTemplateExerciseId ORDER BY setOrder ASC")
//    fun getSetsForTemplateExercise(workoutTemplateExerciseId: Uuid): Flow<List<WorkoutTemplateSetEntity>>

    // Transaction methods to get full template details
//    @Transaction
//    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
//    fun getWorkoutTemplateWithExercisesAndSets(templateId: Uuid): Flow<WorkoutTemplateWithExercisesAndSets?>

//    @Transaction
//    @Query("SELECT * FROM workout_templates ORDER BY name ASC")
//    fun getAllWorkoutTemplatesWithExercisesAndSets(): Flow<List<WorkoutTemplateWithExercisesAndSets>>

//    @Transaction
//    @Query("SELECT * FROM workout_templates ORDER BY name ASC")
//    fun getAllWorkoutTemplatesWithExercises(): Flow<List<WorkoutTemplateWithExercisesEntity>>

    // Methods for managing ProfileFavoriteWorkoutTemplateCrossRef
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(crossRef: ProfileFavoriteWorkoutTemplateCrossRef)

    @Delete
    suspend fun removeFavorite(crossRef: ProfileFavoriteWorkoutTemplateCrossRef)

    @Query("SELECT workoutTemplateId FROM profile_favorite_workout_template_cross_ref WHERE profileId = :profileId")
    fun getFavoriteWorkoutTemplateIds(profileId: Uuid): Flow<List<Uuid>>

    @Query(
        """
            SELECT EXISTS(SELECT 1 FROM profile_favorite_workout_template_cross_ref
                          WHERE profileId = :profileId AND workoutTemplateId = :workoutTemplateId)
        """
    )
    fun isFavorite(
        profileId: Uuid,
        workoutTemplateId: Uuid
    ): Flow<Boolean>
}