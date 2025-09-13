package org.darthacheron.fitbe.workouts.exercises

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_detail_content_description_add_favorite
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_favorite
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_delete_new_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_exercise_not_found
import fitbe.composeapp.generated.resources.exercise_detail_error_loading_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_equipment
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_guide
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_muscle_group
import fitbe.composeapp.generated.resources.exercise_detail_error_missing_name
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_new_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_reset_non_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_error_saving_exercise
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.top_bar_title_add_edit_exercise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import org.darthacheron.fitbe.workouts.equipment.EquipmentRepository
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipment
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ExerciseDetailUiState(
    val name: String = "",
    val guide: String = "",
    val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    val imageUri: String? = null,
    val equipmentList: List<TrainingEquipment> = emptyList(),
    val recommendedFor: List<RecommendedFor> = emptyList(),
    val exerciseType: ExerciseType = ExerciseType.OTHER,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val exerciseId: Uuid? = null,
    val error: ExerciseError = ExerciseError(),
    val default: Boolean = false,
    val persistedDefaultName: String? = null,
    val persistedDefaultGuide: String? = null,
    val persistedDefaultImageUri: String? = null,
    val persistedDefaultMuscleGroups: List<MuscleGroup>? = null,
    val persistedDefaultEquipmentList: List<TrainingEquipment>? = null,
    val persistedDefaultRecommendedForList: List<RecommendedFor>? = null,
    val persistedDefaultExerciseType: ExerciseType? = null,
    val isModifiedFromPersistedDefault: Boolean = false,
    val isFavorite: Boolean = false
)

data class ExerciseError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null,
    val hasGuideError: Boolean = false,
    val guideError: StringResource? = null,
    val hasMuscleGroupError: Boolean = false,
    val muscleGroupError: StringResource? = null,
    val hasRecommendedForError: Boolean = false,
    val recommendedForError: StringResource? = null,
    val hasEquipmentError: Boolean = false,
    val equipmentError: StringResource? = null
) {
    val hasError: Boolean
        get() = hasGeneralError || hasNameError || hasGuideError || hasMuscleGroupError || hasEquipmentError || hasRecommendedForError
}

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ExerciseDetailViewModel(
    private val exerciseRepository: ExerciseRepository,
    equipmentRepository: EquipmentRepository,
    settingsRepository: SettingsRepository,
    private val navHostController: NavHostController,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_add_edit_exercise

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.ExercisesDashboard

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()

    private val currentProfileId: StateFlow<Uuid?> = settingsRepository.getSettingsFlow()
        .map { it.selectedProfileId }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val isFavoriteFlow: StateFlow<Boolean> =
        combine(
            uiState.map { it.exerciseId }.distinctUntilChanged(),
            currentProfileId
        ) { exerciseId, profileId ->
            if (exerciseId != null && profileId != null) {
                exerciseRepository.isFavorite(profileId, exerciseId)
            } else {
                flowOf(false)
            }
        }.flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            isFavoriteFlow.collect { isFav ->
                // This ensures the state is consistent with the database
                _uiState.update { it.copy(isFavorite = isFav) }
                updateTopBarConfig()
            }
        }
    }

    override val actions: List<TopBarAction>
        get() {
            val currentExerciseId = _uiState.value.exerciseId
            val currentProfId = currentProfileId.value
            val isCurrentlyFavorite = _uiState.value.isFavorite

            val favoriteAction = TopBarAction(
                icon = if (isCurrentlyFavorite) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                contentDescription = if (isCurrentlyFavorite) Res.string.exercise_detail_content_description_remove_favorite else Res.string.exercise_detail_content_description_add_favorite,
                onClick = { toggleFavorite() },
                isVisible = currentExerciseId != null && currentProfId != null
            )
            return listOf(favoriteAction)
        }

    private fun toggleFavorite() {
        val exerciseId = _uiState.value.exerciseId
        val profileId = currentProfileId.value

        if (exerciseId != null && profileId != null) {
            val currentIsFavorite = _uiState.value.isFavorite
            val newIsFavorite = !currentIsFavorite

            viewModelScope.launch {
                if (newIsFavorite) {
                    exerciseRepository.addFavorite(profileId, exerciseId)
                } else {
                    exerciseRepository.removeFavorite(profileId, exerciseId)
                }
            }
        }
    }

    val availableMuscleGroups: StateFlow<List<MuscleGroup>> = _uiState
        .map { currentState ->
            MuscleGroup.entries.filter { it !in currentState.targetMuscleGroups }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, MuscleGroup.entries)

    val availableEquipments: StateFlow<List<TrainingEquipment>> =
        combine(
            equipmentRepository.getAllEquipments(),
            uiState
        ) { equipments, uiState ->
            equipments.filter {
                val isNotIn = it.id !in uiState.equipmentList.map { eq -> eq.id }
                isNotIn
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun <T> List<T>.idsEqual(other: List<T>, idSelector: (T) -> Any): Boolean {
        if (this.size != other.size) return false
        return this.map(idSelector).toSet() == other.map(idSelector).toSet()
    }

    fun loadExercise(exerciseIdString: String?) {
        if (exerciseIdString == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = true,
                    default = false,
                    exerciseId = null,
                    name = "",
                    guide = "",
                    targetMuscleGroups = emptyList(),
                    imageUri = null,
                    equipmentList = emptyList(),
                    recommendedFor = emptyList(),
                    exerciseType = ExerciseType.OTHER,
                    error = ExerciseError(),
                    persistedDefaultName = null,
                    persistedDefaultGuide = null,
                    persistedDefaultMuscleGroups = null,
                    persistedDefaultEquipmentList = null,
                    persistedDefaultRecommendedForList = null,
                    persistedDefaultExerciseType = null,
                    isModifiedFromPersistedDefault = false,
                    isFavorite = false
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, exerciseId = Uuid.parse(exerciseIdString)) }
        viewModelScope.launch {
            try {
                val parsedExerciseId = Uuid.parse(exerciseIdString)
                val currentExerciseWithEquipment =
                    exerciseRepository.getExerciseWithExercisesById(parsedExerciseId).firstOrNull()

                if (currentExerciseWithEquipment != null) {
                    if (currentExerciseWithEquipment.default) {
                        val originalDefaultExercise =
                            exerciseRepository.getDefaultExerciseWithEquipment(
                                currentExerciseWithEquipment.id
                            ).firstOrNull()
                        _uiState.update {
                            it.copy(
                                name = currentExerciseWithEquipment.name,
                                guide = currentExerciseWithEquipment.guide,
                                targetMuscleGroups = currentExerciseWithEquipment.targetMuscleGroups,
                                imageUri = currentExerciseWithEquipment.imageUri,
                                equipmentList = currentExerciseWithEquipment.equipmentList,
                                recommendedFor = currentExerciseWithEquipment.recommendedFor,
                                exerciseType = currentExerciseWithEquipment.exerciseType,
                                default = true,
                                isLoading = false,
                                isEditing = false,
                                error = ExerciseError(),
                                persistedDefaultName = originalDefaultExercise?.name,
                                persistedDefaultGuide = originalDefaultExercise?.guide,
                                persistedDefaultMuscleGroups = originalDefaultExercise?.targetMuscleGroups,
                                persistedDefaultRecommendedForList = originalDefaultExercise?.recommendedFor,
                                persistedDefaultEquipmentList = originalDefaultExercise?.equipmentList,
                                isModifiedFromPersistedDefault = if (originalDefaultExercise != null) {
                                    (currentExerciseWithEquipment.name != originalDefaultExercise.name ||
                                            currentExerciseWithEquipment.guide != originalDefaultExercise.guide ||
                                            currentExerciseWithEquipment.exerciseType != originalDefaultExercise.exerciseType ||
                                            !currentExerciseWithEquipment.targetMuscleGroups.idsEqual(
                                                originalDefaultExercise.targetMuscleGroups
                                            ) { mg -> mg.ordinal } ||
                                            !currentExerciseWithEquipment.recommendedFor.idsEqual(
                                                originalDefaultExercise.recommendedFor
                                            ) { recommendedFor -> recommendedFor.ordinal } ||
                                            !currentExerciseWithEquipment.equipmentList.idsEqual(
                                                originalDefaultExercise.equipmentList
                                            ) { eq -> eq.id })
                                } else {
                                    false
                                }
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                name = currentExerciseWithEquipment.name,
                                guide = currentExerciseWithEquipment.guide,
                                targetMuscleGroups = currentExerciseWithEquipment.targetMuscleGroups,
                                imageUri = currentExerciseWithEquipment.imageUri,
                                equipmentList = currentExerciseWithEquipment.equipmentList,
                                recommendedFor = currentExerciseWithEquipment.recommendedFor,
                                exerciseType = currentExerciseWithEquipment.exerciseType,
                                default = false,
                                isLoading = false,
                                isEditing = false,
                                error = ExerciseError(),
                                persistedDefaultName = null,
                                persistedDefaultGuide = null,
                                persistedDefaultMuscleGroups = null,
                                persistedDefaultRecommendedForList = null,
                                persistedDefaultExerciseType = null,
                                persistedDefaultEquipmentList = null,
                                isModifiedFromPersistedDefault = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ExerciseError(
                                hasGeneralError = true,
                                generalError = Res.string.exercise_detail_error_exercise_not_found
                            ),
                            isEditing = false,
                            name = "",
                            guide = "",
                            targetMuscleGroups = emptyList(),
                            recommendedFor = emptyList(),
                            exerciseType = ExerciseType.OTHER,
                            imageUri = null,
                            equipmentList = emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ExerciseError(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_loading_exercise
                        ),
                        isEditing = false,
                        name = "",
                        guide = "",
                        targetMuscleGroups = emptyList(),
                        recommendedFor = emptyList(),
                        exerciseType = ExerciseType.OTHER,
                        imageUri = null,
                        equipmentList = emptyList()
                    )
                }
            }
        }
    }

    fun setIsEditing(isEditing: Boolean) {
        _uiState.update { it.copy(isEditing = isEditing) }
    }

    fun onImageUriChange(imageUri: String?) {
        _uiState.update { currentState ->
            val modified = if (currentState.default && currentState.persistedDefaultName != null) {
                (currentState.name != currentState.persistedDefaultName || imageUri != currentState.persistedDefaultImageUri)
                // TODO:
            } else {
                false
            }
            currentState.copy(
                imageUri = imageUri,
                error = currentState.error.copy(hasGeneralError = false, generalError = null),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onNameChange(name: String) {
        val nameError =
            if (name.isBlank()) Res.string.exercise_detail_error_missing_name else null
        // TODO: check if exercise with same name already exists
        // set hasNameError = true and nameError = Res.string.exercise_detail_error_name_already_exists

        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (name != currentState.persistedDefaultName ||
                        currentState.guide != currentState.persistedDefaultGuide ||
                        currentState.exerciseType != currentState.persistedDefaultExerciseType ||
                        !currentState.targetMuscleGroups.idsEqual(
                            currentState.persistedDefaultMuscleGroups ?: emptyList()
                        ) { mg -> mg.ordinal } ||
                        !currentState.recommendedFor.idsEqual(
                            currentState.persistedDefaultRecommendedForList ?: emptyList()
                        ) { recommendedFor -> recommendedFor.ordinal } ||
                        !currentState.equipmentList.idsEqual(
                            currentState.persistedDefaultEquipmentList ?: emptyList()
                        ) { eq -> eq.id })
            } else {
                false
            }
            currentState.copy(
                name = name,
                error = currentState.error.copy(
                    hasNameError = nameError != null,
                    nameError = nameError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun onGuideChange(guide: String) {
        val guideError =
            if (guide.isBlank()) Res.string.exercise_detail_error_missing_guide else null

        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                        guide != currentState.persistedDefaultGuide ||
                        currentState.exerciseType != currentState.persistedDefaultExerciseType ||
                        !currentState.targetMuscleGroups.idsEqual(
                            currentState.persistedDefaultMuscleGroups ?: emptyList()
                        ) { mg -> mg.ordinal } ||
                        !currentState.recommendedFor.idsEqual(
                            currentState.persistedDefaultRecommendedForList ?: emptyList()
                        ) { recommendedFor -> recommendedFor.ordinal } ||
                        !currentState.equipmentList.idsEqual(
                            currentState.persistedDefaultEquipmentList ?: emptyList()
                        ) { eq -> eq.id })
            } else {
                false
            }
            currentState.copy(
                guide = guide,
                error = currentState.error.copy(
                    hasGuideError = guideError != null,
                    guideError = guideError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    private fun onTargetMuscleGroupsChange(muscleGroups: List<MuscleGroup>) {
        val muscleGroupError =
            if (muscleGroups.isEmpty()) Res.string.exercise_detail_error_missing_muscle_group else null

        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                        currentState.guide != currentState.persistedDefaultGuide ||
                        currentState.exerciseType != currentState.persistedDefaultExerciseType ||
                        !muscleGroups.idsEqual(
                            currentState.persistedDefaultMuscleGroups ?: emptyList()
                        ) { mg -> mg.ordinal } ||
                        !currentState.recommendedFor.idsEqual(
                            currentState.persistedDefaultRecommendedForList ?: emptyList()
                        ) { recommendedFor -> recommendedFor.ordinal } ||
                        !currentState.equipmentList.idsEqual(
                            currentState.persistedDefaultEquipmentList ?: emptyList()
                        ) { eq -> eq.id })
            } else {
                false
            }
            currentState.copy(
                targetMuscleGroups = muscleGroups,
                error = currentState.error.copy(
                    hasMuscleGroupError = muscleGroupError != null,
                    muscleGroupError = muscleGroupError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun addMuscleGroup(muscleGroup: MuscleGroup) {
        val currentGroups = _uiState.value.targetMuscleGroups
        if (muscleGroup !in currentGroups) {
            onTargetMuscleGroupsChange(currentGroups + muscleGroup)
        }
    }

    fun removeMuscleGroup(muscleGroup: MuscleGroup) {
        val currentGroups = _uiState.value.targetMuscleGroups
        onTargetMuscleGroupsChange(currentGroups - muscleGroup)
    }

    private fun onEquipmentListChange(equipments: List<TrainingEquipment>) {
        val equipmentError =
            if (equipments.isEmpty()) Res.string.exercise_detail_error_missing_equipment else null
        _uiState.update { currentState ->
            val modified = if (currentState.default) {
                (currentState.name != currentState.persistedDefaultName ||
                        currentState.guide != currentState.persistedDefaultGuide ||
                        currentState.exerciseType != currentState.persistedDefaultExerciseType ||
                        !currentState.targetMuscleGroups.idsEqual(
                            currentState.persistedDefaultMuscleGroups ?: emptyList()
                        ) { mg -> mg.ordinal } ||
                        !currentState.recommendedFor.idsEqual(
                            currentState.persistedDefaultRecommendedForList ?: emptyList()
                        ) { recommendedFor -> recommendedFor.ordinal } ||
                        !equipments.idsEqual(
                            currentState.persistedDefaultEquipmentList ?: emptyList()
                        ) { eq -> eq.id })
            } else {
                false
            }
            currentState.copy(
                equipmentList = equipments,
                error = currentState.error.copy(
                    hasEquipmentError = equipmentError != null,
                    equipmentError = equipmentError,
                    hasGeneralError = false,
                    generalError = null
                ),
                isModifiedFromPersistedDefault = modified
            )
        }
    }

    fun addEquipment(equipment: TrainingEquipment) {
        val currentEquipments = _uiState.value.equipmentList
        if (equipment !in currentEquipments) {
            onEquipmentListChange(currentEquipments + equipment)
        }
    }

    fun removeEquipment(equipment: TrainingEquipment) {
        val currentEquipments = _uiState.value.equipmentList
        onEquipmentListChange(currentEquipments - equipment)
    }

    fun saveExercise() {
        val currentState = _uiState.value
        if (currentState.error.hasError) {
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val exerciseIdToUse = currentState.exerciseId ?: Uuid.random()
            val exerciseToSave = Exercise(
                id = exerciseIdToUse,
                name = currentState.name,
                guide = currentState.guide,
                targetMuscleGroups = currentState.targetMuscleGroups,
                recommendedFor = currentState.recommendedFor,
                exerciseType = currentState.exerciseType,
                imageUri = currentState.imageUri,
                default = currentState.default,
                dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            )

            try {
                exerciseRepository.upsertExercise(exerciseToSave)
                val equipmentIds = currentState.equipmentList.map { it.id }
                exerciseRepository.updateExerciseEquipmentLinks(exerciseIdToUse, equipmentIds)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        exerciseId = exerciseToSave.id,
                        name = exerciseToSave.name,
                        guide = exerciseToSave.guide,
                        targetMuscleGroups = exerciseToSave.targetMuscleGroups,
                        recommendedFor = exerciseToSave.recommendedFor,
                        exerciseType = exerciseToSave.exerciseType,
                        imageUri = exerciseToSave.imageUri,
                        equipmentList = currentState.equipmentList,
                        error = ExerciseError(),
                        isModifiedFromPersistedDefault = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_saving_exercise
                        )
                    )
                }
            }
        }
    }

    fun resetExerciseToDefault() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.exerciseId == null -> Res.string.exercise_detail_error_reset_new_exercise
            !currentState.default -> Res.string.exercise_detail_error_reset_non_default_exercise
            else -> null
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = ExerciseError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.exerciseId?.let {
                    exerciseRepository.resetExerciseToDefault(currentState.exerciseId)
                    loadExercise(currentState.exerciseId.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_reset_default_exercise
                        )
                    )
                }
            }
        }
    }

    fun deleteExercise() {
        val currentState = _uiState.value
        val errorRes = when {
            currentState.exerciseId == null -> Res.string.exercise_detail_error_delete_new_exercise
            currentState.default -> Res.string.exercise_detail_error_delete_default_exercise
            else -> null
        }

        if (errorRes != null) {
            _uiState.update {
                it.copy(
                    error = ExerciseError(
                        hasGeneralError = true,
                        generalError = errorRes
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                currentState.exerciseId?.let { exerciseId ->
                    val exerciseToDelete = Exercise(
                        id = exerciseId,
                        name = currentState.name,
                        guide = currentState.guide,
                        targetMuscleGroups = currentState.targetMuscleGroups,
                        recommendedFor = currentState.recommendedFor,
                        exerciseType = currentState.exerciseType,
                        imageUri = currentState.imageUri,
                        default = currentState.default,
                        dateUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    )
                    exerciseRepository.deleteExercise(exerciseToDelete)
                    navHostController.popBackStack()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.exercise_detail_error_delete_exercise
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearGeneralError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = currentState.error.copy(
                    hasGeneralError = false,
                    generalError = null
                )
            )
        }
    }
}
