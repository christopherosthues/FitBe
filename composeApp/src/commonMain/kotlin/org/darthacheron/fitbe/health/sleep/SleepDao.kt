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
//    @Query("SELECT * FROM sleeps WHERE profileId = :profileId AND dateUtc BETWEEN SUBSTR(:start, 1, 10) AND SUBSTR(:end, 1, 10) ORDER BY dateUtc ASC")
    @Query("SELECT * FROM sleeps WHERE profileId = :profileId")
//    fun getSleepsBetween(start: String, end: String, profileId: Uuid): Flow<List<SleepEntity>>
    fun getSleepsBetween(profileId: Uuid): Flow<List<SleepEntity>>

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
    fun getSleepsBetween(start: Instant, end: Instant, profileId: Uuid): Flow<List<SleepEntity>>

    @Upsert()
    suspend fun upsertSleep(sleep: SleepEntity)
}
