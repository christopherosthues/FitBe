package org.darthacheron.fitbe.date.week

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import org.darthacheron.fitbe.components.date.week.YearWeek
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class YearWeekTest {
    @Test
    fun testYearWeekInitialization() {
        val yearWeek = YearWeek(2023, 1)
        assertEquals(2023, yearWeek.year)
        assertEquals(1, yearWeek.week)
    }

    @Test
    fun testYearWeekInvalidYear() {
        try {
            YearWeek(0, 1)
            fail("No exception was thrown!")
        } catch (exception: IllegalArgumentException) {
            assertTrue { true }
        }
    }

    @Test
    fun testYearWeekInvalidWeek() {
        try {
            YearWeek(2023, 0)
            fail("No exception was thrown!")
        } catch (exception: IllegalArgumentException) {
            assertTrue { true }
        }
    }

    @Test
    fun testWeeksUntil() {
        val yearWeek1 = YearWeek(2023, 1)
        val yearWeek2 = YearWeek(2023, 2)
        assertEquals(1, yearWeek1.weeksUntil(yearWeek2))
    }

    @Test
    fun testCompareTo() {
        val yearWeek1 = YearWeek(2023, 1)
        val yearWeek2 = YearWeek(2023, 2)
        val yearWeek3 = YearWeek(2024, 1)
        assertTrue(yearWeek1 < yearWeek2)
        assertTrue(yearWeek2 > yearWeek1)
        assertTrue(yearWeek3 > yearWeek2)
    }

    @Test
    fun testToString() {
        val yearWeek = YearWeek(2023, 1)
        assertEquals("2023-W1", yearWeek.toString())
    }

    @Test
    fun testStartDateMillisFirstWeekOfYear() {
        val yearWeek = YearWeek(2023, 1)
        val startDateMillis = yearWeek.startDateMillis()
        val expectedDate = LocalDate(2023, 1, 2).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, startDateMillis)
    }

    @Test
    fun testEndDateMillisFirstWeekOfYear() {
        val yearWeek = YearWeek(2023, 1)
        val endDateMillis = yearWeek.endDateMillis()
        val expectedDate =
            LocalDateTime(2023, 1, 8, 23, 59, 59, 999).toInstant(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, endDateMillis)
    }

    @Test
    fun testStartDateMillisLastWeekOfYear() {
        val yearWeek = YearWeek(2023, 52)
        val startDateMillis = yearWeek.startDateMillis()
        val expectedDate =
            LocalDate(2023, 12, 25).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, startDateMillis)
    }

    @Test
    fun testEndDateMillisLastWeekOfYear() {
        val yearWeek = YearWeek(2023, 52)
        val endDateMillis = yearWeek.endDateMillis()
        val expectedDate =
            LocalDateTime(2023, 12, 31, 23, 59, 59, 999)
                .toInstant(TimeZone.UTC)
                .toEpochMilliseconds()
        assertEquals(expectedDate, endDateMillis)
    }

    @Test
    fun testStartDateMillisMiddleWeekOfYear() {
        val yearWeek = YearWeek(2023, 26)
        val startDateMillis = yearWeek.startDateMillis()
        val expectedDate = LocalDate(2023, 6, 26).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, startDateMillis)
    }

    @Test
    fun testEndDateMillisMiddleWeekOfYear() {
        val yearWeek = YearWeek(2023, 26)
        val endDateMillis = yearWeek.endDateMillis()
        val expectedDate =
            LocalDateTime(2023, 7, 2, 23, 59, 59, 999).toInstant(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, endDateMillis)
    }

    @Test
    fun testStartDateMillisLeapYear() {
        val yearWeek = YearWeek(2024, 1)
        val startDateMillis = yearWeek.startDateMillis()
        val expectedDate = LocalDate(2024, 1, 1).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, startDateMillis)
    }

    @Test
    fun testEndDateMillisLeapYear() {
        val yearWeek = YearWeek(2024, 1)
        val endDateMillis = yearWeek.endDateMillis()
        val expectedDate =
            LocalDateTime(2024, 1, 7, 23, 59, 59, 999).toInstant(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, endDateMillis)
    }

    @Test
    fun testStartDateMillisYear2025() {
        val yearWeek = YearWeek(2025, 1)
        val startDateMillis = yearWeek.startDateMillis()
        val expectedDate =
            LocalDate(2024, 12, 30).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, startDateMillis)
    }

    @Test
    fun testEndDateMillisYear2025() {
        val yearWeek = YearWeek(2025, 1)
        val endDateMillis = yearWeek.endDateMillis()
        val expectedDate =
            LocalDateTime(2025, 1, 5, 23, 59, 59, 999).toInstant(TimeZone.UTC).toEpochMilliseconds()
        assertEquals(expectedDate, endDateMillis)
    }
}