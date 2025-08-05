package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class BeverageRepository(private val dao: BeverageDao) {
    fun getTodayBeverages(profileId: Uuid): Flow<List<Beverage>> {
        val today: Instant = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
            .date.atStartOfDayIn(TimeZone.UTC)
        val dateSpan = toDateSpan(today, today)

        return dao.getTodayDrinks(
            start = dateSpan.first.toString(),
            end = dateSpan.second.toString(),
            profileId = profileId
        ).map { entities -> entities.map { it.toDomain() } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(amount: Int, beverage: String, unit: FluidUnit, profileId: Uuid) {
        val today: Instant = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .date.atStartOfDayIn(TimeZone.UTC)
        dao.upsertDrink(
            BeverageEntity(
                dateUtc = today,
                amount = amount,
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
        amount = amount,
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
            amount = it.amount,
            beverage = beverage,
            unit = unit
        )
    }
}