package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.steps.Steps
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.collections.map
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class BeverageRepository(private val beverageDao: BeverageDao) {
    fun getBeveragesOverview(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<BeverageOverview>> {
        val dateSpan = toDateSpan(startDate, endDate)
        return beverageDao.getBeverages(
            dateSpan.first.toString(),
            dateSpan.second.toString(), profileId
        ).map { list ->
            list.map { it.toDomain() }.groupBy { beverage ->
                beverage.dateUtc.toLocalDateTime(
                    TimeZone.UTC
                ).date
            }.map { (date, beverages) ->
                BeverageOverview(
                    date,
                    beverages.sumOf { it.amount.toInt() }.toUInt()
                )
            }
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
        ).map { entities -> entities.map { it.toDomain() } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(amount: UInt, beverage: String, unit: FluidUnit, profileId: Uuid) {
        val today: Instant = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .date.atStartOfDayIn(TimeZone.UTC)
        beverageDao.upsertBeverage(
            BeverageEntity(
                dateUtc = today,
                amount = amount.toInt(),
                beverage = beverage,
                unit = unit,
                profileId = profileId
            )
        )
    }
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun BeverageEntity.toDomain(): Beverage {
    return Beverage(
        id = id,
        profileId = profileId,
        dateUtc = dateUtc,
        amount = amount.toUInt(),
        beverage = beverage,
        unit = unit
    )
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun BeverageEntity?.toDomainOrNull(): Beverage? {
    return this?.let {
        Beverage(
            id = id,
            profileId = profileId,
            dateUtc = dateUtc,
            amount = it.amount.toUInt(),
            beverage = beverage,
            unit = unit
        )
    }
}