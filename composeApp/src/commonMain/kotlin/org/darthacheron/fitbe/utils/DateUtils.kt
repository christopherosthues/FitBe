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
import org.darthacheron.fitbe.components.DateUnit
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.Year
import org.darthacheron.fitbe.components.date.YearMonth
import org.darthacheron.fitbe.components.date.YearWeek

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
    val date = this.toLocalDateTime(TimeZone.UTC).date
    val firstOfNextMonth = if (date.monthNumber == 12) {
        LocalDate(date.year + 1, 1, 1)
    } else {
        LocalDate(date.year, date.monthNumber + 1, 1)
    }
    return firstOfNextMonth.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
}

fun LocalDate.firstDayOfYear(): LocalDate {
    return LocalDate(this.year, 1, 1)
}

fun LocalDate.lastDayOfYear(): LocalDate {
    return LocalDate(this.year, 12, 31)
}

fun Instant.plusOne(viewType: DateUnit): Instant {
    val localDateTime = this.toLocalDateTime(TimeZone.UTC)
    val newLocalDateTime = when(viewType) {
        DateUnit.DAY -> localDateTime.date.plus(DatePeriod(days = 1))
        DateUnit.WEEK -> localDateTime.date.plus(DatePeriod(days = 7))
        DateUnit.MONTH -> localDateTime.date.plus(DatePeriod(months = 1))
        DateUnit.YEAR -> localDateTime.date.plus(DatePeriod(years = 1))
    }
    return newLocalDateTime.atStartOfDayIn(TimeZone.UTC)
}

fun Instant.minusOne(viewType: DateUnit): Instant {
    val localDateTime = this.toLocalDateTime(TimeZone.UTC)
    val newLocalDateTime = when(viewType) {
        DateUnit.DAY -> localDateTime.date.minus(DatePeriod(days = 1))
        DateUnit.WEEK -> localDateTime.date.minus(DatePeriod(days = 7))
        DateUnit.MONTH -> localDateTime.date.minus(DatePeriod(months = 1))
        DateUnit.YEAR -> localDateTime.date.minus(DatePeriod(years = 1))
    }
    return newLocalDateTime.atStartOfDayIn(TimeZone.UTC)
}

fun DateRange.plusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(TimeZone.UTC)
    val endDate = this.endDate.toLocalDateTime(TimeZone.UTC)
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.plus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
        }
        DateUnit.WEEK -> {
            newStartDate = startDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.plus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.UTC)
        }
        DateUnit.MONTH -> {
            newStartDate = startDate.date.plus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = newStartDate.lastDayOfMonth()
        }
        DateUnit.YEAR -> {
            newStartDate = startDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.plus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.UTC)
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit)
}

fun DateRange.minusOne(): DateRange {
    val startDate = this.startDate.toLocalDateTime(TimeZone.UTC)
    val endDate = this.endDate.toLocalDateTime(TimeZone.UTC)
    var newStartDate: Instant
    var newEndDate: Instant
    when (this.dateUnit) {
        DateUnit.DAY -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.minus(DatePeriod(days = 1)).atStartOfDayIn(TimeZone.UTC)
        }
        DateUnit.WEEK -> {
            newStartDate = startDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.minus(DatePeriod(days = 7)).atStartOfDayIn(TimeZone.UTC)
        }
        DateUnit.MONTH -> {
            newStartDate = startDate.date.minus(DatePeriod(months = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = newStartDate.lastDayOfMonth()
        }
        DateUnit.YEAR -> {
            newStartDate = startDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.UTC)
            newEndDate = endDate.date.minus(DatePeriod(years = 1)).atStartOfDayIn(TimeZone.UTC)
        }
    }
    return DateRange(newStartDate, newEndDate, this.dateUnit)
}

fun Year.toEpochMilli(): Long {
    return LocalDate(this.value, 1, 1).atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun YearMonth.toEpochMilli(): Long {
    return LocalDate(this.year, this.month, 1).atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun YearWeek.toEpochMilli(): Long {
    // Step 1: Find the 4th of January — it's always in ISO week 1
    val jan4 = LocalDate(this.year, 1, 4)
    val jan4WeekStart = jan4.minus(DatePeriod(days = jan4.dayOfWeek.isoDayNumber - 1))

    // Step 2: Add weeks to get to the target week
    val weekStartDate = jan4WeekStart.plus((this.week - 1) * 7, DateTimeUnit.DAY)

    // Step 3: Convert to epoch milliseconds
    return weekStartDate
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()
}