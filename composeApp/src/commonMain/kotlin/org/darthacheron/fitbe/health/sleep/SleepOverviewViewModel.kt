//package org.darthacheron.fitbe.health.sleep
//
//import androidx.lifecycle.viewModelScope
//import fitbe.composeapp.generated.resources.Res
//import fitbe.composeapp.generated.resources.top_bar_title_sleeps
//import fitbe.composeapp.generated.resources.weight_overview_error_loading
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.LocalDateTime
//import org.darthacheron.fitbe.components.date.DateUnit
//import org.darthacheron.fitbe.health.OverviewViewModel
//import org.darthacheron.fitbe.navigation.Screen
//import org.darthacheron.fitbe.profile.ProfileDefaults
//import org.darthacheron.fitbe.profile.ProfileRepository
//import org.darthacheron.fitbe.settings.Settings
//import org.darthacheron.fitbe.settings.SettingsRepository
//import org.darthacheron.fitbe.ui.TopBarManager
//import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
//import org.darthacheron.fitbe.utils.firstDayOfMonth
//import org.darthacheron.fitbe.utils.firstDayOfYear
//import org.darthacheron.fitbe.utils.isoWeekAndYear
//import org.jetbrains.compose.resources.StringResource
//import kotlin.collections.map
//import kotlin.collections.maxOfOrNull
//import kotlin.let
//import kotlin.time.ExperimentalTime
//import kotlin.uuid.ExperimentalUuidApi
//import kotlin.uuid.Uuid
//
//@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
//class SleepOverviewViewModel(
//    private val sleepRepository: SleepRepository,
//    settingsRepository: SettingsRepository,
//    profileRepository: ProfileRepository,
//    topBarManager: TopBarManager
//) : OverviewViewModel<SleepOverviewError, SleepOverviewUiState>(settingsRepository, topBarManager) {
//    override val title: StringResource
//        get() = Res.string.top_bar_title_sleeps
//
//    override val backNavigationIconVisible: Boolean?
//        get() = true
//
//    override val bottomBarSelected: Screen?
//        get() = Screen.Health
//
//    val targetSleeps: StateFlow<UInt?> = settingsRepository.getSettingsFlow()
//        .flatMapLatest { settings ->
//            val profileId = settings.selectedProfileId
//            if (profileId != null) {
//                profileRepository.getProfileFlowById(profileId)
//                    .map { profile -> profile?.targetSleepDuration }
//            } else {
//                flowOf(null)
//            }
//        }
//        .stateIn(viewModelScope, SharingStarted.Lazily, null)
//
//    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
//    private val sleepsDataFlow: StateFlow<List<Sleep>> =
//        combine(dateRange, settingsRepository.getSettingsFlow()) { range, settings ->
//            settings.selectedProfileId?.let {
//                Pair(settings, range.dateUnit) to sleepRepository.getSleepsBetween(
//                    range.startDate,
//                    range.endDate,
//                    it
//                )
//            } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
//        }.flatMapLatest { (settingsDateUnit, sleepsFlow) ->
//            sleepsFlow.map { sleeps ->
//                when (settingsDateUnit.second) {
//                    DateUnit.DAY -> mapDay(sleeps)
//                    DateUnit.WEEK -> mapWeek(sleeps)
//                    DateUnit.MONTH -> mapMonth(sleeps)
//                    DateUnit.YEAR -> mapYear(sleeps)
//                }
//            }
//        }
//            .onStart {
//                _isLoading.value = true
//                _errorMessage.value = null
//            }
//            .catch {
//                _isLoading.value = false
//                _errorMessage.value = Res.string.weight_overview_error_loading
//                emit(emptyList())
//            }
//            .map {
//                _isLoading.value = false
//                it
//            }
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
////        }.flatMapLatest { it }
////            .map { s ->
////                s.map { value ->
////                    Point(
////                        value.dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).date,
////                        (value.hours.toDouble() + value.minutes.toDouble() / 60).roundToDecimals(2)
////                    )
////                }
////            }
//
//    override val uiState: StateFlow<SleepOverviewUiState> = combine(
//        sleepsDataFlow,
//        _isLoading,
//        _errorMessage
//    ) { sleeps, isLoading, errorMessage ->
//        SleepOverviewUiState(
//            isLoading = isLoading,
//            sleeps = sleeps,
//            dates = sleeps.map { it.dateUtc },
//            error = SleepOverviewError(errorMessage)
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = SleepOverviewUiState(isLoading = true)
//    )
//
//    val maxSleeps: StateFlow<UInt> = uiState.map { it.sleeps }
//        .map { sleeps ->
//            sleeps.maxOfOrNull { it.hours * 60u + it.minutes } ?: ProfileDefaults.SLEEP_DURATION
//        }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.SLEEP_DURATION)
//
//    private fun mapDay(sleeps: List<Sleep>): List<Sleep> {
//        return sleeps
//    }
//
//    private fun <K> aggregateSleepsByPeriod(
//        sleeps: List<Sleep>,
//        groupKeySelector: (Sleep) -> K,
//        representativeDateSelector: (List<Sleep>) -> LocalDate
//    ): List<Sleep> {
//        if (sleeps.isEmpty()) return emptyList()
//
//        return sleeps.groupBy(groupKeySelector)
//            .mapNotNull { (_, group) ->
//                if (group.isEmpty()) return@mapNotNull null
//
//                val groupSize = group.size
//                val avgSleepingMinutes = group.sumOf { it.hours.toInt() * 60 + it.minutes.toInt() } / groupSize
//
//                Sleep(
//                    id = Uuid.random(),
//                    profileId = group.first().profileId,
//                    dateUtc = representativeDateSelector(group),
//                    hours = avgSleepingMinutes.toUInt() / 60u,
//                    minutes = avgSleepingMinutes.toUInt() % 60u
//                )
//            }
//    }
//
//    private fun mapWeek(sleeps: List<Sleep>): List<Sleep> {
//        return aggregateSleepsByPeriod(
//            sleeps = sleeps,
//            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
//            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
//        )
//    }
//
//    private fun mapMonth(sleeps: List<Sleep>): List<Sleep> {
//        return aggregateSleepsByPeriod(
//            sleeps = sleeps,
//            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
//            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
//        )
//    }
//
//    private fun mapYear(sleeps: List<Sleep>): List<Sleep> {
//        return aggregateSleepsByPeriod(
//            sleeps = sleeps,
//            groupKeySelector = { it.dateUtc.year },
//            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
//        )
//    }
//
//    @OptIn(ExperimentalUuidApi::class)
//    fun addSleep(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
//        viewModelScope.launch {
//            val settings = settingsRepository.getSettings()
//
//            // TODO: convert from system current to UTC
//            // TODO: if start and end overlap a day -> split into two (or more) sleeps. Each sleep should only be for one day e.g. 9.September - 10.September
//            // there can be more than one sleeps per day e.g. night sleep and day sleep
//            val date = startDateTime.date
//
//            sleepRepository.addSleep(
//                Sleep(
//                    id = Uuid.random(),
//                    profileId = settings.selectedProfileId!!,
//                    startTime = startDateTime,
//                    endTime = endDateTime,
//                    dateUtc = date
//                )
//            )
//        }
//    }
//}