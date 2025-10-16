package org.darthacheron.fitbe.health.sleep

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SleepRepository(
    private val dao: SleepDao
) {
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun getSleepsBetween(
        start: Instant,
        end: Instant,
        profileId: Uuid
    ): Flow<List<Sleep>> {
        val dateSpan = toDateSpan(start, end)
        return dao
            .getSleepsBetween(
                start = dateSpan.first,
                end = dateSpan.second,
                profileId = profileId
            ).map { sleepEntities ->
                val timeZone = TimeZone.currentSystemDefault()
                val queryStartDate = start.toLocalDateTime(timeZone).date
                val queryEndDate = end.toLocalDateTime(timeZone).date.plus(1, DateTimeUnit.DAY)

                sleepEntities.flatMap { entity ->
                    val originalSleep = entity.toSleep()
                    val sleepStartDateTime = originalSleep.start.toLocalDateTime(timeZone)
                    val sleepEndDateTime = originalSleep.end.toLocalDateTime(timeZone)

                    val sleepStartDate = sleepStartDateTime.date
                    val sleepEndDate = sleepEndDateTime.date

                    // If the sleep session is already within a single day, no splitting is needed.
                    if (sleepStartDate == sleepEndDate) {
                        return@flatMap listOf(originalSleep)
                    }

                    val splitSleeps = mutableListOf<Sleep>()
                    var currentInstant = originalSleep.start

                    // Loop through the days covered by the sleep session
                    while (currentInstant < originalSleep.end) {
                        val currentLocalDateTime = currentInstant.toLocalDateTime(timeZone)
                        val currentDate = currentLocalDateTime.date

                        // The end of the current day is the start of the next day.
                        val endOfDayInstant =
                            currentDate.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)

                        // Determine the end of the split segment.
                        // It's either the end of the original sleep or the end of the current day, whichever comes first.
                        val segmentEndInstant =
                            if (originalSleep.end < endOfDayInstant) originalSleep.end else endOfDayInstant

                        // Create a new Sleep object for this day's segment.
                        if (currentInstant < segmentEndInstant &&
                            currentDate >= queryStartDate &&
                            currentDate < queryEndDate
                        ) {
                            splitSleeps.add(
                                Sleep(
                                    id = Uuid.random(), // Generate new ID for the split part
                                    profileId = originalSleep.profileId,
                                    start = currentInstant,
                                    end = segmentEndInstant
                                )
                            )
                        }

                        // Move to the start of the next segment.
                        currentInstant = segmentEndInstant
                    }
                    splitSleeps
                }
            }
    }

    fun getSleeps(
        date: LocalDate,
        profileId: Uuid
        ): Flow<List<Sleep>> {
            val startOfDay = date.atStartOfDayIn(TimeZone.currentSystemDefault())

            return getSleepsBetween(
                start = startOfDay,
                end = startOfDay,
                profileId = profileId
            )
        }

    suspend fun addSleep(sleep: Sleep) = dao.upsertSleep(sleep.toSleepEntity())
}