package org.darthacheron.fitbe.health.weight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class BodyWeightRepository(private val bodyWeightDao: BodyWeightDao) {
    fun getEntries(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<BodyWeight>> {
        val dateSpan = toDateSpan(startDate, endDate)
        return bodyWeightDao.getBodyWeightsBetweenDates(
            dateSpan.first.toString(),
            dateSpan.second.toString(),
            profileId
        )
            .map { list -> list.map { it.toBodyWeight() } }
    }

    suspend fun addBodyWeight(
        bodyWeight: BodyWeight
    ) {
        bodyWeightDao.upsertBodyWeight(bodyWeight.toBodyWeightEntity())
    }
}