package org.darthacheron.fitbe.workouts.equipment

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
interface EquipmentDao {
    @Upsert
    suspend fun upsertEquipment(equipment: TrainingEquipmentEntity): Long

    @Delete
    suspend fun deleteEquipment(equipment: TrainingEquipmentEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDefaultEquipment(defaultEquipment: DefaultTrainingEquipmentEntity): Long

    @Query("SELECT * FROM default_training_equipment WHERE id = :equipmentId")
    fun getDefaultEquipmentById(equipmentId: Uuid): Flow<DefaultTrainingEquipmentEntity?>

    @Query("SELECT * FROM default_training_equipment WHERE id = :equipmentId")
    suspend fun getDefaultEquipmentByIdForReset(equipmentId: Uuid): DefaultTrainingEquipmentEntity?

    @Query("SELECT * FROM default_training_equipment")
    suspend fun getAllDefaultEquipmentSuspend(): List<DefaultTrainingEquipmentEntity>

    @Transaction
    suspend fun resetEquipmentToDefault(equipmentId: Uuid) {
        val defaultEquipment = getDefaultEquipmentByIdForReset(equipmentId)
        if (defaultEquipment != null) {
            upsertEquipment(defaultEquipment.toTrainingEquipmentEntity())
        }
    }

    @Transaction
    @Query("SELECT * FROM training_equipment WHERE id = :equipmentId")
    fun getEquipmentWithExercises(equipmentId: Uuid): Flow<EquipmentWithExercisesEntity?>

    @Query("SELECT * FROM training_equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Uuid): Flow<TrainingEquipmentEntity?>

    @Query("SELECT * FROM training_equipment")
    fun getAllEquipments(): Flow<List<TrainingEquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(crossRef: ProfileFavoriteEquipmentCrossRef)

    @Delete
    suspend fun removeFavorite(crossRef: ProfileFavoriteEquipmentCrossRef)

    @Query("""
        SELECT equipmentId FROM profile_favorite_equipment_cross_ref
        WHERE profileId = :profileId
    """)
    fun getFavoriteEquipmentIds(profileId: Uuid): Flow<List<Uuid>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM profile_favorite_equipment_cross_ref
            WHERE profileId = :profileId
            AND equipmentId = :equipmentId
        )
    """)
    fun isFavorite(profileId: Uuid, equipmentId: Uuid): Flow<Boolean>
}