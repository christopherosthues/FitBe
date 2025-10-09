package org.darthacheron.fitbe.workouts.equipment

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_training_equipment
import fitbe.composeapp.generated.resources.training_equipment_error_favorites
import fitbe.composeapp.generated.resources.training_equipment_error_loading
import fitbe.composeapp.generated.resources.training_equipment_error_toggle_favorite
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
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class TrainingEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository,
    settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FilterableViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_training_equipment

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _isLoadingEquipment = MutableStateFlow(true)
    private val _isLoadingFavorites = MutableStateFlow(true)
    private val _equipmentListErrorMessage = MutableStateFlow<StringResource?>(null)
    private val _favoriteStateErrorMessage = MutableStateFlow<StringResource?>(null)

    private val currentProfileIdFlow: StateFlow<Uuid?> =
        settingsRepository
            .getSettingsFlow()
            .map { it.selectedProfileId }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val internalEquipmentListFlow: StateFlow<List<TrainingEquipment>> =
        equipmentRepository.getAllEquipments()
            .onStart {
                _isLoadingEquipment.value = true
                _equipmentListErrorMessage.value = null
            }
            .map { equipment ->
                _isLoadingEquipment.value = false
                equipment
            }
            .catch { e ->
                _isLoadingEquipment.value = false
                _equipmentListErrorMessage.value = Res.string.training_equipment_error_loading
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
                equipmentRepository.getFavoriteEquipmentIds(profileId)
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
                        _favoriteStateErrorMessage.value =
                            Res.string.training_equipment_error_favorites
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

    private val combinedFiveFlows = combine(
        internalEquipmentListFlow,
        internalFavoritesFlow,
        _isLoadingEquipment,
        _isLoadingFavorites,
        _equipmentListErrorMessage
    ) { equipment: List<TrainingEquipment>,
        favorites: Set<Uuid>,
        isLoadingEquip: Boolean,
        isLoadingFav: Boolean,
        equipError: StringResource? ->
        Pair(
            Pair(equipment, favorites),
            Triple(isLoadingEquip, isLoadingFav, equipError)
        )
    }

    val uiState: StateFlow<TrainingEquipmentScreenUiState> = combinedFiveFlows.combine(
        _favoriteStateErrorMessage
    ) { intermediateData: Pair<Pair<List<TrainingEquipment>, Set<Uuid>>, Triple<Boolean, Boolean, StringResource?>>,
        favError: StringResource? ->
        val (pair1, triple1) = intermediateData
        val (equipment, favorites) = pair1
        val (isLoadingEquip, isLoadingFav, equipError) = triple1

        TrainingEquipmentScreenUiState(
            isLoading = isLoadingEquip || isLoadingFav,
            rawEquipmentList = equipment,
            favoriteEquipmentIds = favorites,
            equipmentListError = equipError,
            favoriteStateError = favError
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TrainingEquipmentScreenUiState(isLoading = true)
    )


    fun navigateToTrainingEquipmentDetail(id: Uuid?) {
        navHostController.navigate(Screen.TrainingEquipmentDetail(id?.toString()))
    }

    fun addFavorite(equipmentId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    equipmentRepository.addFavorite(profileId, equipmentId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value =
                        Res.string.training_equipment_error_toggle_favorite
                }
            }
        }
    }

    fun removeFavorite(equipmentId: Uuid) {
        viewModelScope.launch {
            val profileId = currentProfileIdFlow.value
            if (profileId != null) {
                _favoriteStateErrorMessage.value = null
                try {
                    equipmentRepository.removeFavorite(profileId, equipmentId)
                } catch (e: Exception) {
                    _favoriteStateErrorMessage.value =
                        Res.string.training_equipment_error_toggle_favorite
                }
            }
        }
    }

    fun clearErrorMessage() {
        _equipmentListErrorMessage.value = null
        _favoriteStateErrorMessage.value = null
    }
}