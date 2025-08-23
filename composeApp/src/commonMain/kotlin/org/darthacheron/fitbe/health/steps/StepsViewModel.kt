package org.darthacheron.fitbe.health.steps


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.minusOne
import org.darthacheron.fitbe.utils.plusOne
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsViewModel(
    private val stepsRepository: StepsRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _dateRange = MutableStateFlow(
        DateRange(
            Clock.System.now().minus(6.days),
            Clock.System.now(),
            DateUnit.DAY
        )
    )

    val dateRange: StateFlow<DateRange> = _dateRange

    val targetSteps: StateFlow<UInt?> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetSteps }
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val steps: StateFlow<List<Steps>> = combine(
        _dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        Pair(settings, range.dateUnit) to stepsRepository.getSteps(
            range.startDate,
            range.endDate,
            settings.selectedProfileId!!
        )
    }.flatMapLatest { (settingsDateUnit, stepsFlow) ->
        stepsFlow.map { steps ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(steps)
                DateUnit.WEEK -> mapWeek(steps)
                DateUnit.MONTH -> mapMonth(steps)
                DateUnit.YEAR -> mapYear(steps)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    private fun mapDay(steps: List<Steps>): List<Steps> {
        return steps
    }

    private fun mapWeek(
        stepList: List<Steps>,
    ): List<Steps> = stepList.groupBy { step -> step.dateUtc.isoWeekAndYear() }
        .map { stepsDateMap ->
            var steps = 0
            stepsDateMap.value.forEach {
                steps += it.steps
            }

            Steps(
                id = Uuid.random(),
                dateUtc = stepsDateMap.value.first().dateUtc.firstDayOfIsoWeek(),
                profileId = stepsDateMap.value.first().profileId,
                steps = steps / stepsDateMap.value.size,
            )
        }

    private fun mapMonth(stepsList: List<Steps>): List<Steps> {
        return stepsList.groupBy { step -> step.dateUtc.year to step.dateUtc.month }
            .map { stepsDateMap ->
                var steps = 0
                stepsDateMap.value.forEach {
                    steps += it.steps
                }

                Steps(
                    id = Uuid.random(),
                    dateUtc = stepsDateMap.value.first().dateUtc.firstDayOfMonth(),
                    profileId = stepsDateMap.value.first().profileId,
                    steps = steps / stepsDateMap.value.size,
                )
            }
    }

    private fun mapYear(stepsList: List<Steps>): List<Steps> {
        return stepsList.groupBy { step -> step.dateUtc.year }
            .map { stepsDateMap ->
                var steps = 0
                stepsDateMap.value.forEach {
                    steps += it.steps
                }

                Steps(
                    id = Uuid.random(),
                    dateUtc = stepsDateMap.value.first().dateUtc.firstDayOfYear(),
                    profileId = stepsDateMap.value.first().profileId,
                    steps = steps / stepsDateMap.value.size,
                )
            }
    }

    fun movePast() {
        val range = dateRange.value.minusOne()
        setRange(range)
    }

    fun moveFuture() {
        val range = dateRange.value.plusOne()
        setRange(range)
    }

    fun dates(bodyWeights: List<BodyWeight>): List<LocalDate> {
        return bodyWeights.map { it.dateUtc }
    }

    fun setRange(startDate: Instant, endDate: Instant, dateUnit: DateUnit) {
        _dateRange.value = DateRange(startDate, endDate, dateUnit)
    }

    fun setRange(range: DateRange) {
        _dateRange.value = range
    }

    fun addSteps(date: LocalDate, steps: UInt) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            stepsRepository.addSteps(
                profileId = settings.selectedProfileId!!,
                date = date,
                steps = steps,
            )
        }
    }

    // TODO: update steps
}