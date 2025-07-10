package org.darthacheron.fitbe.health.beverages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalTime::class)
class BeverageRepository(private val dao: BeverageDao) {
    private val today: String = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date.toString() // yyyy-MM-dd

    val todayDrinks: Flow<List<Beverage>> = dao.getTodayDrinks(today)
        .map { beverageEntities -> beverageEntities.map { it.toDomain() } }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addDrink(amount: Int, beverage: String, unit: FluidUnit) {
        dao.upsertDrink(BeverageEntity(date = today, amount = amount, beverage = beverage, unit = unit))
    }
}

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
fun BeverageEntity.toDomain(): Beverage {
    return Beverage(
        id = id,
        dateUtc = date,
        dateLocal = LocalDate.parse(date).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
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
            dateUtc = date,
            dateLocal = LocalDate.parse(date).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
            amount = it.amount,
            beverage = beverage,
            unit = unit
        )
    }
}