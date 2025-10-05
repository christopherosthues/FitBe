package org.darthacheron.fitbe.health.sleeps

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import org.darthacheron.fitbe.database.FitBeDatabase
import org.darthacheron.fitbe.db.createInMemoryRoomDatabase
import org.darthacheron.fitbe.health.sleep.SleepDao
import org.darthacheron.fitbe.health.sleep.SleepEntity
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.profile.Gender
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SleepRepositoryTest {

    private lateinit var db: FitBeDatabase
    private lateinit var sleepDao: SleepDao
    private lateinit var profileDao: ProfileDao
    private lateinit var repository: SleepRepository

    private val testProfileId = Uuid.random()
    private val germanTimeZone = TimeZone.of("Europe/Berlin")

    @BeforeTest
    fun setup() = runTest {
        db = createInMemoryRoomDatabase()
        sleepDao = db.sleepDao
        profileDao = db.profileDao
        repository = SleepRepository(sleepDao)

        profileDao.upsertProfile(
            ProfileEntity(
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
        )
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `getSleepsBetween returns empty list when no data exists`() = runTest {
        val startDate = LocalDate(2025, 10, 1).atStartOfDayIn(germanTimeZone)
        val endDate = LocalDate(2025, 10, 31).atStartOfDayIn(germanTimeZone)

        repository.getSleepsBetween(startDate, endDate, testProfileId).test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween returns single sleep that is within one day`() = runTest {
        // --- GIVEN ---
        val sleepStart = LocalDateTime(2025, 10, 15, 2, 0).toInstant(germanTimeZone)
        val sleepEnd = LocalDateTime(2025, 10, 15, 8, 0).toInstant(germanTimeZone)
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 10, 15).atStartOfDayIn(germanTimeZone)
        val queryEnd = LocalDate(2025, 10, 16).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(1, sleeps.size, "Should return one sleep session")
            assertEquals(sleepStart, sleeps.first().start)
            assertEquals(sleepEnd, sleeps.first().end)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween splits a sleep session that crosses midnight`() = runTest {
        // --- GIVEN ---
        val sleepStart =
            LocalDateTime(2025, 10, 15, 22, 0).toInstant(germanTimeZone) // 10 PM on 15th
        val sleepEnd = LocalDateTime(2025, 10, 16, 6, 0).toInstant(germanTimeZone)   // 6 AM on 16th
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 10, 15).atStartOfDayIn(germanTimeZone)
        val queryEnd = LocalDate(2025, 10, 17).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(2, sleeps.size, "Sleep should be split into two parts")

            val firstPart = sleeps[0]
            val secondPart = sleeps[1]
            val midnight = LocalDate(2025, 10, 16).atStartOfDayIn(germanTimeZone)

            // Check first part (Oct 15)
            assertEquals(sleepStart, firstPart.start)
            assertEquals(midnight, firstPart.end)

            // Check second part (Oct 16)
            assertEquals(midnight, secondPart.start)
            assertEquals(sleepEnd, secondPart.end)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween handles sleep spanning multiple days`() = runTest {
        // --- GIVEN ---
        val sleepStart =
            LocalDateTime(2025, 11, 10, 21, 0).toInstant(germanTimeZone) // 9 PM on 10th
        val sleepEnd = LocalDateTime(2025, 11, 12, 5, 0).toInstant(germanTimeZone)   // 5 AM on 12th
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 11, 10).atStartOfDayIn(germanTimeZone)
        val queryEnd = LocalDate(2025, 11, 13).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(3, sleeps.size, "Sleep should be split into three parts")

            val midnight11 = LocalDate(2025, 11, 11).atStartOfDayIn(germanTimeZone)
            val midnight12 = LocalDate(2025, 11, 12).atStartOfDayIn(germanTimeZone)

            // Part 1: 10th
            assertEquals(sleepStart, sleeps[0].start)
            assertEquals(midnight11, sleeps[0].end)

            // Part 2: 11th (full day)
            assertEquals(midnight11, sleeps[1].start)
            assertEquals(midnight12, sleeps[1].end)

            // Part 3: 12th
            assertEquals(midnight12, sleeps[2].start)
            assertEquals(sleepEnd, sleeps[2].end)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween correctly clips sleep starting before range`() = runTest {
        // --- GIVEN ---
        val sleepStart = LocalDateTime(
            2025,
            10,
            14,
            22,
            0
        ).toInstant(germanTimeZone) // Starts before query range
        val sleepEnd = LocalDateTime(2025, 10, 15, 6, 0).toInstant(germanTimeZone)
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 10, 15).atStartOfDayIn(germanTimeZone)
        val queryEnd = LocalDate(2025, 10, 16).atStartOfDayIn(germanTimeZone)

        // --- THEN ---
        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(
                1,
                sleeps.size,
                "Should only get the part of the sleep within the query range"
            )

            val firstPart = sleeps.first()
            assertEquals(
                queryStart,
                firstPart.start,
                "First part should start at the beginning of the query range"
            )
            assertEquals(sleepEnd, firstPart.end)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween correctly clips sleep ending after range`() = runTest {
        // --- GIVEN ---
        val sleepStart = LocalDateTime(2025, 10, 15, 22, 0).toInstant(germanTimeZone)
        val sleepEnd =
            LocalDateTime(2025, 10, 16, 6, 0).toInstant(germanTimeZone) // Ends after query range
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 10, 15).atStartOfDayIn(germanTimeZone)
        val queryEnd =
            LocalDate(2025, 10, 16).atStartOfDayIn(germanTimeZone) // Query ends at midnight

        // --- THEN ---
        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(
                1,
                sleeps.size,
                "Should only get the part of the sleep within the query range"
            )

            val firstPart = sleeps.first()
            assertEquals(sleepStart, firstPart.start)
            assertEquals(
                queryEnd,
                firstPart.end,
                "First part should end at the end of the query range"
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getSleepsBetween EDGE CASE handles Daylight Saving Time change`() = runTest {
        // In 2025, Germany falls back from CEST (UTC+2) to CET (UTC+1) on Oct 26 at 3:00 AM.
        // A day has 25 hours.
        val sleepStart =
            LocalDateTime(2025, 10, 25, 22, 0).toInstant(germanTimeZone) // 10 PM on 25th
        val sleepEnd = LocalDateTime(2025, 10, 26, 8, 0).toInstant(germanTimeZone)   // 8 AM on 26th
        seedSleep(sleepStart, sleepEnd)

        // --- WHEN ---
        val queryStart = LocalDate(2025, 10, 25).atStartOfDayIn(germanTimeZone)
        val queryEnd = LocalDate(2025, 10, 27).atStartOfDayIn(germanTimeZone)

        repository.getSleepsBetween(queryStart, queryEnd, testProfileId).test {
            val sleeps = awaitItem()
            assertEquals(2, sleeps.size)

            val midnight = LocalDate(2025, 10, 26).atStartOfDayIn(germanTimeZone)
            assertEquals(sleepStart, sleeps[0].start)
            assertEquals(midnight, sleeps[0].end)
            assertEquals(midnight, sleeps[1].start)
            assertEquals(sleepEnd, sleeps[1].end)

            // Check duration of the second part, which is on the DST day. Should be 8 hours.
            val durationOnDstDay = sleeps[1].end - sleeps[1].start
            assertTrue(durationOnDstDay.inWholeHours in 7..8, "Duration should be ~8 hours")

            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun seedSleep(start: Instant, end: Instant) {
        sleepDao.upsertSleep(
            SleepEntity(
                profileId = testProfileId,
                startDateTime = start,
                endDateTime = end
            )
        )
    }
}