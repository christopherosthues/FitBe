package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class BeverageRepository(
    private val beverageDao: BeverageDao
) {
    fun getBeveragesOverview(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<Beverage>> {
        val timeZone = TimeZone.currentSystemDefault()
        val dateSpan = toDateSpan(startDate, endDate, timeZone)

        return beverageDao
            .getBeverages(
                start = dateSpan.first,
                end = dateSpan.second,
                profileId = profileId
            ).map { list ->
                list.map { it.toBeverage() }
            }
    }

    fun getBeveragesForDate(
        date: LocalDate,
        profileId: Uuid
    ): Flow<List<Beverage>> {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone)

        return getBeveragesOverview(
            startDate = startOfDay,
            endDate = startOfDay,
            profileId = profileId
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(beverage: Beverage) {
        beverageDao.upsertBeverage(intake = beverage.toBeverageEntity())
    }

    suspend fun deleteBeverage(id: Uuid) {
        beverageDao.deleteBeverage(id)
    }
}