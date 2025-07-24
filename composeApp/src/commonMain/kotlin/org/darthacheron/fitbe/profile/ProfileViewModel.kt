package org.darthacheron.fitbe.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.settings.BodyMeasurementUnit
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.BodyMeasurementUnitConverter
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val bodyMeasurementUnitConverter: BodyMeasurementUnitConverter,
    private val weightUnitConverter: WeightUnitConverter
) : ViewModel() {
    private val _currentProfile = MutableStateFlow<Profile?>(null)

    init {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            settings.selectedProfileId?.let { id ->
                _currentProfile.value = profileRepository.getProfileById(id)
            }
        }
    }

    val profiles = combine(
        profileRepository.profiles, settingsRepository.getSettingsFlow()
    ) { profiles, settings ->
        profiles.map {
            it.copy(
                targetWeight = weightUnitConverter.convert(
                    it.targetWeight,
                    WeightUnit.KG,
                    settings.weightUnit
                ),
                bodyHeight = bodyMeasurementUnitConverter.convert(
                    it.bodyHeight,
                    BodyMeasurementUnit.CM, settings.bodyMeasurementUnit
                )
            )
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    val currentProfile: StateFlow<Profile?> = combine(
        _currentProfile,
        settingsRepository.getSettingsFlow()
    ) { profile, settings ->
        profile?.let {
            it.copy(
                targetWeight = weightUnitConverter.convert(
                    it.targetWeight,
                    WeightUnit.KG,
                    settings.weightUnit
                ),
                bodyHeight = bodyMeasurementUnitConverter.convert(
                    it.bodyHeight,
                    BodyMeasurementUnit.CM, settings.bodyMeasurementUnit
                )
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun addAndSelectProfile(profile: Profile) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            val domainProfile = profile.copy(
                targetWeight = weightUnitConverter.convert(profile.targetWeight, settings.weightUnit,
                    WeightUnit.KG),
                bodyHeight = bodyMeasurementUnitConverter.convert(profile.bodyHeight, settings.bodyMeasurementUnit,
                    BodyMeasurementUnit.CM)
            )
            profileRepository.upsertProfile(domainProfile)
            val savedProfile = profileRepository.getProfileById(profile.id)
            if (savedProfile != null) {
                _currentProfile.value = savedProfile
                // Update settings
                val currentSettings = settingsRepository.getSettings()
                settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = profile.id))
            }
        }
    }

    fun editProfile(updatedProfile: Profile) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            val domainProfile = updatedProfile.copy(
                targetWeight = weightUnitConverter.convert(updatedProfile.targetWeight, settings.weightUnit,
                    WeightUnit.KG),
                bodyHeight = bodyMeasurementUnitConverter.convert(updatedProfile.bodyHeight, settings.bodyMeasurementUnit,
                    BodyMeasurementUnit.CM)
            )
            profileRepository.upsertProfile(domainProfile)
            _currentProfile.value = profileRepository.getProfileById(updatedProfile.id)
        }
    }

    fun deleteProfile(profileId: Uuid) {
        viewModelScope.launch {
            val profile = profileRepository.getProfileById(profileId)
            if (profile != null) {
                profileRepository.deleteProfile(profile)
                if (_currentProfile.value?.id == profileId) {
                    _currentProfile.value = null
                }
            }
        }
    }

    fun switchProfile(profileId: Uuid) {
        viewModelScope.launch {
            _currentProfile.value = profileRepository.getProfileById(profileId)
            // Persist selected profile
            val currentSettings = settingsRepository.getSettings()
            settingsRepository.saveSettings(currentSettings.copy(selectedProfileId = profileId))
        }
    }
}