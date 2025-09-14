package org.darthacheron.fitbe.workouts.equipment

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_training_equipment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class TrainingEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository,
    private val settingsRepository: SettingsRepository, // Added SettingsRepository
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_training_equipment

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    val allEquipment: StateFlow<List<TrainingEquipment>> =
        equipmentRepository.getAllEquipments()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val currentProfileId: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoriteEquipmentIds: StateFlow<List<Uuid>> = currentProfileId
        .flatMapLatest { profileId ->
            if (profileId != null) {
                equipmentRepository.getFavoriteEquipmentIds(profileId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateToTrainingEquipmentDetail(id: Uuid?) {
        navHostController.navigate(Screen.TrainingEquipmentDetail(id?.toString()))
    }

    fun addFavorite(equipmentId: Uuid) {
        viewModelScope.launch {
            currentProfileId.value?.let {
                equipmentRepository.addFavorite(it, equipmentId)
            }
        }
    }

    fun removeFavorite(equipmentId: Uuid) {
        viewModelScope.launch {
            currentProfileId.value?.let {
                equipmentRepository.removeFavorite(it, equipmentId)
            }
        }
    }
}
