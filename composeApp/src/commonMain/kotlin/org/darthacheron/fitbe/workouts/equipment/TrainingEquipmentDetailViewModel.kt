package org.darthacheron.fitbe.workouts.equipment

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_add_edit_training_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_delete_new_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_loading
import fitbe.composeapp.generated.resources.training_equipment_detail_error_missing_name
import fitbe.composeapp.generated.resources.training_equipment_detail_error_not_found
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_new_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_reset_non_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_error_saving
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_add_favorite
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_remove_favorite
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.exercises_error_favorites // Assuming similar error for equipment
import fitbe.composeapp.generated.resources.exercises_error_toggle_favorite // Assuming similar error for equipment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarAction
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class TrainingEquipmentDetailViewModel(
    private val equipmentRepository: EquipmentRepository,
    settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_add_edit_training_equipment

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    override val backNavigationIconVisible: Boolean?
        get() = true

    private val _uiState = MutableStateFlow(AddEditTrainingEquipmentUiState())
    val uiState: StateFlow<AddEditTrainingEquipmentUiState> = _uiState.asStateFlow()

    private val currentProfileId: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val isFavoriteFlow: StateFlow<Boolean> =
        combine(
            uiState.map { it.equipmentId }.distinctUntilChanged(),
            currentProfileId
        ) { equipmentId, profileId ->
            if (equipmentId != null && profileId != null) {
                equipmentRepository.isFavorite(profileId, equipmentId)
            } else {
                flowOf(false)
            }
        }.flatMapLatest { it }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercises_error_favorites
                        )
                    )
                }
                emit(false)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            isFavoriteFlow.collect { isFav ->
                _uiState.update { currentState ->
                    val newErrorState =
                        if (currentState.error.generalError == Res.string.exercises_error_favorites) {
                            currentState.error.copy(hasGeneralError = false, generalError = null)
                        } else {
                            currentState.error
                        }
                    currentState.copy(isFavorite = isFav, error = newErrorState)
                }
                updateTopBarConfig()
            }
        }
    }

    override val actions: List<TopBarAction>
        get() {
            val currentEquipmentId = _uiState.value.equipmentId
            val currentProfId = currentProfileId.value
            val isCurrentlyFavorite = _uiState.value.isFavorite

            val favoriteAction = TopBarAction(
                icon = if (isCurrentlyFavorite) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                contentDescription = if (isCurrentlyFavorite) Res.string.training_equipment_detail_content_description_remove_favorite else Res.string.training_equipment_detail_content_description_add_favorite,
                onClick = { toggleFavorite() },
                isVisible = currentEquipmentId != null && currentProfId != null
            )
            return listOf(favoriteAction)
        }

    private fun toggleFavorite() {
        val equipmentId = _uiState.value.equipmentId
        val profileId = currentProfileId.value

        if (equipmentId != null && profileId != null) {
            viewModelScope.launch {
                try {
                    if (_uiState.value.isFavorite) {
                        equipmentRepository.removeFavorite(profileId, equipmentId)
                    } else {
                        equipmentRepository.addFavorite(profileId, equipmentId)
                    }
                    // isFavorite state will be updated by isFavoriteFlow collection
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            error = TrainingEquipmentError(
                                hasGeneralError = true,
                                generalError = Res.string.exercises_error_toggle_favorite
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadEquipment(equipmentIdString: String?) {
        if (equipmentIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true,
                    default = false,
                    equipmentId = null,
                    name = "",
                    imageUri = null,
                    error = TrainingEquipmentError(), // Clear errors for new equipment
                    persistedDefaultName = null,
                    persistedDefaultImageUri = null,
                    isModifiedFromPersistedDefault = false,
                    exercises = emptyList(),
                    isFavorite = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                equipmentId = Uuid.parse(equipmentIdString),
                error = TrainingEquipmentError()
            )
        } // Clear errors on load start
        viewModelScope.launch {
            try {
                val parsedEquipmentId = Uuid.parse(equipmentIdString)
                val currentEquipment =
                    equipmentRepository.getEquipmentWithExercisesById(parsedEquipmentId)
                        .firstOrNull()

                if (currentEquipment != null) {
                    val errorState =
                        if (_uiState.value.error.generalError == Res.string.exercises_error_favorites) {
                            _uiState.value.error // Preserve favorites error if it occurred during initial load
                        } else {
                            TrainingEquipmentError() // Clear other errors
                        }
                    if (currentEquipment.default) {
                        val originalDefaultEquipment =
                            equipmentRepository.getDefaultEquipmentById(currentEquipment.id)
                                .firstOrNull()
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = true,
                                isLoading = false,
                                isEditing = false,
                                error = errorState,
                                persistedDefaultName = originalDefaultEquipment?.name,
                                persistedDefaultImageUri = originalDefaultEquipment?.imageUri,
                                isModifiedFromPersistedDefault = (originalDefaultEquipment != null) &&
                                        (currentEquipment.name != originalDefaultEquipment.name || currentEquipment.imageUri != originalDefaultEquipment.imageUri),
                                exercises = currentEquipment.exercises
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                name = currentEquipment.name,
                                imageUri = currentEquipment.imageUri,
                                default = false,
                                isLoading = false,
                                isEditing = false,
                                error = errorState,
                                persistedDefaultName = null,
                                persistedDefaultImageUri = null,
                                isModifiedFromPersistedDefault = false,
                                exercises = currentEquipment.exercises
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = TrainingEquipmentError(
                                hasGeneralError = true,
                                generalError = Res.string.training_equipment_detail_error_not_found
                            ),
                            isEditing = false,
                            persistedDefaultName = null,
                            persistedDefaultImageUri = null,
                            isModifiedFromPersistedDefault = false,
                            exercises = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_loading
                        ),
                        isEditing = false,
                        persistedDefaultName = null,
                        persistedDefaultImageUri = null,
                        isModifiedFromPersistedDefault = false,
                        exercises = emptyList()
                    )
                }
            }
        }
    }

    fun setEditing(isEditing: Boolean) {
        _uiState.update { it.copy(isEditing = isEditing) }
    }

    fun onNameChange(name: String) {
        val nameErrorRes =
            if (name.isBlank()) Res.string.training_equipment_detail_error_missing_name else null
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (name != currentState.persistedDefaultName || currentState.imageUri != currentState.persistedDefaultImageUri)
            } else {
                false
            }
            currentState.copy(
                name = name,
                error = currentState.error.copy(
                    hasNameError = nameErrorRes != null,
                    nameError = nameErrorRes,
                    hasGeneralError = false, // Clear general error when name is changed
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { currentState ->
            val modified =
                if (currentState.default && currentState.persistedDefaultImageUri != null) {
                    (currentState.name != currentState.persistedDefaultName || imageUri != currentState.persistedDefaultImageUri)
                } else if (currentState.default && currentState.persistedDefaultImageUri == null && imageUri != null) {
                    true
                } else {
                    false
                }
            currentState.copy(
                imageUri = imageUri,
                error = currentState.error.copy(
                    hasGeneralError = false,
                    generalError = null
                ), // Clear general error on image change
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun saveEquipment() {
        val currentState = _uiState.value
        val nameIsEmpty = currentState.name.isBlank()

        if (nameIsEmpty) {
            _uiState.update {
                it.copy(
                    error = it.error.copy(
                        hasNameError = true,
                        nameError = Res.string.training_equipment_detail_error_missing_name,
                        hasGeneralError = false, // Ensure general error is clear if only specific field error exists
                        generalError = null
                    )
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                error = TrainingEquipmentError()
            )
        } // Clear errors before saving

        viewModelScope.launch {
            val equipmentToSave = TrainingEquipment(
                id = currentState.equipmentId ?: Uuid.random(),
                name = currentState.name,
                imageUri = currentState.imageUri,
                default = currentState.default,
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            )

            try {
                equipmentRepository.upsertEquipment(equipmentToSave)
                // After successful save, reload to get fresh data and reset isModified/isEditing
                loadEquipment(equipmentToSave.id.toString()) // This will reset error state on success
                _uiState.update { it.copy(isEditing = false) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_saving
                        )
                    )
                }
            }
        }
    }

    fun resetEquipmentToDefault() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.equipmentId == null -> Res.string.training_equipment_detail_error_reset_new_equipment
            !currentState.default -> Res.string.training_equipment_detail_error_reset_non_default_equipment
            !currentState.isModifiedFromPersistedDefault -> null // No error if not modified
            else -> null
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = TrainingEquipmentError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }
        if (!currentState.isModifiedFromPersistedDefault && currentState.default) {
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                error = TrainingEquipmentError()
            )
        }
        viewModelScope.launch {
            try {
                currentState.equipmentId?.let { equipmentId ->
                    equipmentRepository.resetEquipmentToDefault(equipmentId)
                    loadEquipment(equipmentId.toString())
                    _uiState.update { it.copy(isEditing = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_reset_default_equipment
                        )
                    )
                }
            }
        }
    }

    fun deleteEquipment() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.equipmentId == null -> Res.string.training_equipment_detail_error_delete_new_equipment
            currentState.default -> Res.string.training_equipment_detail_error_delete_default_equipment
            else -> null
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = TrainingEquipmentError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                error = TrainingEquipmentError()
            )
        } // Clear errors before delete
        viewModelScope.launch {
            try {
                currentState.equipmentId?.let { equipmentId ->
                    val equipmentToDelete = TrainingEquipment(
                        id = equipmentId,
                        name = currentState.name,
                        imageUri = currentState.imageUri,
                        default = currentState.default,
                        dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    )
                    equipmentRepository.deleteEquipment(equipmentToDelete)
                    navHostController.popBackStack()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = TrainingEquipmentError(
                            hasGeneralError = true,
                            generalError = Res.string.training_equipment_detail_error_delete_equipment
                        )
                    )
                }
            } finally {
                if (currentState.equipmentId != null) _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun navigateToExerciseDetail(exerciseId: Uuid) {
        navHostController.navigate(Screen.ExerciseDetail(exerciseId.toString()))
    }

    fun clearGeneralError() {
        _uiState.update { currentState ->
            currentState.copy(
                error =
                    currentState.error.copy(
                        hasGeneralError = false,
                        generalError = null
                    )
            )
        }
    }
}