package org.darthacheron.fitbe.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus

@OptIn(ExperimentalTime::class)
fun toDateSpan(start: Instant, end: Instant): Pair<Instant, Instant> {
    return Pair(start.toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC), end.toLocalDateTime(
        TimeZone.UTC).date.atTime(23, 59, 59, 0).toInstant(TimeZone.UTC))
}

@OptIn(ExperimentalTime::class)
fun toDateSpan(end: Instant, dateTimeUnit: DateTimeUnit.DateBased, dateInPast: Int): Pair<Instant, Instant> {
    return Pair(end.toLocalDateTime(TimeZone.UTC).date.minus(dateInPast, dateTimeUnit).atStartOfDayIn(
        TimeZone.UTC), end.toLocalDateTime(
        TimeZone.UTC).date.atTime(23, 59, 59, 0).toInstant(TimeZone.UTC))
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
object PastOrPresentSelectableDates : SelectableDates {
    private val timeZone = TimeZone.currentSystemDefault()
    private val today = Clock.System.now().toLocalDateTime(timeZone).date

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(timeZone).date
        return date <= today
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= today.year
    }
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

fun LocalDate.firstDayOfMonth(): LocalDate {
    return LocalDate(this.year, this.monthNumber, 1)
}

fun LocalDate.firstDayOfYear(): LocalDate {
    return LocalDate(this.year, 1, 1)
}