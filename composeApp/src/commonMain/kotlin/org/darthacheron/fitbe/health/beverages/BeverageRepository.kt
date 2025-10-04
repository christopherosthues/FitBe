package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class BeverageRepository(private val beverageDao: BeverageDao) {
    fun getBeveragesOverview(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<Beverage>> {
        val timeZone = TimeZone.currentSystemDefault()
        val dateSpan = toDateSpan(startDate, endDate, timeZone)

        return beverageDao.getBeverages(
            start = dateSpan.first,
            end = dateSpan.second,
            profileId = profileId
        ).map { list ->
            list.map { it.toBeverage() }
        }
    }

    fun getTodayBeverages(profileId: Uuid): Flow<List<Beverage>> {
        val today: Instant = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.atStartOfDayIn(TimeZone.currentSystemDefault())
        val dateSpan = toDateSpan(today, today)

        return beverageDao.getBeverages(
            start = dateSpan.first,
            end = dateSpan.second,
            profileId = profileId
        ).map { entities -> entities.map { it.toBeverage() } }
    }

    fun getBeveragesForDate(date: LocalDate, profileId: Uuid): Flow<List<Beverage>> {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone)
        val dateSpan = toDateSpan(startOfDay, startOfDay)

        return beverageDao.getBeverages(
            start = dateSpan.first,
            end = dateSpan.second,
            profileId = profileId
        ).map { entities -> entities.map { it.toBeverage() } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(beverage: Beverage) {
        beverageDao.upsertBeverage(intake = beverage.toBeverageEntity())
    }
}
