package org.darthacheron.fitbe.health.sleep

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SleepRepository(private val dao: SleepDao) {
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun getSleepsBetween(start: Instant, end: Instant, profileId: Uuid): Flow<List<Sleep>> {
        val dateSpan = toDateSpan(start, end)
        // TODO: start is not correct
        return dao.getSleepsBetween(dateSpan.first.toString(), dateSpan.second.toString(), profileId)
            .map { sleepEntities -> sleepEntities.map { it.toSleep() } }
    }

    suspend fun addSleep(sleep: Sleep) = dao.upsertSleep(toEntity(sleep))
}
