package org.darthacheron.fitbe.workouts.equipment

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_training_equipment
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentViewModel(
    private val equipmentRepository: EquipmentRepository,
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
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = emptyList()
            )

    fun navigateToTrainingEquipmentDetail(id: String?) {
        navHostController.navigate(Screen.TrainingEquipmentDetail(id))
    }
}
