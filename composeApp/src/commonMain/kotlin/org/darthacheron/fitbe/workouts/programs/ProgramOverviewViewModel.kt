package org.darthacheron.fitbe.workouts.programs


import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.program_overview_error_favorites
import fitbe.composeapp.generated.resources.program_overview_error_loading_programs
import fitbe.composeapp.generated.resources.program_overview_error_toggle_favorite
import fitbe.composeapp.generated.resources.top_bar_title_program_overview
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
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FilterableViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.workouts.templates.WorkoutTemplatesUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ProgramsUiState(
    val isLoading: Boolean = true,
    val rawProgramList: List<Program> = emptyList(),
    val favoriteProgramIds: Set<Uuid> = emptySet(),
    val programListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ProgramOverviewViewModel(
    private val programRepository: ProgramRepository,
    private val settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FilterableViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_program_overview
    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard
    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _isLoadingPrograms = MutableStateFlow(true)
    private val _isLoadingFavorites = MutableStateFlow(true)
    private val _programListErrorMessage = MutableStateFlow<StringResource?>(null)
    private val _favoriteStateErrorMessage = MutableStateFlow<StringResource?>(null)

    private val currentProfileIdFlow: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val internalProgramsFlow: StateFlow<List<Program>> =
        programRepository.getAllProgramsWithWorkouts()
            .onStart {
                _isLoadingPrograms.value = true
                _programListErrorMessage.value = null
            }
            .map { templates ->
                _isLoadingPrograms.value = false
                templates
            }
            .catch { e ->
                _isLoadingPrograms.value = false
                _programListErrorMessage.value = Res.string.program_overview_error_loading_programs
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val internalFavoritesFlow: StateFlow<Set<Uuid>> = currentProfileIdFlow
        .flatMapLatest { profileId ->
            if (profileId != null) {
                programRepository.getFavoriteProgramIds(profileId)
                    .map { it.toSet() }
                    .onStart {
                        _isLoadingFavorites.value = true
                        _favoriteStateErrorMessage.value = null
                    }
                    .map { favIds ->
                        _isLoadingFavorites.value = false
                        favIds
                    }
                    .catch { e ->
                        _isLoadingFavorites.value = false
                        _favoriteStateErrorMessage.value = Res.string.program_overview_error_favorites
                        emit(emptySet())
                    }
            } else {
                _isLoadingFavorites.value = false
                flowOf(emptySet())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _errorMessagesFlow = combine(
        _programListErrorMessage,
        _favoriteStateErrorMessage
    ) { listError, favError ->
        Pair(listError, favError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Pair<StringResource?, StringResource?>(null, null)
    )

    val uiState: StateFlow<ProgramsUiState> = combine(
        internalProgramsFlow,
        internalFavoritesFlow,
        _isLoadingPrograms,
        _isLoadingFavorites,
        _errorMessagesFlow
    ) { programs, favorites, isLoadingPrograms, isLoadingFavs, errorMessages ->
        val (listError, favError) = errorMessages
        ProgramsUiState(
            isLoading = isLoadingPrograms || isLoadingFavs,
            rawProgramList = programs,
            favoriteProgramIds = favorites,
            programListError = listError,
            favoriteStateError = favError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgramsUiState(isLoading = true)
    )

    fun navigateToProgramDetail(programId: Uuid?) {
        navHostController.navigate(Screen.ProgramDetail(programId.toString()))
    }

    fun addFavorite(programId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    programRepository.addFavorite(profileId, programId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value = Res.string.program_overview_error_toggle_favorite
                }
            }
        }
    }

    fun removeFavorite(programId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    programRepository.removeFavorite(profileId, programId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value = Res.string.program_overview_error_toggle_favorite
                }
            }
        }
    }

    fun clearErrorMessage() {
        _programListErrorMessage.value = null
        _favoriteStateErrorMessage.value = null
    }
}