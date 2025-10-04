package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
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
        val dateSpan = toDateSpan(startDate, endDate)
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = startDate.toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)
        val endOfDay = endDate.toLocalDateTime(timeZone).date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)

        return beverageDao.getBeverages(
            startOfDay,
            endOfDay, profileId
        ).map { list ->
            list.map { it.toBeverage() }
        }
    }

    fun getTodayBeverages(profileId: Uuid): Flow<List<Beverage>> {
        val today: Instant = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .date.atStartOfDayIn(TimeZone.UTC)
        val dateSpan = toDateSpan(today, today)

        return beverageDao.getBeverages(
            start = dateSpan.first.toString(),
            end = dateSpan.second.toString(),
            profileId = profileId
        ).map { entities -> entities.map { it.toBeverage() } }
    }

    fun getBeveragesForDate(date: LocalDate, profileId: Uuid): Flow<List<Beverage>> {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone)
        val endOfDay = date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)

        return beverageDao.getBeverages(
            start = startOfDay,
            end = endOfDay,
            profileId = profileId
        ).map { entities -> entities.map { it.toBeverage() } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(beverage: Beverage) {
        beverageDao.upsertBeverage(beverage.toBeverageEntity())
    }
}
