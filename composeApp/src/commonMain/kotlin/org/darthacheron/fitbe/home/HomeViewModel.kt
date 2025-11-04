package org.darthacheron.fitbe.home

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_error_loading
import fitbe.composeapp.generated.resources.top_bar_title_home
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.beverages.BeverageRepository
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.BottomNavigationBarViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(
    topNavHostController: NavHostController,
    navHostController: NavHostController,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val beverageRepository: BeverageRepository,

    topBarManager: TopBarManager
) : BottomNavigationBarViewModel(topNavHostController, navHostController, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_home

    override val bottomBarSelected: Screen
        get() = Screen.Home

    val targetBeverage: StateFlow<Int?> =
        profileRepository.getTargetValueFlow { it?.targetBeverageInMilliliter?.toInt() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val beveragesFlow =
            settingsRepository.getSettingsFlow().flatMapLatest { settings ->
                settings.selectedProfileId?.let {
                    beverageRepository.getBeverages(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, it)
                } ?: flowOf(emptyList())
            }.onStart {
//                isLoading.value = true
//                errorMessage.value = null
            }.catch {
//                isLoading.value = false
//                errorMessage.value = Res.string.beverages_daily_view_error_loading
                emit(emptyList())
            }.map { beverages ->
//                isLoading.value = false
                beverages
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalBeverage: StateFlow<Double> = beveragesFlow.map { beverages ->
        beverages.sumOf { it.unit.toMilliliter(it.amount) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}