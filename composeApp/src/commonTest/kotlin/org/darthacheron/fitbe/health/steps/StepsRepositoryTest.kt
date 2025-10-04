package org.darthacheron.fitbe.health.steps

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
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StepsRepositoryTest {

    private lateinit var db: FitBeDatabase
    private lateinit var stepsDao: StepsDao
    private lateinit var profileDao: ProfileDao
    private lateinit var repository: StepsRepository

    private val testProfileId = Uuid.random()
    private val germanTimeZone = TimeZone.of("Europe/Berlin")

    @BeforeTest
    fun setup() = runTest {
        db = createInMemoryRoomDatabase() // This now works!
        stepsDao = db.stepsDao
        profileDao = db.profileDao
        repository = StepsRepository(stepsDao)

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
        profileDao.upsertProfile(profile)
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `getSteps returns empty list when no data exists`() = runTest {
        val startDate = LocalDate(2025, 10, 1).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 31).atStartOfDayIn(germanTimeZone)

        repository.getSteps(startDate, endDate, testProfileId).test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSteps returns entries within a simple date range`() = runTest {
        // --- GIVEN ---
        // Entries inside and outside the query range
        seedSteps(Instant.parse("2025-10-14T22:00:00Z"), 1000) // UTC time, falls on Oct 15 in Berlin
        seedSteps(Instant.parse("2025-10-15T23:00:00Z"), 2000) // UTC time, falls on Oct 16 in Berlin
        seedSteps(Instant.parse("2025-10-17T01:00:00Z"), 3000) // UTC time, falls on Oct 17 in Berlin

        // --- WHEN ---
        // Query for a range in local German time
        val startDate = LocalDate(2025, 10, 15).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 16).atStartOfDayIn(germanTimeZone) // End is exclusive, so query until start of 17th

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(2, entries.size, "Should find 2 entries within the local date range")
            assertEquals(15, entries[0].date.toLocalDateTime(germanTimeZone).dayOfMonth)
            assertEquals(16, entries[1].date.toLocalDateTime(germanTimeZone).dayOfMonth)
            assertEquals(3000u, entries.sumOf { it.steps })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSteps includes entry exactly at midnight start of range`() = runTest {
        // --- GIVEN ---
        // An entry created exactly at midnight in Berlin.
        // This is 2025-10-25 22:00 UTC because Berlin is UTC+2 in October.
        val entryTime = LocalDateTime(2025, 10, 26, 0, 0).toInstant(germanTimeZone)
        seedSteps(entryTime, 5000)

        // --- WHEN ---
        // We query for the range starting on that day.
        val startDate = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
            val entries = awaitItem()
            assertEquals(1, entries.size, "Should find entry exactly at the start of the day")
            assertEquals(entryTime, entries[0].date)
            assertEquals(5000u, entries[0].steps)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSteps EDGE CASE handles year boundary in German time`() = runTest {
        // --- GIVEN ---
        // Entry at 23:30 UTC on Dec 31, 2025. In Berlin (CET, UTC+1), this is 00:30 on Jan 1, 2026.
        val newYearsEntryInstant = LocalDateTime(2026, 1, 1, 0, 30).toInstant(germanTimeZone)
        seedSteps(dateUtc = newYearsEntryInstant, steps = 500)

        // Entry earlier on Dec 31, 2025.
        val oldYearsEntryInstant = LocalDateTime(2025, 12, 31, 10, 0).toInstant(germanTimeZone)
        seedSteps(dateUtc = oldYearsEntryInstant, steps = 10000)


        // --- WHEN ---
        // We query for the single local day of January 1st, 2026.
        val startDate = LocalDate(2026, 1, 1).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2026, 1, 2).atStartOfDayIn(germanTimeZone) // Exclusive end

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
            val stepsList = awaitItem()
            assertEquals(
                1,
                stepsList.size,
                "Should only find the entry that falls into the new year locally"
            )

            val entry = stepsList.first()
            assertEquals(500u, entry.steps)

            // Verify the date of the returned object is correct
            val localEntryDate = entry.date.toLocalDateTime(germanTimeZone).date
            assertEquals(LocalDate(2026, 1, 1), localEntryDate)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSteps EDGE CASE handles Daylight Saving Time change`() = runTest {
        // In 2025, Germany falls back from CEST (UTC+2) to CET (UTC+1) on Oct 26 at 3:00 AM.
        // 02:59 CEST becomes 02:00 CET. The hour from 02:00 to 02:59 happens "twice".

        // --- GIVEN ---
        // Entry at 00:30 UTC -> 02:30 CEST (the first 2am hour)
        seedSteps(dateUtc = Instant.parse("2025-10-26T00:30:00Z"), steps = 2000)

        // Entry at 01:30 UTC -> 02:30 CET (the second 2am hour)
        seedSteps(dateUtc = Instant.parse("2025-10-26T01:30:00Z"), steps = 3000)

        // --- WHEN ---
        // Query for the local date of Oct 26.
        val startDate = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
            val stepsList = awaitItem()
            assertEquals(2, stepsList.size, "Should find both entries on the day of DST change")
            assertEquals(5000u, stepsList.sumOf { it.steps }, "Should sum steps from both entries")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSteps does not include entry at last nanosecond of previous day`() = runTest {
        // --- GIVEN ---
        // An entry created exactly at midnight in Berlin, which marks the start of Oct 26.
        val startOfTestDay = LocalDateTime(2025, 10, 26, 0, 0).toInstant(germanTimeZone)
        // The last possible moment of the *previous* day.
        val endOfPreviousDay = startOfTestDay.minus(1, DateTimeUnit.NANOSECOND)
        seedSteps(endOfPreviousDay, 9999)

        // --- WHEN ---
        // We query for the range starting on Oct 26.
        val startDate = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 27).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
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
    fun `getSteps includes entry at last nanosecond of the day`() = runTest {
        // --- GIVEN ---
        // The start of the next day in Berlin.
        val startOfNextDay = LocalDateTime(2025, 10, 27, 0, 0).toInstant(germanTimeZone)
        // The last possible moment of the day we want to test (Oct 26).
        val endOfTestDay = startOfNextDay.minus(1, DateTimeUnit.NANOSECOND)
        seedSteps(endOfTestDay, 12345)

        // --- WHEN ---
        // We query for the range of Oct 26. The end date is the exclusive start of Oct 27.
        val startDate = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSteps(startDate, endDate, testProfileId).test {
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

    private suspend fun seedSteps(dateUtc: Instant, steps: Int) {
        stepsDao.upsertSteps(
            StepsEntity(
                profileId = testProfileId,
                dateUtc = dateUtc,
                steps = steps
            )
        )
    }
}