package org.darthacheron.fitbe.exercises.exercises

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
interface ExerciseDao { // Example, you might have separate DAOs
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

    @Transaction
    suspend fun resetExerciseToDefault(equipmentId: Uuid) {
        val defaultExercise = getDefaultExerciseByIdForReset(equipmentId)
        if (defaultExercise != null) {
            upsertExercise(defaultExercise.toExerciseEntity())
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseEquipmentCrossRef(crossRef: ExerciseEquipmentCrossRef)

    @Query("DELETE FROM exercise_equipment_cross_ref WHERE exerciseId = :exerciseId AND equipmentId = :equipmentId")
    suspend fun removeExerciseEquipmentCrossRef(exerciseId: Uuid, equipmentId: Uuid)


    @Transaction // Important for relations
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseWithEquipment(exerciseId: Uuid): Flow<ExerciseWithEquipmentEntity?>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: Uuid): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>
}
