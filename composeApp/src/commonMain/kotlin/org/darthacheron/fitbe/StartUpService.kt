package org.darthacheron.fitbe

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.profile.Profile
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.koin.core.logger.Logger
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class StartUpService(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) {
    fun initialize() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val settings = settingsRepository.getSettings()
                val profiles = profileRepository.profiles.first()

                if (profiles.isEmpty()) {
                    val defaultProfile = Profile()
                    profileRepository.upsertProfile(defaultProfile)
                    settingsRepository.saveSettings(settings.copy(selectedProfileId = defaultProfile.id))
                }
            } catch (exception: Exception) {
                // TODO proper error handling
            }
        }
    }
}