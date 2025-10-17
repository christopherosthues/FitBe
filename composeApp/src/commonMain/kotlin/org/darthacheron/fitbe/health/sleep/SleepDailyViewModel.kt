package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_daily_view_content_description_add_sleep
import fitbe.composeapp.generated.resources.sleep_daily_view_error_loading
import fitbe.composeapp.generated.resources.sleep_daily_view_error_saving
import fitbe.composeapp.generated.resources.sleep_overview_error_loading
import fitbe.composeapp.generated.resources.sleep_overview_error_saving
import fitbe.composeapp.generated.resources.steps_daily_view_error_loading
import fitbe.composeapp.generated.resources.top_bar_title_daily_view_sleeps
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.components.DailyViewModel
import org.darthacheron.fitbe.health.steps.Steps
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class SleepDailyViewModel(
    private val sleepRepository: SleepRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : DailyViewModel<SleepDailyError, SleepDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_daily_view_sleeps

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val addButtonContentDescription: StringResource
        get() = Res.string.sleep_daily_view_content_description_add_sleep

    val targetSleeps: StateFlow<Int> =
        settingsRepository
            .getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    profileRepository
                        .getProfileFlowById(profileId)
                        .map { profile -> profile?.targetSleepDuration?.toInt() ?: ProfileDefaults.SLEEP_DURATION.toInt() }
                } else {
                    flowOf(ProfileDefaults.SLEEP_DURATION.toInt())
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.SLEEP_DURATION.toInt())

    private val sleepsDataFlow: StateFlow<List<Sleep>> =
        date
            .flatMapLatest { date ->
                settingsRepository.getSettingsFlow().flatMapLatest { settings ->
                    settings.selectedProfileId?.let {
                        sleepRepository.getSleeps(date.toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
                    } ?: flowOf(emptyList())
                }
            }.onStart {
                isLoading.value = true
                errorMessage.value = null
            }.catch {
                isLoading.value = false
                errorMessage.value = Res.string.sleep_daily_view_error_loading
                emit(emptyList())
            }.map { beverages ->
                isLoading.value = false
                beverages
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val uiState: StateFlow<SleepDailyUiState> =
        combine(
            sleepsDataFlow,
            targetSleeps,
            isLoading,
            errorMessage
        ) { sleeps, target, isLoading, errorMessage ->
            val duration = sleeps.fold(Duration.ZERO) { acc, it -> acc + it.duration }
            SleepDailyUiState(
                isLoading = isLoading,
                sleeps = sleeps,
//                times = sleeps.map { it. },
                target = target,
                sleepHours = duration.inWholeMinutes.toInt() / 60,
                sleepMinutes = duration.inWholeMinutes.toInt() % 60,
                error = SleepDailyError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SleepDailyUiState(isLoading = true)
        )

    val maxSleeps: StateFlow<Int> =
        uiState
            .map { it.sleeps }
            .map { sleeps ->
                if (sleeps.isEmpty()) {
                    ProfileDefaults.SLEEP_DURATION.toInt()
                } else {
                    sleeps.maxOfOrNull { it.totalMinutes } ?: ProfileDefaults.SLEEP_DURATION.toInt()
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.SLEEP_DURATION.toInt())

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(
        startDateTime: Instant,
        endDateTime: Instant
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.sleep_daily_view_error_saving
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
                errorMessage.value = Res.string.sleep_daily_view_error_saving
            }
        }
    }
}