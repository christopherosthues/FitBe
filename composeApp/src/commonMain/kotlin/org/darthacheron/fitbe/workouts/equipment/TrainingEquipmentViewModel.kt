package org.darthacheron.fitbe.workouts.equipment

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_training_equipment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class TrainingEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository,
    settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_training_equipment

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText.asStateFlow()

    private val allEquipmentFlow: StateFlow<List<TrainingEquipment>> =
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

    @Composable
    private fun toEquipmentWithLocalizedName(equipments: List<TrainingEquipment>): List<TrainingEquipment> {
        return equipments.map {
            TrainingEquipment(
                id = it.id,
                name = getEquipmentName(it.name, it.default),
                imageUri = it.imageUri,
                default = it.default,
                dateUtc = it.dateUtc
            )
        }
    }

    val allEquipment: StateFlow<List<TrainingEquipment>> = combine(
        allEquipmentFlow,
        filterText,
        favoriteEquipmentIds
    ) { equipmentList, filter, favorites ->
        val localizedEquipment = toEquipmentWithLocalizedName(equipmentList)
        val filteredList = if (filter.isBlank()) {
            localizedEquipment
        } else {
            localizedEquipment.filter {
                it.name.contains(filter, ignoreCase = true)
            }
        }
        filteredList.sortedWith(
            compareByDescending<TrainingEquipment> { favorites.contains(it.id) }
                .thenBy { it.name }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onFilterTextChanged(text: String) {
        _filterText.value = text
    }

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
