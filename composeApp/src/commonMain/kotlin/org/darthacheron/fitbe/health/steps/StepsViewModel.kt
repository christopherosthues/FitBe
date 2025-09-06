package org.darthacheron.fitbe.health.steps


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_steps
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
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDao
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.minusOne
import org.darthacheron.fitbe.utils.plusOne
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsViewModel(
    private val stepsRepository: StepsRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<Steps>(settingsRepository, profileRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_steps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    val targetSteps: StateFlow<UInt?> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetSteps }
            } else {
                flowOf(ProfileDefaults.STEPS)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    @OptIn(ExperimentalCoroutinesApi::class)
    val steps: StateFlow<List<Steps>> = combine(
        dateRange,
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

    val maxSteps: StateFlow<UInt> = steps
        .map { stepsList ->
            if (stepsList.isEmpty()) ProfileDefaults.STEPS else stepsList.maxOf { it.steps }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    private fun mapDay(steps: List<Steps>): List<Steps> {
        return steps
    }

    private fun <K> aggregateStepsByPeriod(
        steps: List<Steps>,
        groupKeySelector: (Steps) -> K,
        representativeDateSelector: (List<Steps>) -> LocalDate
    ): List<Steps> {
        if (steps.isEmpty()) return emptyList()

        return steps.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgSteps = group.sumOf { it.steps }.toDouble() / groupSize

                Steps(
                    id = Uuid.random(),
                    dateUtc = representativeDateSelector(group),
                    profileId = group.first().profileId,
                    steps = avgSteps.roundToInt().toUInt(),
                )
            }
    }

    private fun mapWeek(
        stepList: List<Steps>,
    ): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepList,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(stepsList: List<Steps>): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepsList,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(stepsList: List<Steps>): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepsList,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    override fun dates(list: List<Steps>): List<LocalDate> {
        return list.map { it.dateUtc }
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