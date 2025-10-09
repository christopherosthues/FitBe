package org.darthacheron.fitbe.health.steps

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.Beverage
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StepsRepository(
    private val stepsDao: StepsDao
) {
    fun getSteps(
        startDate: Instant,
        endDate: Instant,
        profileId: Uuid
    ): Flow<List<Steps>> {
        val dateSpan = toDateSpan(startDate, endDate)
        return stepsDao
            .getStepsBetweenDates(
                start = dateSpan.first,
                end = dateSpan.second,
                profileId = profileId
            ).map { list ->
                list.map { it.toSteps() }
            }
    }

    fun getStepsForDate(
        date: LocalDate,
        profileId: Uuid
    ): Flow<List<Steps>> {
        val startOfDay = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        val dateSpan = toDateSpan(startOfDay, startOfDay)

        return stepsDao
            .getStepsBetweenDates(
                start = dateSpan.first,
                end = dateSpan.second,
                profileId = profileId
            ).map { entities -> entities.map { it.toSteps() } }
    }

    suspend fun addSteps(steps: Steps) {
        stepsDao.upsertSteps(steps.toStepsEntity())
    }

    suspend fun updateSteps(steps: Steps) {
        stepsDao.upsertSteps(steps.toStepsEntity())
    }

    suspend fun deleteSteps(steps: Steps) {
        stepsDao.deleteSteps(steps.toStepsEntity())
    }
}