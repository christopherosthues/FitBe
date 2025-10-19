package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_error_deleting
import fitbe.composeapp.generated.resources.beverages_daily_view_error_editing
import fitbe.composeapp.generated.resources.beverages_daily_view_error_saving
import fitbe.composeapp.generated.resources.steps_daily_view_content_description_add_steps
import fitbe.composeapp.generated.resources.steps_daily_view_error_deleting
import fitbe.composeapp.generated.resources.steps_daily_view_error_editing
import fitbe.composeapp.generated.resources.steps_daily_view_error_loading
import fitbe.composeapp.generated.resources.steps_daily_view_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_daily_view_steps
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
import org.darthacheron.fitbe.health.beverages.Beverage
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.components.DailyViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.math.max
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsDailyViewModel(
    private val stepsRepository: StepsRepository,
    private val profileRepository: ProfileRepository,
    settingsRepository: SettingsRepository,
    topBarManager: TopBarManager
) : DailyViewModel<StepsDailyError, StepsDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_daily_view_steps

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val addButtonContentDescription: StringResource
        get() = Res.string.steps_daily_view_content_description_add_steps

    private val targetSteps: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetSteps?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

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

    private val maxSteps: StateFlow<Int> =
        combine(
            stepsDataFlow,
            targetSteps
        ) { stepsList, targetSteps ->
            if (stepsList.isEmpty()) {
                max(ProfileDefaults.STEPS.toInt(), targetSteps ?: ProfileDefaults.STEPS.toInt())
            } else {
                max((stepsList.maxOfOrNull { it.steps } ?: ProfileDefaults.STEPS).toInt(),
                    targetSteps ?: ProfileDefaults.STEPS.toInt()
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS.toInt())

    override val uiState: StateFlow<StepsDailyUiState> =
        combine(
            stepsDataFlow,
            maxSteps,
            targetSteps,
            isLoading,
            errorMessage
        ) { steps, maxSteps, target, isLoading, errorMessage ->
            StepsDailyUiState(
                isLoading = isLoading,
                steps = steps,
                times = steps.map { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).time },
                target = target,
                maxSteps = maxSteps,
                error = StepsDailyError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StepsDailyUiState(isLoading = true)
        )

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

    fun editSteps(
        id: Uuid,
        date: Instant,
        steps: UInt
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.steps_daily_view_error_editing
                    return@launch
                }

                stepsRepository.editSteps(
                    Steps(
                        id = id,
                        date = date,
                        profileId = profileId,
                        steps = steps
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.steps_daily_view_error_editing
            }
        }
    }

    fun deleteSteps(id: Uuid) {
        viewModelScope.launch {
            try {
                stepsRepository.deleteSteps(id)
            } catch (e: Exception) {
                errorMessage.value = Res.string.steps_daily_view_error_deleting
            }
        }
    }
}