package org.darthacheron.fitbe.health.sleep

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

class SleepRepository(private val dao: SleepDao) {
    fun getSleepsBetween(start: Long, end: Long): Flow<List<Sleep>> =
        dao.getSleepsBetween(start, end)
            .map { sleepEntities -> sleepEntities.map { it.toDomain() } }

    suspend fun addSleep(sleep: SleepEntity) = dao.upsertSleep(sleep)
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun SleepEntity.toDomain(): Sleep {
    return Sleep(
        id = id,
        dateUtc = dateUtc,
        dateLocal = LocalDate.parse(dateUtc).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
        minutes = minutes,
        hours = hours,
    )
}