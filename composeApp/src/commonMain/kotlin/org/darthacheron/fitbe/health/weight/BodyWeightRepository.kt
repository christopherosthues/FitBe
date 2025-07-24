package org.darthacheron.fitbe.health.weight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.utils.toDateSpan
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

    suspend fun addBodyWeight(entry: BodyWeight) {
        bodyWeightDao.upsertBodyWeight(BodyWeightEntity.fromBodyWeight(entry))
    }
}