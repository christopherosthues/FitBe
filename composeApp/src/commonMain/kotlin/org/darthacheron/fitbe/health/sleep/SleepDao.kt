package org.darthacheron.fitbe.health.sleep

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Dao
interface SleepDao {
    @Query("SELECT * FROM sleeps WHERE id = :id")
    suspend fun getSleep(id: Uuid): SleepEntity?

    @Upsert()
    suspend fun upsertSleep(sleep: SleepEntity)

    // TODO: check call sites for correct dates
    @Query(
        """
        SELECT * FROM sleeps 
        WHERE profileId = :profileId
        AND startDateTime < :end 
        AND endDateTime > :start
        ORDER BY startDateTime ASC
    """
    )
    fun getSleepsBetween(
        start: Instant,
        end: Instant,
        profileId: Uuid
    ): Flow<List<SleepEntity>>

    @Query("DELETE FROM sleeps WHERE id = :id")
    suspend fun deleteSleep(id: Uuid)
}