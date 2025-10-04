package org.darthacheron.fitbe.health.beverages

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.db.createInMemoryRoomDatabase
import org.darthacheron.fitbe.profile.Gender
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class BeverageRepositoryTest {

    private lateinit var db: FitBeDatabase
    private lateinit var beverageDao: BeverageDao
    private lateinit var repository: BeverageRepository

    // Use a fixed profile for all tests
    private val testProfileId = Uuid.random()

    // We'll use the German timezone for our tests as requested.
    private val germanTimeZone = TimeZone.of("Europe/Berlin")

    @BeforeTest
    fun setup() = runTest {
        // Create an in-memory database instance for each test.
        db = createInMemoryRoomDatabase()
        beverageDao = db.beverageDao
        repository = BeverageRepository(beverageDao)

        // Insert a profile to satisfy foreign key constraints.
        val profile = ProfileEntity(
            id = testProfileId,
            name = "Test User",
            gender = Gender.MALE,
            bodyHeightInCm = 180.0,
            targetWeight = 80.0,
            targetSteps = 10000,
            targetKcal = 2000,
            targetBeverageInMilliliter = 2000,
            targetSleepDuration = 8,
            dateOfBirth = LocalDate(2000, 1, 1)
        )
        db.profileDao.upsertProfile(profile)
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `getBeverages returns empty list when no data exists`() = runTest {
        val startDate = LocalDate(2025, 10, 1).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 31).toInstantAtStartOfDay(germanTimeZone)

        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages returns entries within a simple date range`() = runTest {
        // --- GIVEN ---
        // Entries inside and outside the query range
        seedBeverage("2025-10-14T22:00:00Z") // UTC time, falls on Oct 15 in Berlin
        seedBeverage("2025-10-15T23:00:00Z") // UTC time, falls on Oct 16 in Berlin
        seedBeverage("2025-10-17T01:00:00Z") // UTC time, falls on Oct 17 in Berlin

        // --- WHEN ---
        // Query for a range in local German time. The end date is exclusive.
        val startDate = LocalDate(2025, 10, 15).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 16).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(2, entries.size, "Should find 2 entries within the local date range")
            assertEquals(15, entries[0].date.toLocalDateTime(germanTimeZone).dayOfMonth)
            assertEquals(16, entries[1].date.toLocalDateTime(germanTimeZone).dayOfMonth)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages includes entry exactly at midnight start of range`() = runTest {
        // --- GIVEN ---
        // An entry created exactly at midnight in Berlin.
        val entryTime = LocalDateTime(2025, 10, 26, 0, 0).toInstant(germanTimeZone)
        seedBeverage(entryTime)

        // --- WHEN ---
        // We query for the range starting on that day.
        val startDate = LocalDate(2025, 10, 26).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(1, entries.size, "Should find entry exactly at the start of the day")
            assertEquals(entryTime, entries[0].date)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages EDGE CASE handles Daylight Saving Time change (fall back)`() = runTest {
        // --- GIVEN ---
        // In 2025, Germany falls back from CEST (UTC+2) to CET (UTC+1) on Oct 26 at 3:00 AM.
        // Entry at 00:30 UTC, which is 02:30 CEST (the first 2 AM hour) on Oct 26th
        seedBeverage("2025-10-26T00:30:00Z")

        // Entry at 01:30 UTC, which is 02:30 CET (the second 2 AM hour) on Oct 26th
        seedBeverage("2025-10-26T01:30:00Z")

        // --- WHEN ---
        // We query for the single local day of October 26th.
        val startDate = LocalDate(2025, 10, 26).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(2, entries.size, "Should find all entries on the day of DST change")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages EDGE CASE handles year boundary`() = runTest {
        // --- GIVEN ---
        // An entry on the last hour of the year in UTC, which is already the next year in Berlin.
        seedBeverage("2025-12-31T23:30:00Z") // This is 2026-01-01 00:30 CET in Berlin

        // An entry on the last day of the year in Berlin
        seedBeverage("2025-12-31T10:00:00Z") // 2025-12-31 11:00 CET

        // --- WHEN ---
        // Query for the first day of 2026 in Berlin time.
        val startDate = LocalDate(2026, 1, 1).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2026, 1, 2).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(
                1,
                entries.size,
                "Should find one entry that falls into the new year locally"
            )
            val entryDate = entries.first().date.toLocalDateTime(germanTimeZone)
            assertEquals(2026, entryDate.year)
            assertEquals(1, entryDate.monthNumber)
            assertEquals(1, entryDate.dayOfMonth)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages does not include entry at last nanosecond of previous day`() = runTest {
        // --- GIVEN ---
        val startOfTestDay = LocalDateTime(2025, 10, 26, 0, 0).toInstant(germanTimeZone)
        val endOfPreviousDay = startOfTestDay.minus(1, DateTimeUnit.NANOSECOND)
        seedBeverage(endOfPreviousDay)

        // --- WHEN ---
        val startDate = LocalDate(2025, 10, 26).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(
                0,
                entries.size,
                "Should not include entry from the last nanosecond of the previous day"
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBeverages includes entry at last nanosecond of the day`() = runTest {
        // --- GIVEN ---
        val startOfNextDay = LocalDateTime(2025, 10, 27, 0, 0).toInstant(germanTimeZone)
        val endOfTestDay = startOfNextDay.minus(1, DateTimeUnit.NANOSECOND)
        seedBeverage(endOfTestDay)

        // --- WHEN ---
        val startDate = LocalDate(2025, 10, 26).toInstantAtStartOfDay(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).toInstantAtStartOfDay(germanTimeZone)

        // --- THEN ---
        repository.getBeveragesOverview(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(
                1,
                entries.size,
                "Should include entry from the last nanosecond of the day"
            )
            assertEquals(endOfTestDay, entries.first().date)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Helper function to quickly seed the database with a BeverageEntity
    private suspend fun seedBeverage(utcInstant: Instant) {
        val entity = BeverageEntity(
            profileId = testProfileId,
            dateUtc = utcInstant,
            amount = 250.0,
            beverage = "Water",
            unit = FluidUnit.Milliliter
        )
        beverageDao.upsertBeverage(entity)
    }

    // Overloaded helper to accept ISO string for convenience
    private suspend fun seedBeverage(utcInstantString: String) {
        seedBeverage(Instant.parse(utcInstantString))
    }

    // Helper to convert LocalDate to Instant at the start of that day in a specific timezone
    private fun LocalDate.toInstantAtStartOfDay(timeZone: TimeZone): Instant {
        return this.atStartOfDayIn(timeZone)
    }
}