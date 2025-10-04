package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_overview_error_loading
import fitbe.composeapp.generated.resources.top_bar_title_steps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsOverviewViewModel(
    private val stepsRepository: StepsRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<StepsOverviewError, StepsOverviewUiState>(settingsRepository, topBarManager) {
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

    private val stepsDataFlow: StateFlow<List<StepsOverview>> = combine(
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings.selectedProfileId?.let { profileId ->
            Pair(settings, range.dateUnit) to stepsRepository.getSteps(
                range.startDate,
                range.endDate,
                profileId
            )
        } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
    }.flatMapLatest { (settingsDateUnit, stepsSource) ->
        stepsSource.map { steps ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(steps)
                DateUnit.WEEK -> mapWeek(steps)
                DateUnit.MONTH -> mapMonth(steps)
                DateUnit.YEAR -> mapYear(steps)
            }
        }
    }
    .onStart {
        isLoading.value = true
        errorMessage.value = null
    }
    .catch {
        isLoading.value = false
        errorMessage.value = Res.string.steps_overview_error_loading
        emit(emptyList())
    }
    .map { steps ->
        isLoading.value = false
        steps
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val uiState: StateFlow<StepsOverviewUiState> = combine(
        stepsDataFlow,
        isLoading,
        errorMessage
    ) { steps, isLoading, errorMessage ->
        StepsOverviewUiState(
            isLoading = isLoading,
            steps = steps,
            dates = steps.map { it.date },
            error = StepsOverviewError(errorMessage)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StepsOverviewUiState(isLoading = true)
    )

    val maxSteps: StateFlow<UInt> = uiState
        .map { it.steps }
        .map { stepsList ->
            if (stepsList.isEmpty()) ProfileDefaults.STEPS else stepsList.maxOfOrNull { it.steps } ?: ProfileDefaults.STEPS
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    private fun mapDay(steps: List<Steps>): List<StepsOverview> {
        return aggregateDailySteps(steps)
    }

    private fun aggregateDailySteps(steps: List<Steps>): List<StepsOverview> {
        if (steps.isEmpty()) {
            return emptyList()
        }

        return steps.groupBy { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date }
            .map { (date, group) ->
                StepsOverview(
                    date = date,
                    steps = group.sumOf { it.steps }
                )
            }
    }

    private fun <K> aggregateAverageStepsByPeriod(
        dailySteps: List<StepsOverview>,
        groupKeySelector: (StepsOverview) -> K,
        representativeDateSelector: (List<StepsOverview>) -> LocalDate,
        daysInPeriodSelector: (List<StepsOverview>) -> Int
    ): List<StepsOverview> {
        if (dailySteps.isEmpty()) return emptyList()

        return dailySteps.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val sumSteps = group.sumOf { it.steps }
                val daysInPeriod = daysInPeriodSelector(group)
                if (daysInPeriod == 0) return@mapNotNull null

                val avgSteps = (sumSteps.toDouble() / daysInPeriod).roundToInt().toUInt()

                StepsOverview(
                    date = representativeDateSelector(group),
                    steps = avgSteps
                )
            }
    }

    private fun mapWeek(
        stepList: List<Steps>,
    ): List<StepsOverview> {
        val dailySteps = aggregateDailySteps(stepList)
        return aggregateAverageStepsByPeriod(
            dailySteps = dailySteps,
            groupKeySelector = { it.date.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().date.firstDayOfIsoWeek() },
            daysInPeriodSelector = { _ -> 7 }
        )
    }

    private fun mapMonth(stepsList: List<Steps>): List<StepsOverview> {
        val dailySteps = aggregateDailySteps(stepsList)
        return aggregateAverageStepsByPeriod(
            dailySteps = dailySteps,
            groupKeySelector = { it.date.year to it.date.month },
            representativeDateSelector = { group ->
                group.first().date.firstDayOfMonth()
            },
            daysInPeriodSelector = { group ->
                val localDate = group.first().date
                val firstDay = localDate.firstDayOfMonth()
                val nextMonth = if (firstDay.monthNumber == 12) {
                    LocalDate(firstDay.year + 1, 1, 1)
                } else {
                    LocalDate(firstDay.year, firstDay.monthNumber + 1, 1)
                }
                firstDay.daysUntil(nextMonth)
            }
        )
    }

    private fun mapYear(stepsList: List<Steps>): List<StepsOverview> {
        val dailySteps = aggregateDailySteps(stepsList)
        return aggregateAverageStepsByPeriod(
            dailySteps = dailySteps,
            groupKeySelector = { it.date.year },
            representativeDateSelector = { group -> group.first().date.firstDayOfYear() },
            daysInPeriodSelector = { group ->
                val year = group.first().date.year
                val isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
                if (isLeap) 366 else 365
            }
        )
    }

    fun addSteps(date: LocalDate, steps: UInt) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            settings.selectedProfileId?.let { profileId ->
                stepsRepository.addSteps(
                    Steps(
                        profileId = profileId,
                        date = date.atStartOfDayIn(TimeZone.currentSystemDefault()),
                        steps = steps
                    )
                )
            }
        }
    }
}