package org.darthacheron.fitbe.health.steps

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.darthacheron.fitbe.health.beverages.Beverage
import org.darthacheron.fitbe.health.beverages.toBeverageEntity
import org.darthacheron.fitbe.utils.toDateSpan
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class StepsRepository(
    private val stepsDao: StepsDao
) {
    suspend fun getSteps(id: Uuid): Steps? {
        return stepsDao.getSteps(id)?.toSteps()
    }

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

    fun getSteps(
        date: LocalDate,
        profileId: Uuid
    ): Flow<List<Steps>> {
        val startOfDay = date.atStartOfDayIn(TimeZone.currentSystemDefault())

        return getSteps(
            startDate = startOfDay,
            endDate = startOfDay,
            profileId = profileId
        )
    }

    suspend fun addSteps(steps: Steps) {
        stepsDao.upsertSteps(steps = steps.toStepsEntity())
    }

    suspend fun editSteps(steps: Steps) {
        stepsDao.upsertSteps(steps = steps.toStepsEntity())
    }

    suspend fun deleteSteps(id: Uuid) {
        stepsDao.deleteSteps(id)
    }
}