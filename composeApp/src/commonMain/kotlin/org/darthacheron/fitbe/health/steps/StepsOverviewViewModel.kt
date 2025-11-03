package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_overview_content_description_add_steps
import fitbe.composeapp.generated.resources.steps_overview_error_loading
import fitbe.composeapp.generated.resources.steps_overview_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_overview_steps
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
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.components.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.roundToDecimals
import org.darthacheron.fitbe.utils.roundUpToNextTen
import org.jetbrains.compose.resources.StringResource
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class StepsOverviewViewModel(
    private val stepsRepository: StepsRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<StepsOverviewError, StepsOverviewUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_overview_steps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val addButtonContentDescription: StringResource
        get() = Res.string.steps_overview_content_description_add_steps

    private val targetSteps: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetSteps?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val stepsDataFlow: StateFlow<List<StepsOverview>> =
        combine(
            dateRange,
            settingsRepository.getSettingsFlow()
        ) { range, settings ->
            settings.selectedProfileId?.let { profileId ->
                Pair(settings, range.dateUnit) to
                    stepsRepository.getSteps(
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
        }.onStart {
            isLoading.value = true
            errorMessage.value = null
        }.catch {
            isLoading.value = false
            errorMessage.value = Res.string.steps_overview_error_loading
            emit(emptyList())
        }.map {
            isLoading.value = false
            it
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val maxSteps: StateFlow<Int> =
        combine(
            stepsDataFlow,
            targetSteps
        ) { stepsList, targetSteps ->
            if (stepsList.isEmpty()) {
                max(ProfileDefaults.STEPS.toInt(), targetSteps ?: ProfileDefaults.STEPS.toInt())
            } else {
                max(
                    (stepsList.maxOfOrNull { it.steps }?.roundUpToNextTen()?.toInt() ?: ProfileDefaults.STEPS.toInt()),
                    targetSteps ?: ProfileDefaults.STEPS.toInt()
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS.toInt())

    override val uiState: StateFlow<StepsOverviewUiState> =
        combine(
            stepsDataFlow,
            maxSteps,
            targetSteps,
            isLoading,
            errorMessage
        ) { steps, maxSteps, target, isLoading, errorMessage ->
            StepsOverviewUiState(
                isLoading = isLoading,
                steps = steps,
                maxSteps = maxSteps,
                target = target,
                dates = steps.map { it.date },
                error = StepsOverviewError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StepsOverviewUiState(isLoading = true)
        )

    private fun mapDay(steps: List<Steps>): List<StepsOverview> = aggregateDailySteps(steps)

    private fun aggregateDailySteps(steps: List<Steps>): List<StepsOverview> {
        if (steps.isEmpty()) {
            return emptyList()
        }

        return steps
            .groupBy { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date }
            .map { (date, group) ->
                StepsOverview(
                    date = date,
                    steps = group.sumOf { it.steps }.toDouble()
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

        return dailySteps
            .groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val sumSteps = group.sumOf { it.steps }
                val daysInPeriod = daysInPeriodSelector(group)
                if (daysInPeriod == 0) return@mapNotNull null

                val avgSteps = (sumSteps / daysInPeriod).roundToDecimals(2)

                StepsOverview(
                    date = representativeDateSelector(group),
                    steps = avgSteps
                )
            }
    }

    private fun mapWeek(stepList: List<Steps>): List<StepsOverview> {
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
                val nextMonth =
                    if (firstDay.monthNumber == 12) {
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

    fun addSteps(
        date: Instant,
        steps: UInt
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.steps_overview_error_saving
                    return@launch
                }

                stepsRepository.addSteps(
                    Steps(
                        profileId = profileId,
                        date = date,
                        steps = steps
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.steps_overview_error_saving
            }
        }
    }
}