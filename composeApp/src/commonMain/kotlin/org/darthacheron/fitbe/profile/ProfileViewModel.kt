package org.darthacheron.fitbe.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles

    private val _currentProfile = MutableStateFlow<Profile?>(null)
    val currentProfile: StateFlow<Profile?> = _currentProfile

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            _profiles.value = profileRepository.getAllProfiles()
        }
    }

    fun addProfile(profile: Profile) {
        viewModelScope.launch {
            profileRepository.addOrUpdateProfile(profile)
            loadProfiles()
        }
    }

    fun editProfile(updatedProfile: Profile) {
        viewModelScope.launch {
            profileRepository.addOrUpdateProfile(updatedProfile)
            loadProfiles()
        }
    }

    fun deleteProfile(profileId: Uuid) {
        viewModelScope.launch {
            val profile = _profiles.value.find { it.id == profileId }
            if (profile != null) {
                profileRepository.deleteProfile(profile)
                loadProfiles()
                if (_currentProfile.value?.id == profileId) {
                    _currentProfile.value = null
                }
            }
        }
    }

    fun switchProfile(profileId: Uuid) {
        _currentProfile.value = _profiles.value.find { it.id == profileId }
    }
}