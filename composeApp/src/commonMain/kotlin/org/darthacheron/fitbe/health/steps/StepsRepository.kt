package org.darthacheron.fitbe.health.steps

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StepsRepository(private val stepsDao: StepsDao) {
    fun getSteps(startDate: Instant,
                 endDate: Instant,
                 profileId: Uuid): Flow<List<Steps>> {
        val dateSpan = toDateSpan(startDate, endDate)
        return stepsDao.getStepsBetweenDates(dateSpan.first.toString(),
            dateSpan.second.toString(), profileId).map {
                list -> list.map { it.toSteps() }
        }
    }

    fun getTodaySteps(profileId: Uuid): Flow<Steps> {
        val today: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .date
        return stepsDao.getStepsForDate(today.toString(), profileId).map {
            it.toSteps()
        }
    }

    suspend fun addSteps(profileId: Uuid, date: LocalDate, steps: UInt) {
        stepsDao.upsertSteps(
            StepsEntity(
                profileId = profileId,
                dateUtc = date,
                steps = steps.toInt()
            )
        )
    }

    suspend fun updateSteps(steps: Steps) {
        stepsDao.upsertSteps(toEntity(steps))
    }

    suspend fun deleteSteps(steps: Steps) {
        stepsDao.deleteSteps(toEntity(steps))
    }
}