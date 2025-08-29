package org.darthacheron.fitbe.exercises

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface ExerciseDao { // Example, you might have separate DAOs

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEquipment(equipment: TrainingEquipmentEntity): Long

    // --- Methods for the Join Table ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseEquipmentCrossRef(crossRef: ExerciseEquipmentCrossRef)

    @Query("DELETE FROM exercise_equipment_cross_ref WHERE exerciseId = :exerciseId AND equipmentId = :equipmentId")
    suspend fun removeExerciseEquipmentCrossRef(exerciseId: Uuid, equipmentId: Uuid)


    @Transaction // Important for relations
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseWithEquipment(exerciseId: Uuid): Flow<ExerciseWithEquipmentEntity?>

    @Transaction
    @Query("SELECT * FROM exercises")
    fun getAllExercisesWithEquipment(): Flow<List<ExerciseWithEquipmentEntity>>

    @Transaction
    @Query("SELECT * FROM training_equipment WHERE id = :equipmentId")
    fun getEquipmentWithExercises(equipmentId: Uuid): Flow<EquipmentWithExercisesEntity?>

    @Transaction
    @Query("SELECT * FROM training_equipment")
    fun getAllEquipmentWithExercises(): Flow<List<EquipmentWithExercisesEntity>>
}
