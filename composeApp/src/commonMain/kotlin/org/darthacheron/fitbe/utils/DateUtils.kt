package org.darthacheron.fitbe.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DatePeriod
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.year.Year
import org.darthacheron.fitbe.components.date.month.YearMonth
import org.darthacheron.fitbe.components.date.week.YearWeek

@OptIn(ExperimentalTime::class)
fun toDateSpan(start: Instant, end: Instant): Pair<Instant, Instant> {
    return Pair(start.toLocalDateTime(TimeZone.currentSystemDefault()).date.atStartOfDayIn(TimeZone.currentSystemDefault()), end.toLocalDateTime(
        TimeZone.currentSystemDefault()).date.atTime(23, 59, 59, 999).toInstant(TimeZone.currentSystemDefault()))
}

fun LocalDate.isoWeekAndYear(): Pair<Int, Int> {
    // ISO 8601: Monday = 1, Sunday = 7
    val dayOfWeek = this.dayOfWeek.isoDayNumber  // 1..7

    // Find the nearest Thursday to determine the ISO week-based year
    val nearestThursday = this.plus(DatePeriod(days = 4 - dayOfWeek)) // 4 = Thursday

    val weekBasedYear = nearestThursday.year

    // Start of the first ISO week of the week-based year (Monday of the week containing Jan 4)
    val jan4 = LocalDate(weekBasedYear, 1, 4)
    val jan4DayOfWeek = jan4.dayOfWeek.isoDayNumber
    val week1Start = jan4.minus(DatePeriod(days = jan4DayOfWeek - 1)) // Monday of that week

    // Calculate the number of weeks between week1Start and this date's Monday
    val currentWeekStart = this.minus(DatePeriod(days = dayOfWeek - 1))
    val daysBetween = week1Start.daysUntil(currentWeekStart)

    val weekNumber = (daysBetween / 7) + 1

    return weekBasedYear to weekNumber
}

fun LocalDate.firstDayOfIsoWeek(): LocalDate {
    // ISO 8601: Monday = 1, Sunday = 7
    val dayOfWeek = this.dayOfWeek.isoDayNumber
    return this.minus(DatePeriod(days = dayOfWeek - 1))
}

fun LocalDate.lastDayOfIsoWeek(): LocalDate {
    val dayOfWeek = this.dayOfWeek.isoDayNumber // Monday=1, Sunday=7
    return this.plus(DatePeriod(days = 7 - dayOfWeek))
}

fun LocalDate.firstDayOfMonth(): LocalDate {
    return LocalDate(this.year, this.monthNumber, 1)
}

fun LocalDate.lastDayOfMonth(): LocalDate {
    val firstOfNextMonth = if (this.monthNumber == 12) {
        LocalDate(this.year + 1, 1, 1)
    } else {
        LocalDate(this.year, this.monthNumber + 1, 1)
    }
    return firstOfNextMonth.minus(DatePeriod(days = 1))
}

fun Instant.lastDayOfMonth(): Instant {
    val date = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstOfNextMonth = if (date.monthNumber == 12) {
        LocalDate(date.year + 1, 1, 1)
    } else {
        LocalDate(date.year, date.monthNumber + 1, 1)
    }
    return firstOfNextMonth.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
}

fun LocalDate.firstDayOfYear(): LocalDate {
    return LocalDate(this.year, 1, 1)
}

fun LocalDate.lastDayOfYear(): LocalDate {
    return LocalDate(this.year, 12, 31)
}

fun Instant.plusOne(viewType: DateUnit): Instant {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val newLocalDateTime = when(viewType) {
        DateUnit.DAY -> localDateTime.date.plus(DatePeriod(days = 1))
        DateUnit.WEEK -> localDateTime.date.plus(DatePeriod(days = 7))
        DateUnit.MONTH -> localDateTime.date.plus(DatePeriod(months = 1))
        DateUnit.YEAR -> localDateTime.date.plus(DatePeriod(years = 1))
    }
    return newLocalDateTime.atStartOfDayIn(TimeZone.currentSystemDefault())
}

fun Instant.minusOne(viewType: DateUnit): Instant {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val newLocalDateTime = when(viewType) {
        DateUnit.DAY -> localDateTime.date.minus(DatePeriod(days = 1))
        DateUnit.WEEK -> localDateTime.date.minus(DatePeriod(days = 7))
        DateUnit.MONTH -> localDateTime.date.minus(DatePeriod(months = 1))
        DateUnit.YEAR -> localDateTime.date.minus(DatePeriod(years = 1))
    }
    return newLocalDateTime.atStartOfDayIn(TimeZone.currentSystemDefault())
}

fun DateRange.plusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(TimeZone.currentSystemDefault())
    val endDate = this.endDate.toLocalDateTime(TimeZone.currentSystemDefault())
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
        DateUnit.WEEK -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
        DateUnit.MONTH -> {
            newStartDate = startDate.date.plus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.plus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.currentSystemDefault()).lastDayOfMonth()
        }
        DateUnit.YEAR -> {
            newStartDate = startDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit)
}

fun DateRange.minusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(TimeZone.currentSystemDefault())
    val endDate = this.endDate.toLocalDateTime(TimeZone.currentSystemDefault())
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
        DateUnit.WEEK -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
        DateUnit.MONTH -> {
            newStartDate = startDate.date.minus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.minus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.currentSystemDefault()).lastDayOfMonth()
        }
        DateUnit.YEAR -> {
            newStartDate = startDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
            newEndDate = endDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.currentSystemDefault())
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit)
}
