package org.darthacheron.fitbe.nutrition.water

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class WaterIntakeRepository(private val dao: WaterIntakeDao) {
    private val today: String = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date.toString() // yyyy-MM-dd

    val todayIntake: Flow<WaterIntake?> = dao.getTodayIntake(today)
        .map { waterIntakeEntity -> waterIntakeEntity.toDomainOrNull() }

    suspend fun setIntake(amount: Int) {
        dao.upsertIntake(WaterIntakeEntity(date = today, amountMl = amount))
    }
}

@OptIn(ExperimentalTime::class)
fun WaterIntakeEntity.toDomain(): WaterIntake {
    return WaterIntake(
        dateUtc = this.date,
        dateLocal = LocalDate.parse(this.date).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
        amount = this.amountMl
    )
}

@OptIn(ExperimentalTime::class)
fun WaterIntakeEntity?.toDomainOrNull(): WaterIntake? {
    return this?.let {
        WaterIntake(
            dateUtc = this.date,
            dateLocal = LocalDate.parse(this.date).atStartOfDayIn(TimeZone.currentSystemDefault()).toString(),
            amount = it.amountMl
        )
    }
}