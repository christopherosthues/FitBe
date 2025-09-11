package org.darthacheron.fitbe.profile

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.darthacheron.fitbe.workouts.exercises.ProfileFavoriteExerciseCrossRef
import org.darthacheron.fitbe.workouts.exercises.ProfileWithFavoritesEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE id = :id")
    fun getProfileFlowById(id: Uuid): Flow<ProfileEntity?>

    @Upsert
    suspend fun upsertProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    // In your DAO interface (e.g., ProfileDao.kt or AppDao.kt)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteExercise(favorite: ProfileFavoriteExerciseCrossRef)

    @Delete
    suspend fun removeFavoriteExercise(favorite: ProfileFavoriteExerciseCrossRef)

    @Transaction // Important for relations
    @Query("SELECT * FROM profiles WHERE id = :profileId")
    fun getProfileWithFavorites(profileId: Long): Flow<ProfileWithFavoritesEntity?>

}

