package org.darthacheron.fitbe.health.weight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class BodyWeightRepository(
    private val bodyWeightDao: BodyWeightDao
) {
    fun getBodyWeights(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<BodyWeight>> {
        val dateSpan = toDateSpan(startDate, endDate)
        return bodyWeightDao
            .getBodyWeightsBetweenDates(
                start = dateSpan.first,
                end = dateSpan.second,
                profileId = profileId
            ).map { list -> list.map { it.toBodyWeight() } }
    }

    suspend fun addBodyWeight(bodyWeight: BodyWeight) {
        bodyWeightDao.upsertBodyWeight(bodyWeight.toBodyWeightEntity())
    }
}