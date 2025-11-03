@file:OptIn(ExperimentalTime::class)

package org.darthacheron.fitbe.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun toDateSpan(
    start: Instant,
    end: Instant,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Pair<Instant, Instant> {
    val startOfDay = start.toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)
    val endOfDay =
        end
            .toLocalDateTime(timeZone)
            .date
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)

    return Pair(startOfDay, endOfDay)
}

fun LocalDate.isoWeekAndYear(): Pair<Int, Int> {
    // ISO 8601: Monday = 1, Sunday = 7
    val dayOfWeek = this.dayOfWeek.isoDayNumber // 1..7

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

fun LocalDate.firstDayOfMonth(): LocalDate = LocalDate(this.year, this.monthNumber, 1)

fun Instant.lastDayOfMonth(): Instant {
    val date = this.toLocalDateTime(TimeZone.UTC).date
    val firstOfNextMonth =
        if (date.monthNumber == 12) {
            LocalDate(date.year + 1, 1, 1)
        } else {
            LocalDate(date.year, date.monthNumber + 1, 1)
        }
    return firstOfNextMonth.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
}

fun LocalDate.firstDayOfYear(): LocalDate = LocalDate(this.year, 1, 1)

fun DateRange.plusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(this.timeZone)
    val endDate = this.endDate.toLocalDateTime(this.timeZone)
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(this.timeZone)
        }

        DateUnit.WEEK -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(this.timeZone)
        }

        DateUnit.MONTH -> {
            newStartDate = startDate.date.plus(DatePeriod(months = 1)).atStartOfDayIn(this.timeZone)
            newEndDate =
                endDate.date
                    .plus(DatePeriod(months = 1))
                    .atStartOfDayIn(this.timeZone)
                    .lastDayOfMonth()
        }

        DateUnit.YEAR -> {
            newStartDate = startDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(this.timeZone)
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit, this.timeZone)
}

fun DateRange.minusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(this.timeZone)
    val endDate = this.endDate.toLocalDateTime(this.timeZone)
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(this.timeZone)
        }

        DateUnit.WEEK -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(this.timeZone)
        }

        DateUnit.MONTH -> {
            newStartDate =
                startDate.date.minus(DatePeriod(months = 1)).atStartOfDayIn(this.timeZone)
            newEndDate =
                endDate.date
                    .minus(DatePeriod(months = 1))
                    .atStartOfDayIn(this.timeZone)
                    .lastDayOfMonth()
        }

        DateUnit.YEAR -> {
            newStartDate = startDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(this.timeZone)
            newEndDate = endDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(this.timeZone)
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit, this.timeZone)
}