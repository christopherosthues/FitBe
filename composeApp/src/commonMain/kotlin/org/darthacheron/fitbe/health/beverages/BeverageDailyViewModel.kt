package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_error_loading
import fitbe.composeapp.generated.resources.beverages_daily_view_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_beverages
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
import org.darthacheron.fitbe.health.componenets.DailyViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class BeverageDailyViewModel(
    private val repository: BeverageRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : DailyViewModel<BeverageDailyError, BeverageDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_beverages

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    private val targetBeverage: StateFlow<UInt> =
        settingsRepository
            .getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    profileRepository
                        .getProfileFlowById(profileId)
                        .map { profile -> profile?.targetBeverageInMilliliter ?: ProfileDefaults.BEVERAGE }
                } else {
                    flowOf(ProfileDefaults.BEVERAGE)
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private val beveragesFlow =
        date
            .flatMapLatest { date ->
                settingsRepository.getSettingsFlow().flatMapLatest { settings ->
                    settings.selectedProfileId?.let {
                        repository.getBeveragesForDate(date.toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
                    } ?: flowOf(emptyList())
                }
            }.onStart {
                isLoading.value = true
                errorMessage.value = null
            }.catch {
                isLoading.value = false
                errorMessage.value = Res.string.beverages_daily_view_error_loading
                emit(emptyList())
            }.map { beverages ->
                isLoading.value = false
                beverages
            }

    // TODO: translations

    override val uiState: StateFlow<BeverageDailyUiState> =
        combine(
            beveragesFlow,
            targetBeverage,
            isLoading,
            errorMessage
        ) { beverages, target, isLoading, error ->
            val totalAmount = beverages.sumOf { it.unit.toMilliliter(it.amount) }
            val progress =
                if (target > 0u) {
                    totalAmount / target.toDouble()
                } else {
                    0.0
                }
            BeverageDailyUiState(
                isLoading = isLoading,
                beverages = beverages,
                progress = progress,
                total = totalAmount,
                target = target,
                error = BeverageDailyError(error)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BeverageDailyUiState())

    @OptIn(ExperimentalUuidApi::class)
    fun addBeverage(
        amount: Double,
        name: String,
        unit: FluidUnit,
        date: Instant
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.beverages_daily_view_error_saving
                    return@launch
                }
                repository.addBeverage(
                    Beverage(
                        amount = amount,
                        beverage = name,
                        unit = unit,
                        date = date,
                        profileId = profileId
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.beverages_daily_view_error_saving
            }
        }
    }
}