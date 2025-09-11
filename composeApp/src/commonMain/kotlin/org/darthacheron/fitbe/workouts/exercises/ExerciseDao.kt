package org.darthacheron.fitbe.workouts.exercises

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface ExerciseDao { 
    @Upsert
    suspend fun upsertExercise(exercise: ExerciseEntity): Long

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDefaultExercise(defaultExercise: DefaultExerciseEntity): Long

    @Query("SELECT * FROM default_exercises WHERE id = :exerciseId")
    fun getDefaultExerciseById(exerciseId: Uuid): Flow<DefaultExerciseEntity?>

    @Query("SELECT * FROM default_exercises WHERE id = :exerciseId")
    suspend fun getDefaultExerciseByIdForReset(exerciseId: Uuid): DefaultExerciseEntity?

    @Transaction // Important for relations
    @Query("SELECT * FROM default_exercises WHERE id = :exerciseId")
    fun getDefaultExerciseWithEquipment(exerciseId: Uuid): Flow<DefaultExerciseWithEquipmentEntity?>

    @Transaction
    suspend fun resetExerciseToDefault(exerciseId: Uuid) { // Renamed parameter for clarity
        val defaultExercise = getDefaultExerciseByIdForReset(exerciseId)
        if (defaultExercise != null) {
            upsertExercise(defaultExercise.toExerciseEntity())
            // Also reset equipment links for default exercises if needed
            val defaultEquipmentLinks = getDefaultExerciseWithEquipment(exerciseId).firstOrNull()?.equipmentList?.map { it.id } ?: emptyList()
            updateExerciseEquipmentLinks(exerciseId, defaultEquipmentLinks)
        }
    }

    // Methods for managing ExerciseEquipmentCrossRef
    @Query("DELETE FROM exercise_equipment_cross_ref WHERE exerciseId = :exerciseId")
    suspend fun deleteCrossRefsByExerciseId(exerciseId: Uuid)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // IGNORE might be fine if we always delete first
    suspend fun insertCrossRefs(crossRefs: List<ExerciseEquipmentCrossRef>)

    @Transaction
    suspend fun updateExerciseEquipmentLinks(exerciseId: Uuid, newEquipmentIds: List<Uuid>) {
        deleteCrossRefsByExerciseId(exerciseId)
        if (newEquipmentIds.isNotEmpty()) {
            val newCrossRefs = newEquipmentIds.map { equipmentId ->
                ExerciseEquipmentCrossRef(exerciseId = exerciseId, equipmentId = equipmentId)
            }
            insertCrossRefs(newCrossRefs)
        }
    }

    @Transaction // Important for relations
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseWithEquipment(exerciseId: Uuid): Flow<ExerciseWithEquipmentEntity?>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: Uuid): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    // Methods for managing ProfileFavoriteExerciseCrossRef
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(crossRef: ProfileFavoriteExerciseCrossRef)

    @Delete
    suspend fun removeFavorite(crossRef: ProfileFavoriteExerciseCrossRef)

    @Query("SELECT exerciseId FROM profile_favorite_exercise_cross_ref WHERE profileId = :profileId")
    fun getFavoriteExerciseIds(profileId: Uuid): Flow<List<Uuid>>

    @Query("SELECT EXISTS(SELECT 1 FROM profile_favorite_exercise_cross_ref WHERE profileId = :profileId AND exerciseId = :exerciseId)")
    fun isFavorite(profileId: Uuid, exerciseId: Uuid): Flow<Boolean>
}
