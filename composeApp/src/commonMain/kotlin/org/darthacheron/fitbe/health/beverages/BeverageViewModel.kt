package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_beverages
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class BeverageViewModel(
    private val repository: BeverageRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_beverages

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    val targetBeverage: StateFlow<UInt> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetBeverageInMilliliter ?: ProfileDefaults.BEVERAGE }
            } else {
                flowOf(ProfileDefaults.BEVERAGE)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    val todayIntake: Flow<List<Beverage>> = settingsRepository.getSettingsFlow().flatMapLatest {
        repository.getTodayBeverages(it.selectedProfileId!!)
    }

    val todayProgress: StateFlow<Double> = combine(todayIntake, targetBeverage) {
        todayIntake, targetBeverage ->
        todayIntake.sumOf { it.unit.toMilliliter(it.amount) }.toDouble() / targetBeverage.toDouble()
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    @OptIn(ExperimentalUuidApi::class)
    fun addBeverage(amount: Double, name: String, unit: FluidUnit, date: Instant) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            repository.addBeverage(
                Beverage(
                    amount = amount,
                    beverage = name,
                    unit = unit,
                    dateUtc = date,
                    profileId = settings.selectedProfileId!!
                )
            )
        }
    }
}