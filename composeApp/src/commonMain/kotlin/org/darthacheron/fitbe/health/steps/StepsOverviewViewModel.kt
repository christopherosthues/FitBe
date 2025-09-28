package org.darthacheron.fitbe.health.steps

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_overview_error_loading // Added
import fitbe.composeapp.generated.resources.top_bar_title_steps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch // Added
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart // Added
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class StepsOverviewViewModel(
    private val stepsRepository: StepsRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<Steps>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_steps

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<StringResource?>(null)

    val targetSteps: StateFlow<UInt?> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetSteps }
            } else {
                flowOf(ProfileDefaults.STEPS)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    private val stepsDataFlow: StateFlow<List<Steps>> = combine(
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings.selectedProfileId?.let { profileId ->
            Pair(settings, range.dateUnit) to stepsRepository.getSteps(
                range.startDate,
                range.endDate,
                profileId
            )
        } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
    }.flatMapLatest { (settingsDateUnit, stepsSource) ->
        stepsSource.map { steps ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(steps)
                DateUnit.WEEK -> mapWeek(steps)
                DateUnit.MONTH -> mapMonth(steps)
                DateUnit.YEAR -> mapYear(steps)
            }
        }
    }
    .onStart {
        _isLoading.value = true
        _errorMessage.value = null
    }
    .catch {
        _isLoading.value = false
        _errorMessage.value = Res.string.steps_overview_error_loading
        emit(emptyList())
    }
    .map { steps ->
        _isLoading.value = false
        steps
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<StepsOverviewUiState> = combine(
        stepsDataFlow,
        _isLoading,
        _errorMessage
    ) { steps, isLoading, errorMessage ->
        StepsOverviewUiState(
            isLoading = isLoading,
            steps = steps,
            dates = steps.map { it.dateUtc },
            error = StepsOverviewError(errorMessage)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StepsOverviewUiState(isLoading = true)
    )

    val maxSteps: StateFlow<UInt> = uiState
        .map { it.steps }
        .map { stepsList ->
            if (stepsList.isEmpty()) ProfileDefaults.STEPS else stepsList.maxOfOrNull { it.steps } ?: ProfileDefaults.STEPS
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    private fun mapDay(steps: List<Steps>): List<Steps> {
        return steps
    }

    private fun <K> aggregateStepsByPeriod(
        steps: List<Steps>,
        groupKeySelector: (Steps) -> K,
        representativeDateSelector: (List<Steps>) -> LocalDate
    ): List<Steps> {
        if (steps.isEmpty()) return emptyList()

        return steps.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgSteps = group.sumOf { it.steps.toDouble() } / groupSize // Ensure double for division

                Steps(
                    id = Uuid.random(),
                    dateUtc = representativeDateSelector(group),
                    profileId = group.first().profileId,
                    steps = avgSteps.roundToInt().toUInt(),
                )
            }
    }

    private fun mapWeek(
        stepList: List<Steps>,
    ): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepList,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(stepsList: List<Steps>): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepsList,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(stepsList: List<Steps>): List<Steps> {
        return aggregateStepsByPeriod(
            steps = stepsList,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    fun addSteps(dateUtc: LocalDate, steps: UInt) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            settings.selectedProfileId?.let { profileId ->
                stepsRepository.addSteps(
                    Steps(
                        profileId = profileId,
                        dateUtc = dateUtc,
                        steps = steps
                    )
                )
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}