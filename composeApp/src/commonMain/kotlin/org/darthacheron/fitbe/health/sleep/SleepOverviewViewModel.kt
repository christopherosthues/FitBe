package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_overview_content_description_add_sleep
import fitbe.composeapp.generated.resources.sleep_overview_error_loading
import fitbe.composeapp.generated.resources.sleep_overview_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_overview_sleeps
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
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
import org.jetbrains.compose.resources.StringResource
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.map
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class SleepOverviewViewModel(
    private val sleepRepository: SleepRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<SleepOverviewError, SleepOverviewUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_overview_sleeps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val addButtonContentDescription: StringResource
        get() = Res.string.sleep_overview_content_description_add_sleep

    private val targetSleeps: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetSleepDuration?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
    private val sleepsDataFlow: StateFlow<List<SleepOverview>> =
        combine(
            dateRange,
            settingsRepository.getSettingsFlow()
        ) { range, settings ->
            settings.selectedProfileId?.let { profileId ->
                Pair(settings, range.dateUnit) to
                    sleepRepository.getSleepsBetween(
                        range.startDate,
                        range.endDate,
                        profileId
                    )
            } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
        }.flatMapLatest { (settingsDateUnit, sleepsFlow) ->
            sleepsFlow.map { sleeps ->
                when (settingsDateUnit.second) {
                    DateUnit.DAY -> mapDay(sleeps)
                    DateUnit.WEEK -> mapWeek(sleeps)
                    DateUnit.MONTH -> mapMonth(sleeps)
                    DateUnit.YEAR -> mapYear(sleeps)
                }
            }
        }.onStart {
            isLoading.value = true
            errorMessage.value = null
        }.catch {
            isLoading.value = false
            errorMessage.value = Res.string.sleep_overview_error_loading
            emit(emptyList())
        }.map {
            isLoading.value = false
            it
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val maxSleeps: StateFlow<Int> = combine(
        sleepsDataFlow,
        targetSleeps
    ) { sleeps, targetSleeps ->
        if (sleeps.isEmpty()) {
            max(ProfileDefaults.SLEEP_DURATION.toInt(), targetSleeps ?: ProfileDefaults.SLEEP_DURATION.toInt())
        } else {
            max(
                sleeps.maxOfOrNull { it.totalMinutes }?.roundToInt() ?: ProfileDefaults.SLEEP_DURATION.toInt(),
                targetSleeps ?: ProfileDefaults.SLEEP_DURATION.toInt()
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.SLEEP_DURATION.toInt())

    override val uiState: StateFlow<SleepOverviewUiState> =
        combine(
            sleepsDataFlow,
            maxSleeps,
            targetSleeps,
            isLoading,
            errorMessage
        ) { sleeps, maxSleeps, target, isLoading, errorMessage ->
            SleepOverviewUiState(
                isLoading = isLoading,
                sleeps = sleeps,
                maxSleeps = maxSleeps,
                target = target,
                dates = sleeps.map { it.date },
                error = SleepOverviewError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SleepOverviewUiState(isLoading = true)
        )

    private fun mapDay(sleeps: List<Sleep>): List<SleepOverview> = aggregateDailySleeps(sleeps)

    private fun aggregateDailySleeps(sleeps: List<Sleep>): List<SleepOverview> {
        if (sleeps.isEmpty()) {
            return emptyList()
        }

        return sleeps
            .groupBy { it.start.toLocalDateTime(TimeZone.currentSystemDefault()).date }
            .map { (date, group) ->
                SleepOverview(
                    date = date,
                    totalMinutes = group.sumOf { it.totalMinutes }.toDouble()
                )
            }
    }

    private fun <K> aggregateSleepsByPeriod(
        sleeps: List<SleepOverview>,
        groupKeySelector: (SleepOverview) -> K,
        representativeDateSelector: (List<SleepOverview>) -> LocalDate,
        daysInPeriodSelector: (List<SleepOverview>) -> Int
    ): List<SleepOverview> {
        if (sleeps.isEmpty()) return emptyList()

        return sleeps
            .groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val sumSleeps = group.sumOf { it.totalMinutes }
                val daysInPeriod = daysInPeriodSelector(group)
                if (daysInPeriod == 0) return@mapNotNull null

                val avgSleepingMinutes = (sumSleeps / daysInPeriod).roundToDecimals(2)

                SleepOverview(
                    date = representativeDateSelector(group),
                    totalMinutes = avgSleepingMinutes
                )
            }
    }

    private fun mapWeek(sleeps: List<Sleep>): List<SleepOverview> {
        val dailySleeps = aggregateDailySleeps(sleeps)
        return aggregateSleepsByPeriod(
            sleeps = dailySleeps,
            groupKeySelector = { it.date.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().date.firstDayOfIsoWeek() },
            daysInPeriodSelector = { _ -> 7 }
        )
    }

    private fun mapMonth(sleeps: List<Sleep>): List<SleepOverview> {
        val dailySleeps = aggregateDailySleeps(sleeps)
        return aggregateSleepsByPeriod(
            sleeps = dailySleeps,
            groupKeySelector = { it.date.year to it.date.month },
            representativeDateSelector = { group -> group.first().date.firstDayOfMonth() },
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

    private fun mapYear(sleeps: List<Sleep>): List<SleepOverview> {
        val dailySleeps = aggregateDailySleeps(sleeps)
        return aggregateSleepsByPeriod(
            sleeps = dailySleeps,
            groupKeySelector = { it.date.year },
            representativeDateSelector = { group -> group.first().date.firstDayOfYear() },
            daysInPeriodSelector = { group ->
                val year = group.first().date.year
                val isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
                if (isLeap) 366 else 365
            }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(
        startDateTime: Instant,
        endDateTime: Instant
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.sleep_overview_error_saving
                    return@launch
                }

                sleepRepository.addSleep(
                    Sleep(
                        id = Uuid.random(),
                        profileId = profileId,
                        start = startDateTime,
                        end = endDateTime
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.sleep_overview_error_saving
            }
        }
    }
}