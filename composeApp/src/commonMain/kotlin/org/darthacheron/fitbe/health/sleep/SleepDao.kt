package org.darthacheron.fitbe.health.sleep

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleeps WHERE dateUtc BETWEEN :start AND :end ORDER BY dateUtc ASC")
    fun getSleepsBetween(start: Long, end: Long): Flow<List<SleepEntity>>

    @Upsert()
    suspend fun upsertSleep(sleep: SleepEntity)
}