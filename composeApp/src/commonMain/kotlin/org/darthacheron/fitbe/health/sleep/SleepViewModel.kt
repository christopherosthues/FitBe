package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_sleeps
import fitbe.composeapp.generated.resources.weight_overview_error_loading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.health.weight.WeightOverviewUiState
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.StringResource
import kotlin.collections.map
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class SleepViewModel(
    private val sleepRepository: SleepRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<Sleep>(settingsRepository, profileRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_sleeps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<StringResource?>(null)

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
//    val sleeps: StateFlow<List<Point<LocalDate, Double>>> =
    private val sleepsDataFlow: StateFlow<List<Sleep>> =
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
                    DateUnit.DAY -> mapDay(sleeps, settingsDateUnit.first)
                    DateUnit.WEEK -> mapWeek(sleeps, settingsDateUnit.first)
                    DateUnit.MONTH -> mapMonth(sleeps, settingsDateUnit.first)
                    DateUnit.YEAR -> mapYear(sleeps, settingsDateUnit.first)
                }
            }
        }
            .onStart {
                _isLoading.value = true
                _errorMessage.value = null
            }
            .catch {
                _isLoading.value = false
                _errorMessage.value = Res.string.weight_overview_error_loading
                emit(emptyList())
            }
            .map {
                _isLoading.value = false
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

    val uiState: StateFlow<SleepOverviewUiState> = combine(
        sleepsDataFlow,
        _isLoading,
        _errorMessage
    ) { sleeps, isLoading, errorMessage ->
        SleepOverviewUiState(
            isLoading = isLoading,
            sleeps = sleeps,
            dates = sleeps.map { it.dateUtc },
            errorMessage = errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SleepOverviewUiState(isLoading = true)
    )

    private fun mapDay(sleeps: List<Sleep>, settings: Settings): List<Sleep> {
        return sleeps
    }

    private fun <K> aggregateSleepsByPeriod(
        sleeps: List<Sleep>,
        settings: Settings,
        groupKeySelector: (Sleep) -> K,
        representativeDateSelector: (List<Sleep>) -> LocalDate
    ): List<Sleep> {
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

    private fun mapWeek(sleeps: List<Sleep>, settings: Settings): List<Sleep> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            settings = settings,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(sleeps: List<Sleep>, settings: Settings): List<Sleep> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            settings = settings,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(sleeps: List<Sleep>, settings: Settings): List<Sleep> {
        return aggregateSleepsByPeriod(
            sleeps = sleeps,
            settings = settings,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(hours: UInt, minutes: UInt, date: LocalDate) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            sleepRepository.addSleep(
                Sleep(
                    id = Uuid.random(),
                    profileId = settings.selectedProfileId!!,
                    hours = hours,
                    minutes = minutes,
                    dateUtc = date
                )
            )
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}