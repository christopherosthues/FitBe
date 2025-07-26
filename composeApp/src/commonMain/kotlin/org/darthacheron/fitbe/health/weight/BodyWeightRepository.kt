package org.darthacheron.fitbe.health.weight

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

    suspend fun addBodyWeight(
        profileId: Uuid,
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double,
        muscleMassInKg: Double,
        boneMassInKg: Double,
        bodyWaterInPercentage: Double
    ) {
        bodyWeightDao.upsertBodyWeight(BodyWeightEntity(
            profileId = profileId,
            dateUtc = date,
            weightInKg = weightInKg,
            bodyFatPercentage = bodyFatPercentage,
            muscleMassInKg = muscleMassInKg,
            boneMassInKg = boneMassInKg,
            bodyWaterInPercentage = bodyWaterInPercentage,
        ))
    }
}