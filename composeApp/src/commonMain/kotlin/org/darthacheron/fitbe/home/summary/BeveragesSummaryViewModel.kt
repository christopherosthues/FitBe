package org.darthacheron.fitbe.home.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_error_loading
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class BeveragesSummaryViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val beverageRepository: BeverageRepository,
) : ViewModel() {
    private val isLoading = MutableStateFlow(true)
    private val errorMessage = MutableStateFlow<StringResource?>(null)

    private val targetBeverage: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetBeverageInMilliliter?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val beveragesFlow =
        settingsRepository.getSettingsFlow().flatMapLatest { settings ->
            settings.selectedProfileId?.let {
                beverageRepository.getBeverages(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
            } ?: flowOf(emptyList())
        }.onStart {
            isLoading.value = true
            errorMessage.value = null
        }.catch {
            isLoading.value = false
            errorMessage.value = Res.string.profile_error_loading
            emit(emptyList())
        }.map { beverages ->
            isLoading.value = false
            beverages
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val totalBeverage: StateFlow<Double> = beveragesFlow.map { beverages ->
        beverages.sumOf { it.unit.toMilliliter(it.amount) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val uiState: StateFlow<BeveragesSummaryUiState> =
        combine(
            targetBeverage,
            totalBeverage,
            isLoading,
            errorMessage
        ) { targetBeverage, totalBeverage, isLoading, errorMessage ->
            BeveragesSummaryUiState(
                isLoading = isLoading,
                error = BeveragesSummaryError(errorMessage),
                target = targetBeverage,
                total = totalBeverage,
                progress = if (targetBeverage != null && targetBeverage > 0) {
                    (totalBeverage.toFloat() / targetBeverage).coerceIn(0f, 1f)
                } else {
                    1f
                }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BeveragesSummaryUiState())
}
