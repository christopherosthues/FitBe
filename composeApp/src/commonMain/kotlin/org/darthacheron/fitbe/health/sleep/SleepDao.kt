package org.darthacheron.fitbe.health.sleep

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface SleepDao {
    @Query("SELECT * FROM beverages WHERE dateUtc = :today")
    suspend fun getSleeps(startDate: String, endDate: String): Flow<List<SleepEntity>>
}