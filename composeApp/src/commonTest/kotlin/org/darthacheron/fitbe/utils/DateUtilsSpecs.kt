package org.darthacheron.fitbe.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DateUtilsSpecs {
    @Test
    fun `test Jan 1, 2025 is in ISO week 1 of 2025`() {
        val date = LocalDate(2025, 1, 1)
        assertEquals(2025 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test Jan 5, 2025 is in ISO week 1 of 2025`() {
        val date = LocalDate(2025, 1, 5)
        assertEquals(2025 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test Jan 6, 2025 is in ISO week 2 of 2025`() {
        val date = LocalDate(2025, 1, 6)
        assertEquals(2025 to 2, date.isoWeekAndYear())
    }

    @Test
    fun `test Jul 28, 2025 is in ISO week 31 of 2025`() {
        val date = LocalDate(2025, 7, 28)
        assertEquals(2025 to 31, date.isoWeekAndYear())
    }

    @Test
    fun `test Jul 27, 2025 is in ISO week 30 of 2025`() {
        val date = LocalDate(2025, 7, 27)
        assertEquals(2025 to 30, date.isoWeekAndYear())
    }

    @Test
    fun `test Dec 30, 2024 is in ISO week 1 of 2025`() {
        val date = LocalDate(2024, 12, 30)
        assertEquals(2025 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test Dec 31, 2024 is in ISO week 1 of 2025`() {
        val date = LocalDate(2024, 12, 31)
        assertEquals(2025 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test Dec 29, 2025 is in ISO week 1 of 2026`() {
        val date = LocalDate(2025, 12, 29)
        assertEquals(2026 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test Dec 28, 2025 is in ISO week 52 of 2025`() {
        val date = LocalDate(2025, 12, 28)
        assertEquals(2025 to 52, date.isoWeekAndYear())
    }

    @Test
    fun `test first day of 2024 (a leap year)`() {
        val date = LocalDate(2024, 1, 1)
        assertEquals(2024 to 1, date.isoWeekAndYear())
    }

    @Test
    fun `test last day of 2023 (not a leap year)`() {
        val date = LocalDate(2023, 12, 31)
        assertEquals(2023 to 52, date.isoWeekAndYear())
    }

    @Test
    fun `test Monday stays the same`() {
        val date = LocalDate(2025, 7, 28) // Monday
        assertEquals(LocalDate(2025, 7, 28), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test Tuesday goes back one day`() {
        val date = LocalDate(2025, 7, 29) // Tuesday
        assertEquals(LocalDate(2025, 7, 28), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test Sunday goes back six days`() {
        val date = LocalDate(2025, 8, 3) // Sunday
        assertEquals(LocalDate(2025, 7, 28), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test week crossing year boundary`() {
        val date = LocalDate(2024, 12, 31) // Tuesday
        assertEquals(LocalDate(2024, 12, 30), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test first day of year`() {
        val date = LocalDate(2025, 1, 1) // Wednesday
        assertEquals(LocalDate(2024, 12, 30), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test leap year date`() {
        val date = LocalDate(2024, 2, 29) // Thursday
        assertEquals(LocalDate(2024, 2, 26), date.firstDayOfIsoWeek())
    }

    @Test
    fun `test first day of January`() {
        val date = LocalDate(2025, 1, 15)
        assertEquals(LocalDate(2025, 1, 1), date.firstDayOfMonth())
    }

    @Test
    fun `test first day of February in leap year`() {
        val date = LocalDate(2024, 2, 29)
        assertEquals(LocalDate(2024, 2, 1), date.firstDayOfMonth())
    }

    @Test
    fun `test first day of December`() {
        val date = LocalDate(2025, 12, 25)
        assertEquals(LocalDate(2025, 12, 1), date.firstDayOfMonth())
    }

    @Test
    fun `test first day of month already first day`() {
        val date = LocalDate(2025, 3, 1)
        assertEquals(LocalDate(2025, 3, 1), date.firstDayOfMonth())
    }

    @Test
    fun `test normal year`() {
        val date = LocalDate(2025, 7, 15)
        assertEquals(LocalDate(2025, 1, 1), date.firstDayOfYear())
    }

    @Test
    fun `test leap year`() {
        val date = LocalDate(2024, 12, 31)
        assertEquals(LocalDate(2024, 1, 1), date.firstDayOfYear())
    }

    @Test
    fun `test first day of the year already first day`() {
        val date = LocalDate(2023, 1, 1)
        assertEquals(LocalDate(2023, 1, 1), date.firstDayOfYear())
    }

    private val baseStart = LocalDate(2024, 2, 15).atStartOfDayIn(TimeZone.UTC)
    private val baseEnd = LocalDate(2024, 2, 16).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun testPlusOneDay() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.DAY,
                TimeZone.UTC
            )
        val result = pair.plusOne()
        val expectedStart = LocalDate(2024, 2, 16).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 17).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneDay() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.DAY,
                TimeZone.UTC
            )
        val result = pair.minusOne()
        val expectedStart = LocalDate(2024, 2, 14).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 15).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneWeek() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.WEEK,
                TimeZone.UTC
            )
        val result = pair.plusOne()
        val expectedStart = LocalDate(2024, 2, 22).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 23).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneWeek() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.WEEK,
                TimeZone.UTC
            )
        val result = pair.minusOne()
        val expectedStart = LocalDate(2024, 2, 8).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 9).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneCalendarMonth() {
        val result =
            DateRange(
                LocalDate(2024, 12, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 12, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.MONTH,
                TimeZone.UTC
            ).plusOne()
        val expectedStart = LocalDate(2025, 1, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2025, 1, 31).atStartOfDayIn(TimeZone.UTC) // Last day of March
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneCalendarMonthFebruaryLeapYear() {
        val result =
            DateRange(
                LocalDate(2024, 1, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 1, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.MONTH,
                TimeZone.UTC
            ).plusOne()
        val expectedStart = LocalDate(2024, 2, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 29).atStartOfDayIn(TimeZone.UTC) // Last day of March
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneMonth() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.MONTH,
                TimeZone.UTC
            )
        val result = pair.plusOne()
        val expectedStart = LocalDate(2024, 3, 15).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 3, 31).atStartOfDayIn(TimeZone.UTC) // Last day of March
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneCalendarMonth() {
        val result =
            DateRange(
                LocalDate(2024, 12, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 12, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.MONTH,
                TimeZone.UTC
            ).minusOne()
        val expectedStart = LocalDate(2024, 11, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 11, 30).atStartOfDayIn(TimeZone.UTC) // Last day of Jan
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneCalendarMonthFebruaryLeapYear() {
        val result =
            DateRange(
                LocalDate(2024, 3, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 3, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.MONTH,
                TimeZone.UTC
            ).minusOne()
        val expectedStart = LocalDate(2024, 2, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 2, 29).atStartOfDayIn(TimeZone.UTC) // Last day of Jan
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneMonth() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.MONTH,
                TimeZone.UTC
            )
        val result = pair.minusOne()
        val expectedStart = LocalDate(2024, 1, 15).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2024, 1, 31).atStartOfDayIn(TimeZone.UTC) // Last day of Jan
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneCalendarYear() {
        val result =
            DateRange(
                LocalDate(2024, 1, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 12, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.YEAR,
                TimeZone.UTC
            ).minusOne()
        val expectedStart = LocalDate(2023, 1, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2023, 12, 31).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneYear() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.YEAR,
                TimeZone.UTC
            )
        val result = pair.plusOne()
        val expectedStart = LocalDate(2025, 2, 15).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2025, 2, 16).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testPlusOneCalendarYear() {
        val result =
            DateRange(
                LocalDate(2024, 1, 1).atStartOfDayIn(TimeZone.UTC),
                LocalDate(2024, 12, 31).atStartOfDayIn(TimeZone.UTC),
                DateUnit.YEAR,
                TimeZone.UTC
            ).plusOne()
        val expectedStart = LocalDate(2025, 1, 1).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2025, 12, 31).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }

    @Test
    fun testMinusOneYear() {
        val pair =
            DateRange(
                baseStart,
                baseEnd,
                DateUnit.YEAR,
                TimeZone.UTC
            )
        val result = pair.minusOne()
        val expectedStart = LocalDate(2023, 2, 15).atStartOfDayIn(TimeZone.UTC)
        val expectedEnd = LocalDate(2023, 2, 16).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expectedStart, result.startDate)
        assertEquals(expectedEnd, result.endDate)
    }
}