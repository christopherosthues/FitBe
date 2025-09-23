package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_overview_error_loading
import fitbe.composeapp.generated.resources.top_bar_title_beverages_overview
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
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class BeverageOverviewViewModel(
    private val beverageRepository: BeverageRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<BeverageOverview>(settingsRepository, profileRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_beverages_overview

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<StringResource?>(null)

    val targetBeverages: StateFlow<UInt> = settingsRepository.getSettingsFlow()
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

    private val beveragesFlow: StateFlow<List<BeverageOverview>> = combine(
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings.selectedProfileId?.let { profileId ->
            Pair(settings, range.dateUnit) to beverageRepository.getBeveragesOverview(
                range.startDate,
                range.endDate,
                profileId
            )
        } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
    }.flatMapLatest { (settingsDateUnit, beveragesSource) ->
        beveragesSource.map { beverages ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(beverages)
                DateUnit.WEEK -> mapWeek(beverages)
                DateUnit.MONTH -> mapMonth(beverages)
                DateUnit.YEAR -> mapYear(beverages)
            }
        }
    }
    .onStart {
        _isLoading.value = true
        _errorMessage.value = null
    }
    .catch {
        _isLoading.value = false
        _errorMessage.value = Res.string.beverages_overview_error_loading
        emit(emptyList())
    }
    .map { beverages ->
        _isLoading.value = false
        beverages
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<BeverageOverviewUiState> = combine(
        beveragesFlow,
        _isLoading,
        _errorMessage
    ) { beverages, isLoading, errorMessage ->
        BeverageOverviewUiState(
            isLoading = isLoading,
            beverages = beverages,
            dates = beverages.map { it.dateUtc },
            errorMessage = errorMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BeverageOverviewUiState(isLoading = true)
    )

    val maxBeverages: StateFlow<UInt> = uiState
        .map { it.beverages }
        .map { beverages ->
            if (beverages.isEmpty()) ProfileDefaults.BEVERAGE else beverages.maxOfOrNull { it.amount } ?: ProfileDefaults.BEVERAGE
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private fun mapDay(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return beverages
    }

    private fun <K> aggregateStepsByPeriod(
        beverages: List<BeverageOverview>,
        groupKeySelector: (BeverageOverview) -> K,
        representativeDateSelector: (List<BeverageOverview>) -> LocalDate
    ): List<BeverageOverview> {
        if (beverages.isEmpty()) return emptyList()

        return beverages.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgAmount = group.sumOf { it.amount.toDouble() } / groupSize

                BeverageOverview(
                    dateUtc = representativeDateSelector(group),
                    amount = avgAmount.roundToInt().toUInt(),
                )
            }
    }

    private fun mapWeek(
        beverages: List<BeverageOverview>,
    ): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
