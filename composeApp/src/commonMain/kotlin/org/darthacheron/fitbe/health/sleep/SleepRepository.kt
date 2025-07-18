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

class SleepRepository(private val dao: SleepDao) {
    @OptIn(ExperimentalTime::class)
    fun getSleepsBetween(start: Instant, end: Instant): Flow<List<Sleep>> {
        val dateSpan = toDateSpan(start, end)
        return dao.getSleepsBetween(dateSpan.first.toString(), dateSpan.second.toString())
            .map { sleepEntities -> sleepEntities.map { it.toDomain() } }
    }

    suspend fun addSleep(sleep: SleepEntity) = dao.upsertSleep(sleep)
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun SleepEntity.toDomain(): Sleep {
    return Sleep(
        id = id,
        dateUtc = dateUtc,
        dateLocal = dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()),
        minutes = minutes.toUInt(),
        hours = hours.toUInt(),
    )
}