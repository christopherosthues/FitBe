package org.darthacheron.fitbe.home.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class StepsSummaryViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val stepsRepository: StepsRepository,
) : ViewModel() {
    private val targetSteps: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetSteps?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val stepsFlow =
        settingsRepository.getSettingsFlow().flatMapLatest { settings ->
            settings.selectedProfileId?.let {
                stepsRepository.getSteps(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
            } ?: flowOf(emptyList())
        }.onStart {
//                isLoading.value = true
//                errorMessage.value = null
        }.catch {
//                isLoading.value = false
//                errorMessage.value = Res.string.beverages_daily_view_error_loading
            emit(emptyList())
        }.map { steps ->
//                isLoading.value = false
            steps
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val totalSteps: StateFlow<Int> = stepsFlow.map { steps ->
        steps.sumOf { it.steps.toInt() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val uiState: StateFlow<StepsSummaryUiState> =
        combine(
            targetSteps,
            totalSteps,
        ) { targetSteps, totalSteps ->
            StepsSummaryUiState(
                target = targetSteps,
                total = totalSteps,
                progress = if (targetSteps != null && targetSteps > 0) {
                    (totalSteps.toFloat() / targetSteps).coerceIn(0f, 1f)
                } else {
                    1f
                }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepsSummaryUiState())
}
