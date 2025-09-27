package org.darthacheron.fitbe.profile

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_error_beverage
import fitbe.composeapp.generated.resources.profile_error_deleting
import fitbe.composeapp.generated.resources.profile_error_height_cm
import fitbe.composeapp.generated.resources.profile_error_height_inch
import fitbe.composeapp.generated.resources.profile_error_kcal
import fitbe.composeapp.generated.resources.profile_error_loading
import fitbe.composeapp.generated.resources.profile_error_name_blank
import fitbe.composeapp.generated.resources.profile_error_name_exists
import fitbe.composeapp.generated.resources.profile_error_saving
import fitbe.composeapp.generated.resources.profile_error_steps
import fitbe.composeapp.generated.resources.profile_error_switching
import fitbe.composeapp.generated.resources.profile_error_weight_kg
import fitbe.composeapp.generated.resources.profile_error_weight_lb
import fitbe.composeapp.generated.resources.top_bar_title_profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.validators.BeverageValidator
import org.darthacheron.fitbe.components.validators.BodyHeightValidator
import org.darthacheron.fitbe.components.validators.BodyWeightValidator
import org.darthacheron.fitbe.components.validators.KcalValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.components.validators.PositiveNumberValidator
import org.darthacheron.fitbe.components.validators.StepsValidator
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.settings.BodyMeasurementUnit
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.BodyMeasurementUnitConverter
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.ui.BottomNavigationBarViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.toDoubleString
import org.darthacheron.fitbe.utils.toUintString
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ProfileError(
    val hasGeneralError: Boolean = false,
    val generalError: StringResource? = null,
    val hasNameError: Boolean = false,
    val nameError: StringResource? = null,
    val hasKcalError: Boolean = false,
    val kcalError: StringResource? = null,
    val hasBeverageError: Boolean = false,
    val beverageError: StringResource? = null,
    val hasWeightError: Boolean = false,
    val weightError: StringResource? = null,
    val hasStepsError: Boolean = false,
    val stepsError: StringResource? = null,
    val hasHeightError: Boolean = false,
    val heightError: StringResource? = null
) {
    val hasAnyFieldError: Boolean
        get() = hasNameError || hasKcalError || hasBeverageError || hasWeightError || hasStepsError || hasHeightError
}

@OptIn(ExperimentalUuidApi::class)
data class ProfileUiState(
    val isLoading: Boolean = false,
    val currentProfile: Profile? = null,
    val currentProfileDisplay: Profile? = null,
    val allProfilesDisplay: List<Profile> = emptyList(),
    val error: ProfileError = ProfileError(),
    val isEditing: Boolean = false,
    val editingProfileId: Uuid? = null,

    val inputName: String = "",
    val inputDateOfBirth: LocalDate? = null,
    val inputGender: Gender = ProfileDefaults.gender,
    val inputTargetKcal: String = "",
    val inputTargetBeverage: String = "",
    val inputTargetWeight: String = "",
    val inputTargetSleepDuration: UInt? = null,
    val inputTargetSteps: String = "",
    val inputBodyHeight: String = "",
    val currentSettings: Settings = Settings()
)

@OptIn(ExperimentalUuidApi::class)
private data class ProfileCombinedInitData(
    val domainCurrent: Profile?,
    val displayCurrent: Profile?,
    val displayAll: List<Profile>,
    val settings: Settings
)

@OptIn(ExperimentalUuidApi::class)
class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val bodyMeasurementUnitConverter: BodyMeasurementUnitConverter,
    private val weightUnitConverter: WeightUnitConverter,
    topNavHostController: NavHostController,
    navHostController: NavHostController,
    topBarManager: TopBarManager,
    private val beverageValidator: BeverageValidator,
    private val bodyHeightValidator: BodyHeightValidator,
    private val bodyWeightValidator: BodyWeightValidator,
    private val kcalValidator: KcalValidator,
    private val positiveDecimalValidator: PositiveDecimalValidator,
    private val positiveNumberValidator: PositiveNumberValidator,
    private val stepsValidator: StepsValidator,
) : BottomNavigationBarViewModel(topNavHostController, navHostController, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_profile

    override val bottomBarSelected: Screen?
        get() = Screen.Profile

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        combine(
            profileRepository.profiles,
            settingsRepository.getSettingsFlow()
        ) { domainProfiles, settings ->
            val currentDomainProfile = settings.selectedProfileId?.let { id ->
                domainProfiles.find { it.id == id }
            } ?: domainProfiles.firstOrNull()

            val displayProfiles = domainProfiles.map { profile ->
                profile.copy(
                    targetWeight = weightUnitConverter.convert(
                        profile.targetWeight,
                        WeightUnit.KG,
                        settings.weightUnit
                    ),
                    bodyHeight = bodyMeasurementUnitConverter.convert(
                        profile.bodyHeight,
                        BodyMeasurementUnit.CM,
                        settings.bodyMeasurementUnit
                    )
                )
            }
            val displayCurrentProfile = currentDomainProfile?.copy(
                targetWeight = weightUnitConverter.convert(
                    currentDomainProfile.targetWeight,
                    WeightUnit.KG,
                    settings.weightUnit
                ),
                bodyHeight = bodyMeasurementUnitConverter.convert(
                    currentDomainProfile.bodyHeight,
                    BodyMeasurementUnit.CM,
                    settings.bodyMeasurementUnit
                )
            )
            ProfileCombinedInitData(
                currentDomainProfile,
                displayCurrentProfile,
                displayProfiles,
                settings
            )
        }.onEach { initData ->
            _uiState.update {
                val baseState = it.copy(
                    isLoading = false,
                    currentProfile = initData.domainCurrent,
                    currentProfileDisplay = initData.displayCurrent,
                    allProfilesDisplay = initData.displayAll,
                    error = if (initData.settings.selectedProfileId != null && initData.domainCurrent == null && initData.displayAll.isNotEmpty()) {
                        it.error.copy(
                            hasGeneralError = true,
                            generalError = Res.string.profile_error_loading
                        )
                    } else if (initData.domainCurrent == null && initData.displayAll.isEmpty() && !it.isEditing) {
                        it.error.copy(
                            hasGeneralError = it.error.hasGeneralError,
                            generalError = it.error.generalError
                        )
                    } else {
                        it.error.copy(hasGeneralError = false, generalError = null)
                    },
                    currentSettings = initData.settings
                )
                if (!it.isEditing) {
                    baseState.copy(
                        inputName = initData.displayCurrent?.name ?: ProfileDefaults.NAME,
                        inputDateOfBirth = initData.displayCurrent?.dateOfBirth
                            ?: Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                        inputGender = initData.displayCurrent?.gender ?: ProfileDefaults.gender,
                        inputTargetKcal = initData.displayCurrent?.targetKcal.toUintString(),
                        inputTargetBeverage = initData.displayCurrent?.targetBeverageInMilliliter.toUintString(),
                        inputTargetWeight = initData.displayCurrent?.targetWeight.toDoubleString(),
                        inputTargetSleepDuration = initData.displayCurrent?.targetSleepDuration
                            ?: ProfileDefaults.SLEEP_DURATION,
                        inputTargetSteps = initData.displayCurrent?.targetSteps.toUintString(),
                        inputBodyHeight = initData.displayCurrent?.bodyHeight.toDoubleString()
                    )
                } else {
                    baseState
                }
            }
        }.catch { e ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = ProfileError(
                        hasGeneralError = true,
                        generalError = Res.string.profile_error_loading
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    // --- Input Field Handlers ---
    fun onNameChanged(name: String) {
        _uiState.update { it.copy(inputName = name) }

        viewModelScope.launch {
            _uiState.update {
                val validatedStateAfterFieldChecks = _uiState.value
                val profileId = validatedStateAfterFieldChecks.editingProfileId ?: Uuid.random()
                val trimmedName = name.trim()
                val existingProfileByName = profileRepository.getProfileByName(trimmedName)

                val error = if (trimmedName.isBlank()) {
                    it.error.copy(
                        hasNameError = true,
                        nameError = Res.string.profile_error_name_blank
                    )
                } else if (existingProfileByName != null && existingProfileByName.id != profileId) {
                    it.error.copy(
                        hasNameError = true,
                        nameError = Res.string.profile_error_name_exists
                    )
                } else {
                    it.error.copy(hasNameError = false, nameError = null)
                }
                it.copy(error = error)
            }
        }
    }

    fun onDateOfBirthChanged(date: LocalDate?) =
        _uiState.update { it.copy(inputDateOfBirth = date) }

    fun onGenderChanged(gender: Gender) = _uiState.update { it.copy(inputGender = gender) }

    fun onTargetKcalChanged(kcal: String) = _uiState.update {
        val error =
            if (!positiveNumberValidator.validate(kcal) || !kcalValidator.validate(kcal.toUIntOrNull())) {
                it.error.copy(hasKcalError = true, kcalError = Res.string.profile_error_kcal)
            } else {
                it.error.copy(hasKcalError = false, kcalError = null)
            }
        it.copy(inputTargetKcal = kcal, error = error)
    }

    fun onTargetBeverageChanged(beverage: String) = _uiState.update {
        val error = if (!positiveDecimalValidator.validate(beverage) || !beverageValidator.validate(
                beverage.toDoubleOrNull()
            )) {
            it.error.copy(
                hasBeverageError = true,
                beverageError = Res.string.profile_error_beverage
            )
        } else {
            it.error.copy(hasBeverageError = false, beverageError = null)
        }
        it.copy(inputTargetBeverage = beverage, error = error)
    }

    fun onTargetWeightChanged(weight: String) = _uiState.update {
        val settings = it.currentSettings
        val error = if (!positiveDecimalValidator.validate(weight) ||
            !bodyWeightValidator.validate(
                weight.toDoubleOrNull(),
                settings.weightUnit
            )
        ) {
            it.error.copy(
                hasWeightError = true,
                weightError = when(settings.weightUnit){
                    WeightUnit.KG -> Res.string.profile_error_weight_kg
                    WeightUnit.POUND -> Res.string.profile_error_weight_lb
                }
            )
        } else {
            it.error.copy(hasWeightError = false, weightError = null)
        }
        it.copy(inputTargetWeight = weight, error = error)
    }

    fun onTargetSleepDurationChanged(duration: UInt?) =
        _uiState.update { it.copy(inputTargetSleepDuration = duration) }

    fun onTargetStepsChanged(steps: String) = _uiState.update {
        val error = if (!positiveNumberValidator.validate(steps) || !stepsValidator.validate(
                steps.toUIntOrNull()
            )) {
            it.error.copy(hasStepsError = true, stepsError = Res.string.profile_error_steps)
        } else {
            it.error.copy(hasStepsError = false, stepsError = null)
        }
        it.copy(inputTargetSteps = steps, error = error)
    }

    fun onBodyHeightChanged(height: String) = _uiState.update {
        val settings = it.currentSettings
        val error = if (!positiveDecimalValidator.validate(height) || !bodyHeightValidator.validate(
                height.toDoubleOrNull()
            )) {
            it.error.copy(
                hasHeightError = true,
                heightError = when(settings.bodyMeasurementUnit){
                    BodyMeasurementUnit.CM -> Res.string.profile_error_height_cm
                    BodyMeasurementUnit.INCH -> Res.string.profile_error_height_inch
                }
            )
        } else {
            it.error.copy(hasHeightError = false, heightError = null)
        }
        it.copy(inputBodyHeight = height, error = error)
    }

    // --- Mode Changers ---
    fun startEditingCurrentProfile() {
        _uiState.value.currentProfileDisplay?.let { current ->
            _uiState.update {
                it.copy(
                    isEditing = true,
                    editingProfileId = current.id,
                    error = ProfileError(),
                    inputName = current.name,
                    inputDateOfBirth = current.dateOfBirth,
                    inputGender = current.gender,
                    inputTargetKcal = current.targetKcal.toUintString(),
                    inputTargetBeverage = current.targetBeverageInMilliliter.toUintString(),
                    inputTargetWeight = current.targetWeight.toDoubleString(),
                    inputTargetSleepDuration = current.targetSleepDuration,
                    inputTargetSteps = current.targetSteps.toUintString(),
                    inputBodyHeight = current.bodyHeight.toDoubleString()
                )
            }
        }
    }

    fun startAddingNewProfile() {
        _uiState.update {
            it.copy(
                isEditing = true,
                editingProfileId = null,
                error = ProfileError(),
                inputName = ProfileDefaults.NAME,
                inputDateOfBirth = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                inputGender = ProfileDefaults.gender,
                inputTargetKcal = ProfileDefaults.KCAL.toUintString(),
                inputTargetBeverage = ProfileDefaults.BEVERAGE.toUintString(),
                inputTargetWeight = ProfileDefaults.WEIGHT_IN_KG.toDoubleString(),
                inputTargetSleepDuration = ProfileDefaults.SLEEP_DURATION,
                inputTargetSteps = ProfileDefaults.STEPS.toUintString(),
                inputBodyHeight = ProfileDefaults.BODY_HEIGHT_IN_CM.toDoubleString()
            )
        }
    }

    fun cancelEditingOrAdding() {
        val currentDisplay = _uiState.value.currentProfileDisplay
        _uiState.update {
            it.copy(
                isEditing = false,
                editingProfileId = null,
                error = it.error.copy(
                    hasNameError = false, nameError = null,
                    hasKcalError = false, kcalError = null,
                    hasBeverageError = false, beverageError = null,
                    hasWeightError = false, weightError = null,
                    hasStepsError = false, stepsError = null,
                    hasHeightError = false, heightError = null
                ),
                inputName = currentDisplay?.name ?: ProfileDefaults.NAME,
                inputDateOfBirth = currentDisplay?.dateOfBirth ?: Clock.System.now()
                    .toLocalDateTime(TimeZone.UTC).date,
                inputGender = currentDisplay?.gender ?: ProfileDefaults.gender,
                inputTargetKcal = currentDisplay?.targetKcal.toUintString(),
                inputTargetBeverage = currentDisplay?.targetBeverageInMilliliter.toUintString(),
                inputTargetWeight = currentDisplay?.targetWeight.toDoubleString(),
                inputTargetSleepDuration = currentDisplay?.targetSleepDuration
                    ?: ProfileDefaults.SLEEP_DURATION,
                inputTargetSteps = currentDisplay?.targetSteps.toUintString(),
                inputBodyHeight = currentDisplay?.bodyHeight.toDoubleString()
            )
        }
    }

    fun saveProfile() {
        val currentState = _uiState.value

        if (currentState.error.hasAnyFieldError) {
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                error = it.error.copy(hasGeneralError = false, generalError = null)
            )
        }

        viewModelScope.launch {
            val profileId = currentState.editingProfileId ?: Uuid.random()

            try {
                val settings = currentState.currentSettings
                val isAddingNew = currentState.editingProfileId == null

                val profileToSave = Profile(
                    id = profileId,
                    name = currentState.inputName.trim(),
                    gender = currentState.inputGender,
                    dateOfBirth = currentState.inputDateOfBirth,
                    targetKcal = currentState.inputTargetKcal.toUIntOrNull(),
                    targetBeverageInMilliliter = currentState.inputTargetBeverage.toUIntOrNull(),
                    targetWeight = currentState.inputTargetWeight.replace(
                        ",",
                        "."
                    )?.toDoubleOrNull()?.let {
                        weightUnitConverter.convert(it, settings.weightUnit, WeightUnit.KG)
                    },
                    targetSleepDuration = currentState.inputTargetSleepDuration,
                    targetSteps = currentState.inputTargetSteps.toUIntOrNull(),
                    bodyHeight = currentState.inputBodyHeight.replace(",", ".")
                        ?.toDoubleOrNull()?.let {
                        bodyMeasurementUnitConverter.convert(
                            it,
                            settings.bodyMeasurementUnit,
                            BodyMeasurementUnit.CM
                        )
                    }
                )

                profileRepository.upsertProfile(profileToSave)
                val savedProfileCheck = profileRepository.getProfileById(profileToSave.id)

                if (savedProfileCheck == null || savedProfileCheck != profileToSave) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ProfileError(
                                hasGeneralError = true,
                                generalError = Res.string.profile_error_saving
                            )
                            // Do not reset isEditing or editingProfileId, as save failed
                        )
                    }
                    return@launch
                }

                if (isAddingNew || settings.selectedProfileId == null || settings.selectedProfileId != profileToSave.id) {
                    settingsRepository.saveSettings(settings.copy(selectedProfileId = profileToSave.id))
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        editingProfileId = null,
                        error = ProfileError()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ProfileError(
                            hasGeneralError = true,
                            generalError = Res.string.profile_error_saving
                        )
                        // Do not reset isEditing or editingProfileId, as save failed due to unexpected exception
                    )
                }
            }
        }
    }

    fun deleteProfile(profileId: Uuid) {
        _uiState.update { it.copy(isLoading = true, error = ProfileError()) }
        viewModelScope.launch {
            try {
                val profileToDelete = profileRepository.getProfileById(profileId)
                if (profileToDelete != null) {
                    profileRepository.deleteProfile(profileToDelete)

                    val allProfilesAfterDeletion = profileRepository.profiles.first()
                    val currentSettings = settingsRepository.getSettings()

                    if (allProfilesAfterDeletion.isEmpty()) {
                        val newDefaultProfile =
                            Profile(id = Uuid.random())
                        val conflictingProfile =
                            profileRepository.getProfileByName(newDefaultProfile.name)
                        if (conflictingProfile != null) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = ProfileError(
                                        hasGeneralError = true,
                                        generalError = Res.string.profile_error_saving,
                                        hasNameError = true,
                                        nameError = Res.string.profile_error_name_exists
                                    ),
                                    currentProfile = null,
                                    currentProfileDisplay = null,
                                    allProfilesDisplay = emptyList()
                                )
                            }
                            return@launch
                        }
                        try {
                            profileRepository.upsertProfile(newDefaultProfile)
                            val savedDefaultCheck =
                                profileRepository.getProfileById(newDefaultProfile.id)
                            if (savedDefaultCheck == null || savedDefaultCheck != newDefaultProfile) {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = ProfileError(
                                            hasGeneralError = true,
                                            generalError = Res.string.profile_error_saving
                                        ),
                                        currentProfile = null,
                                        currentProfileDisplay = null,
                                        allProfilesDisplay = emptyList()
                                    )
                                }
                                return@launch
                            }
                            settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = newDefaultProfile.id))
                            _uiState.update { it.copy(isLoading = false) } // Rely on init flow to repopulate
                        } catch (e: Exception) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = ProfileError(
                                        hasGeneralError = true,
                                        generalError = Res.string.profile_error_deleting
                                    ),
                                    currentProfile = null,
                                    currentProfileDisplay = null,
                                    allProfilesDisplay = emptyList()
                                )
                            }
                        }
                    } else {
                        val newSelectedProfileId =
                            if (currentSettings.selectedProfileId == profileId ||
                                currentSettings.selectedProfileId == null ||
                                allProfilesAfterDeletion.none { it.id == currentSettings.selectedProfileId }
                            ) {
                                allProfilesAfterDeletion.first().id
                            } else {
                                currentSettings.selectedProfileId
                            }
                        settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = newSelectedProfileId))
                        _uiState.update { it.copy(isLoading = false) } // Rely on init flow
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ProfileError(
                                hasGeneralError = true,
                                generalError = Res.string.profile_error_deleting
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ProfileError(
                            hasGeneralError = true,
                            generalError = Res.string.profile_error_deleting
                        )
                    )
                }
            }
        }
    }

    fun switchProfile(profileId: Uuid) {
        _uiState.update { it.copy(isLoading = true, error = ProfileError()) }
        viewModelScope.launch {
            try {
                val targetProfileExists =
                    profileRepository.profiles.first().any { it.id == profileId }
                if (targetProfileExists) {
                    val currentSettings = settingsRepository.getSettings()
                    settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = profileId))
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditing = false,
                            editingProfileId = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ProfileError(
                                hasGeneralError = true,
                                generalError = Res.string.profile_error_switching
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ProfileError(
                            hasGeneralError = true,
                            generalError = Res.string.profile_error_switching
                        )
                    )
                }
            }
        }
    }

    fun clearGeneralError() {
        _uiState.update {
            it.copy(
                error = it.error.copy(
                    hasGeneralError = false,
                    generalError = null
                )
            )
        }
    }
}
