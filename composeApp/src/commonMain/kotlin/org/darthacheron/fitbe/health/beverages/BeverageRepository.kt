package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalTime::class)
class BeverageRepository(private val dao: BeverageDao) {
    private val today: Instant = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date.atStartOfDayIn(TimeZone.UTC) // yyyy-MM-dd

    val todayDrinks: Flow<List<Beverage>> = loadTodayDrinks()

    private fun loadTodayDrinks(): Flow<List<Beverage>> {
        val dateSpan = toDateSpan(today, today)
        return dao.getTodayDrinks(dateSpan.first.toString(), dateSpan.second.toString())
            .map { beverageEntities -> beverageEntities.map { it.toDomain() } }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addBeverage(amount: Int, beverage: String, unit: FluidUnit) {
        dao.upsertDrink(BeverageEntity(dateUtc = today, amount = amount, beverage = beverage, unit = unit))
    }
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun BeverageEntity.toDomain(): Beverage {
    return Beverage(
        id = id,
        dateUtc = dateUtc,
//        dateLocal = LocalDate.parse(dateUtc).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
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
            dateUtc = dateUtc,
//            dateLocal = LocalDate.parse(dateUtc).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
            amount = it.amount,
            beverage = beverage,
            unit = unit
        )
    }
}