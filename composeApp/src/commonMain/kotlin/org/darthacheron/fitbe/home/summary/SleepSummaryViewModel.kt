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
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class SleepSummaryViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val sleepRepository: SleepRepository,
) : ViewModel() {
    private val targetSleep: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetSleepDuration?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val sleepsFlow =
        settingsRepository.getSettingsFlow().flatMapLatest { settings ->
            settings.selectedProfileId?.let {
                sleepRepository.getSleeps(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
            } ?: flowOf(emptyList())
        }.onStart {
//                isLoading.value = true
//                errorMessage.value = null
        }.catch {
//                isLoading.value = false
//                errorMessage.value = Res.string.beverages_daily_view_error_loading
            emit(emptyList())
        }.map { sleeps ->
//                isLoading.value = false
            sleeps
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val totalSleep: StateFlow<Int> = sleepsFlow.map { sleeps ->
        sleeps.sumOf { it.totalMinutes }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val uiState: StateFlow<SleepSummaryUiState> =
        combine(
            targetSleep,
            totalSleep,
        ) { targetSleep, totalSleep ->
            val sleepHours = totalSleep / 60
            val sleepMinutes = totalSleep % 60
            SleepSummaryUiState(
                target = targetSleep,
                total = totalSleep,
                progress = if (targetSleep != null && targetSleep > 0) {
                    (totalSleep.toFloat() / targetSleep).coerceIn(0f, 1f)
                } else {
                    1f
                },
                sleepHours = sleepHours,
                sleepMinutes = sleepMinutes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SleepSummaryUiState())
}
