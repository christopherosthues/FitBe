package org.darthacheron.fitbe.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _currentProfile = MutableStateFlow<Profile?>(null)
    val currentProfile: StateFlow<Profile?> = _currentProfile

    init {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            settings.selectedProfileId?.let { id ->
                _currentProfile.value = profileRepository.getProfileById(id)
            }
        }
    }

    val profiles = profileRepository.profiles.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        listOf()
    )

    fun addProfile(profile: Profile) {
        viewModelScope.launch {
            profileRepository.upsertProfile(profile)
        }
    }

    fun editProfile(updatedProfile: Profile) {
        viewModelScope.launch {
            profileRepository.upsertProfile(updatedProfile)
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