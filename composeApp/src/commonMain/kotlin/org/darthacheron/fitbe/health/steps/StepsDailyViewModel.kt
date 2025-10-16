package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_daily_view_content_description_add_steps
import fitbe.composeapp.generated.resources.steps_daily_view_error_loading
import fitbe.composeapp.generated.resources.steps_daily_view_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_daily_steps
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
import org.darthacheron.fitbe.health.components.DailyViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsDailyViewModel(
    private val stepsRepository: StepsRepository,
    private val profileRepository: ProfileRepository,
    settingsRepository: SettingsRepository,
    topBarManager: TopBarManager
) : DailyViewModel<StepsDailyError, StepsDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_daily_steps

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val addButtonContentDescription: StringResource
        get() = Res.string.steps_daily_view_content_description_add_steps

    val targetSteps: StateFlow<Int> =
        settingsRepository
            .getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    profileRepository
                        .getProfileFlowById(profileId)
                        .map { profile -> profile?.targetSteps?.toInt() ?: ProfileDefaults.STEPS.toInt()}
                } else {
                    flowOf(ProfileDefaults.STEPS.toInt())
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS.toInt())

    private val stepsDataFlow: StateFlow<List<Steps>> =
        date
            .flatMapLatest { date ->
                settingsRepository.getSettingsFlow().flatMapLatest { settings ->
                    settings.selectedProfileId?.let {
                        stepsRepository.getSteps(date.toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
                    } ?: flowOf(emptyList())
                }
            }.onStart {
                isLoading.value = true
                errorMessage.value = null
            }.catch {
                isLoading.value = false
                errorMessage.value = Res.string.steps_daily_view_error_loading
                emit(emptyList())
            }.map { beverages ->
                isLoading.value = false
                beverages
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val uiState: StateFlow<StepsDailyUiState> =
        combine(
            stepsDataFlow,
            targetSteps,
            isLoading,
            errorMessage
        ) { steps, target, isLoading, errorMessage ->
            StepsDailyUiState(
                isLoading = isLoading,
                steps = steps,
                times = steps.map { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).time },
                target = target,
                error = StepsDailyError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StepsDailyUiState(isLoading = true)
        )

    val maxSteps: StateFlow<UInt> =
        uiState
            .map { it.steps }
            .map { stepsList ->
                if (stepsList.isEmpty()) {
                    ProfileDefaults.STEPS
                } else {
                    stepsList.maxOfOrNull { it.steps } ?: ProfileDefaults.STEPS
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    fun addSteps(
        date: Instant,
        steps: UInt
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.steps_daily_view_error_saving
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
                errorMessage.value = Res.string.steps_daily_view_error_saving
            }
        }
    }
}