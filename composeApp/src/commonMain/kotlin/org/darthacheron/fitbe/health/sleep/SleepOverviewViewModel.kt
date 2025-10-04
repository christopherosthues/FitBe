package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_sleeps
import fitbe.composeapp.generated.resources.weight_overview_error_loading
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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
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
        get() = Res.string.top_bar_title_sleeps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    val targetSleeps: StateFlow<UInt?> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetSleepDuration }
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
    private val sleepsDataFlow: StateFlow<List<SleepOverview>> =
        combine(dateRange, settingsRepository.getSettingsFlow()) { range, settings ->
            settings.selectedProfileId?.let {
                Pair(settings, range.dateUnit) to sleepRepository.getSleepsBetween(
                    range.startDate,
                    range.endDate,
                    it
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
        }
            .onStart {
                isLoading.value = true
                errorMessage.value = null
            }
            .catch {
                isLoading.value = false
                errorMessage.value = Res.string.weight_overview_error_loading
                emit(emptyList())
            }
            .map {
                isLoading.value = false
                it
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
//        }.flatMapLatest { it }
//            .map { s ->
//                s.map { value ->
//                    Point(
//                        value.dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).date,
//                        (value.hours.toDouble() + value.minutes.toDouble() / 60).roundToDecimals(2)
//                    )
//                }
//            }

    override val uiState: StateFlow<SleepOverviewUiState> = combine(
        sleepsDataFlow,
        isLoading,
        errorMessage
    ) { sleeps, isLoading, errorMessage ->
        SleepOverviewUiState(
            isLoading = isLoading,
            sleeps = sleeps,
            dates = sleeps.map { it.dateUtc },
            error = SleepOverviewError(errorMessage)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SleepOverviewUiState(isLoading = true)
    )

    val maxSleeps: StateFlow<UInt> = uiState.map { it.sleeps }
        .map { sleeps ->
            sleeps.maxOfOrNull { it.hours * 60u + it.minutes } ?: ProfileDefaults.SLEEP_DURATION
        }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.SLEEP_DURATION)

    private fun mapDay(sleeps: List<Sleep>): List<SleepOverview> {
        return sleeps
    }

    private fun <K> aggregateSleepsByPeriod(
        sleeps: List<Sleep>,
        groupKeySelector: (Sleep) -> K,
        representativeDateSelector: (List<Sleep>) -> LocalDate
    ): List<SleepOverview> {
        if (sleeps.isEmpty()) return emptyList()

        return sleeps.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgSleepingMinutes = group.sumOf { it.hours.toInt() * 60 + it.minutes.toInt() } / groupSize

                Sleep(
                    id = Uuid.random(),
                    profileId = group.first().profileId,
                    dateUtc = representativeDateSelector(group),
                    hours = avgSleepingMinutes.toUInt() / 60u,
                    minutes = avgSleepingMinutes.toUInt() % 60u
                )
            }
    }

    private fun mapWeek(sleeps: List<Sleep>): List<SleepOverview> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(sleeps: List<Sleep>): List<SleepOverview> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(sleeps: List<Sleep>): List<SleepOverview> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(startDateTime: Instant, endDateTime: Instant) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            sleepRepository.addSleep(
                Sleep(
                    id = Uuid.random(),
                    profileId = settings.selectedProfileId!!,
                    start = startDateTime,
                    end = endDateTime,
                )
            )
        }
    }
}