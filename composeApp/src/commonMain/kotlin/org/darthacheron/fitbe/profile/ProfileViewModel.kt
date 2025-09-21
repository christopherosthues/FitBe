package org.darthacheron.fitbe.profile

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_error_beverage_invalid
import fitbe.composeapp.generated.resources.profile_error_deleting
import fitbe.composeapp.generated.resources.profile_error_height_invalid
import fitbe.composeapp.generated.resources.profile_error_kcal_invalid
import fitbe.composeapp.generated.resources.profile_error_loading
import fitbe.composeapp.generated.resources.profile_error_name_blank
import fitbe.composeapp.generated.resources.profile_error_name_exists
import fitbe.composeapp.generated.resources.profile_error_saving
import fitbe.composeapp.generated.resources.profile_error_steps_invalid
import fitbe.composeapp.generated.resources.profile_error_switching
import fitbe.composeapp.generated.resources.profile_error_weight_invalid
import fitbe.composeapp.generated.resources.top_bar_title_profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val currentProfile: Profile? = null, // Domain model for current
    val currentProfileDisplay: Profile? = null, // Specifically for display with units applied
    val allProfilesDisplay: List<Profile> = emptyList(), // Specifically for display with units applied
    val error: ProfileError = ProfileError(),
    val isEditing: Boolean = false,
    val editingProfileId: Uuid? = null, // Null if adding a new profile, non-null if editing an existing one

    val inputName: String = "",
    val inputDateOfBirth: LocalDate? = null,
    val inputGender: Gender = ProfileDefaults.gender,
    val inputTargetKcal: String = "",
    val inputTargetBeverage: String = "",
    val inputTargetWeight: String = "",
    val inputTargetSleepDuration: LocalTime? = null,
    val inputTargetSteps: String = "",
    val inputBodyHeight: String = "",
    val currentSettings: Settings = Settings() // Keep a copy of current settings for conversions
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
    topBarManager: TopBarManager
) : BottomNavigationBarViewModel(topNavHostController, navHostController, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_profile

    override val bottomBarSelected: Screen?
        get() = Screen.Profile

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        combine(
            profileRepository.profiles, // Flow<List<Profile>> - domain units
            settingsRepository.getSettingsFlow() // Flow<Settings>
        ) { domainProfiles, settings ->
            val currentDomainProfile = settings.selectedProfileId?.let {
                id -> domainProfiles.find { it.id == id }
            } ?: domainProfiles.firstOrNull()

            val displayProfiles = domainProfiles.map { profile ->
                profile.copy(
                    targetWeight = weightUnitConverter.convert(profile.targetWeight, WeightUnit.KG, settings.weightUnit),
                    bodyHeight = bodyMeasurementUnitConverter.convert(profile.bodyHeight, BodyMeasurementUnit.CM, settings.bodyMeasurementUnit)
                )
            }
            val displayCurrentProfile = currentDomainProfile?.copy(
                targetWeight = weightUnitConverter.convert(currentDomainProfile.targetWeight, WeightUnit.KG, settings.weightUnit),
                bodyHeight = bodyMeasurementUnitConverter.convert(currentDomainProfile.bodyHeight, BodyMeasurementUnit.CM, settings.bodyMeasurementUnit)
            )
            ProfileCombinedInitData(currentDomainProfile, displayCurrentProfile, displayProfiles, settings)
        }.onEach { initData ->
            _uiState.update {
                val baseState = it.copy(
                    isLoading = false,
                    currentProfile = initData.domainCurrent,
                    currentProfileDisplay = initData.displayCurrent,
                    allProfilesDisplay = initData.displayAll,
                    error = if (initData.settings.selectedProfileId != null && initData.domainCurrent == null && initData.displayAll.isNotEmpty()) {
                        it.error.copy(hasGeneralError = true, generalError = Res.string.profile_error_loading)
                    } else if (initData.domainCurrent == null && initData.displayAll.isEmpty() && !it.isEditing) {
                        it.error.copy(hasGeneralError = it.error.hasGeneralError, generalError = it.error.generalError)
                    } else {
                        it.error.copy(hasGeneralError = false, generalError = null)
                    },
                    currentSettings = initData.settings
                )
                if (!it.isEditing) {
                    baseState.copy(
                        inputName = initData.displayCurrent?.name ?: ProfileDefaults.NAME,
                        inputDateOfBirth = initData.displayCurrent?.dateOfBirth ?: Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                        inputGender = initData.displayCurrent?.gender ?: ProfileDefaults.gender,
                        inputTargetKcal = initData.displayCurrent?.targetKcal.toUintString(),
                        inputTargetBeverage = initData.displayCurrent?.targetBeverageInMilliliter.toUintString(),
                        inputTargetWeight = initData.displayCurrent?.targetWeight.toDoubleString(),
                        inputTargetSleepDuration = initData.displayCurrent?.targetSleepDuration ?: ProfileDefaults.SLEEP_DURATION,
                        inputTargetSteps = initData.displayCurrent?.targetSteps.toUintString(),
                        inputBodyHeight = initData.displayCurrent?.bodyHeight.toDoubleString()
                    )
                } else {
                    baseState
                }
            }
        }.catch { e ->
            _uiState.update { it.copy(isLoading = false, error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_loading)) }
        }.launchIn(viewModelScope)
    }

    // --- Input Field Handlers ---
    fun onNameChanged(name: String) = _uiState.update {
        val error = if (name.trim().isBlank()) {
            it.error.copy(hasNameError = true, nameError = Res.string.profile_error_name_blank)
        } else {
            it.error.copy(hasNameError = false, nameError = null)
        }
        it.copy(inputName = name, error = error)
    }

    fun onDateOfBirthChanged(date: LocalDate?) = _uiState.update { it.copy(inputDateOfBirth = date) }
    fun onGenderChanged(gender: Gender) = _uiState.update { it.copy(inputGender = gender) }

    fun onTargetKcalChanged(kcal: String) = _uiState.update {
        val parsedKcal = kcal.toUIntOrNull()
        val error = if (kcal.isNotEmpty() && (parsedKcal == null || parsedKcal == 0u)) {
            it.error.copy(hasKcalError = true, kcalError = Res.string.profile_error_kcal_invalid)
        } else {
            it.error.copy(hasKcalError = false, kcalError = null)
        }
        it.copy(inputTargetKcal = kcal, error = error)
    }

    fun onTargetBeverageChanged(beverage: String) = _uiState.update {
        val parsedBeverage = beverage.toUIntOrNull()
        val error = if (beverage.isNotEmpty() && (parsedBeverage == null || parsedBeverage == 0u)) {
            it.error.copy(hasBeverageError = true, beverageError = Res.string.profile_error_beverage_invalid)
        } else {
            it.error.copy(hasBeverageError = false, beverageError = null)
        }
        it.copy(inputTargetBeverage = beverage, error = error)
    }

    fun onTargetWeightChanged(weight: String) = _uiState.update {
        val parsedWeight = weight.replace(",", ".").toDoubleOrNull()
        val error = if (weight.isNotEmpty() && (parsedWeight == null || parsedWeight <= 0)) {
            it.error.copy(hasWeightError = true, weightError = Res.string.profile_error_weight_invalid)
        } else {
            it.error.copy(hasWeightError = false, weightError = null)
        }
        it.copy(inputTargetWeight = weight, error = error)
    }

    fun onTargetSleepDurationChanged(duration: LocalTime?) = _uiState.update { it.copy(inputTargetSleepDuration = duration) }

    fun onTargetStepsChanged(steps: String) = _uiState.update {
        val parsedSteps = steps.toUIntOrNull()
        val error = if (steps.isNotEmpty() && (parsedSteps == null || parsedSteps == 0u)) {
            it.error.copy(hasStepsError = true, stepsError = Res.string.profile_error_steps_invalid)
        } else {
            it.error.copy(hasStepsError = false, stepsError = null)
        }
        it.copy(inputTargetSteps = steps, error = error)
    }

    fun onBodyHeightChanged(height: String) = _uiState.update {
        val parsedHeight = height.replace(",", ".").toDoubleOrNull()
        val error = if (height.isNotEmpty() && (parsedHeight == null || parsedHeight <= 0)) {
            it.error.copy(hasHeightError = true, heightError = Res.string.profile_error_height_invalid)
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
                    error = ProfileError(), // Clear all errors
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
                editingProfileId = null, // Indicates adding
                error = ProfileError(), // Clear all errors
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
                error = it.error.copy( // Clear only field errors, preserve general error if any from loading
                    hasNameError = false, nameError = null,
                    hasKcalError = false, kcalError = null,
                    hasBeverageError = false, beverageError = null,
                    hasWeightError = false, weightError = null,
                    hasStepsError = false, stepsError = null,
                    hasHeightError = false, heightError = null
                ),
                inputName = currentDisplay?.name ?: ProfileDefaults.NAME,
                inputDateOfBirth = currentDisplay?.dateOfBirth ?: Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                inputGender = currentDisplay?.gender ?: ProfileDefaults.gender,
                inputTargetKcal = currentDisplay?.targetKcal.toUintString(),
                inputTargetBeverage = currentDisplay?.targetBeverageInMilliliter.toUintString(),
                inputTargetWeight = currentDisplay?.targetWeight.toDoubleString(),
                inputTargetSleepDuration = currentDisplay?.targetSleepDuration ?: ProfileDefaults.SLEEP_DURATION,
                inputTargetSteps = currentDisplay?.targetSteps.toUintString(),
                inputBodyHeight = currentDisplay?.bodyHeight.toDoubleString()
            )
        }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        val trimmedName = currentState.inputName.trim()

        onNameChanged(trimmedName) // Use trimmed name for validation first
        onTargetKcalChanged(currentState.inputTargetKcal)
        onTargetBeverageChanged(currentState.inputTargetBeverage)
        onTargetWeightChanged(currentState.inputTargetWeight)
        onTargetStepsChanged(currentState.inputTargetSteps)
        onBodyHeightChanged(currentState.inputBodyHeight)
        
        val validatedStateAfterFieldChecks = _uiState.value // Re-fetch state after individual field validations

        if (validatedStateAfterFieldChecks.error.hasAnyFieldError) {
            return
        }

        _uiState.update { it.copy(isLoading = true, error = it.error.copy(hasGeneralError = false, generalError = null)) }

        viewModelScope.launch {
            val profileId = validatedStateAfterFieldChecks.editingProfileId ?: Uuid.random()
            val existingProfileByName = profileRepository.getProfileByName(trimmedName)

            if (existingProfileByName != null && existingProfileByName.id != profileId) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error.copy(hasNameError = true, nameError = Res.string.profile_error_name_exists)
                    )
                }
                return@launch
            }

            try {
                val settings = validatedStateAfterFieldChecks.currentSettings
                val isAddingNew = validatedStateAfterFieldChecks.editingProfileId == null

                val profileToSave = Profile(
                    id = profileId, // Use determined profileId
                    name = trimmedName, // Use trimmed name that was validated and checked
                    gender = validatedStateAfterFieldChecks.inputGender,
                    dateOfBirth = validatedStateAfterFieldChecks.inputDateOfBirth,
                    targetKcal = validatedStateAfterFieldChecks.inputTargetKcal.toUIntOrNull(),
                    targetBeverageInMilliliter = validatedStateAfterFieldChecks.inputTargetBeverage.toUIntOrNull(),
                    targetWeight = validatedStateAfterFieldChecks.inputTargetWeight.replace(",",".")?.toDoubleOrNull()?.let {
                        weightUnitConverter.convert(it, settings.weightUnit, WeightUnit.KG)
                    },
                    targetSleepDuration = validatedStateAfterFieldChecks.inputTargetSleepDuration,
                    targetSteps = validatedStateAfterFieldChecks.inputTargetSteps.toUIntOrNull(),
                    bodyHeight = validatedStateAfterFieldChecks.inputBodyHeight.replace(",",".")?.toDoubleOrNull()?.let {
                        bodyMeasurementUnitConverter.convert(it, settings.bodyMeasurementUnit, BodyMeasurementUnit.CM)
                    }
                )

                profileRepository.upsertProfile(profileToSave)

                if (isAddingNew || settings.selectedProfileId == null || settings.selectedProfileId != profileToSave.id) {
                    settingsRepository.saveSettings(settings.copy(selectedProfileId = profileToSave.id))
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        editingProfileId = null,
                        error = ProfileError() // Clear all errors on successful save
                    )
                }
            } catch (e: Exception) {
                // This will catch other errors during upsert, not related to name constraint if pre-check is done.
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_saving)
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
                        val newDefaultProfile = Profile(id = Uuid.random()) // Uses ProfileDefaults.NAME
                        val conflictingProfile = profileRepository.getProfileByName(newDefaultProfile.name)
                        if (conflictingProfile != null) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = ProfileError(
                                        hasGeneralError = true, generalError = Res.string.profile_error_saving,
                                        hasNameError = true, nameError = Res.string.profile_error_name_exists
                                    ),
                                    currentProfile = null, 
                                    currentProfileDisplay = null, 
                                    allProfilesDisplay = emptyList()
                                )
                            }
                            return@launch // Stop if default name conflicts
                        }
                        try {
                            profileRepository.upsertProfile(newDefaultProfile)
                            settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = newDefaultProfile.id))
                            _uiState.update { it.copy(isLoading = false) } // Rely on init flow to repopulate
                        } catch (e: Exception) {
                             _uiState.update { 
                                 it.copy(
                                     isLoading = false, 
                                     error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_deleting),
                                     currentProfile = null, 
                                     currentProfileDisplay = null, 
                                     allProfilesDisplay = emptyList()
                                )
                             }
                        }
                    } else {
                        val newSelectedProfileId = if (currentSettings.selectedProfileId == profileId ||
                                                      currentSettings.selectedProfileId == null ||
                                                      allProfilesAfterDeletion.none { it.id == currentSettings.selectedProfileId }) {
                            allProfilesAfterDeletion.first().id
                        } else {
                            currentSettings.selectedProfileId
                        }
                        settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = newSelectedProfileId))
                        _uiState.update { it.copy(isLoading = false) } // Rely on init flow
                    }
                } else { // profileToDelete == null
                    _uiState.update { it.copy(isLoading = false, error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_deleting)) }
                }
            } catch (e: Exception) { // Catch other exceptions during the process
                _uiState.update { it.copy(isLoading = false, error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_deleting)) }
            }
        }
    }

    fun switchProfile(profileId: Uuid) {
        _uiState.update { it.copy(isLoading = true, error = ProfileError()) }
        viewModelScope.launch {
            try {
                val targetProfileExists = profileRepository.profiles.first().any { it.id == profileId }
                if (targetProfileExists) {
                    val currentSettings = settingsRepository.getSettings()
                    settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = profileId))
                    _uiState.update { it.copy(isLoading = false, isEditing = false, editingProfileId = null) } 
                } else {
                     _uiState.update { it.copy(isLoading = false, error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_switching)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = ProfileError(hasGeneralError = true, generalError = Res.string.profile_error_switching)) }
            }
        }
    }

    fun clearGeneralError() {
        _uiState.update { it.copy(error = it.error.copy(hasGeneralError = false, generalError = null)) }
    }
}
