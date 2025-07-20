package org.darthacheron.fitbe

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.profile.Gender
import org.darthacheron.fitbe.profile.Profile
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class StartUpService(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) {
    fun initialize() {
        CoroutineScope(Dispatchers.Default).launch {
            val settings = settingsRepository.getSettings()
            val profiles = profileRepository.profiles.first()

            if (profiles.isEmpty()) {
                val defaultProfile = Profile(
                    name = "Default",
                    gender = Gender.UNKNOWN,
                    targetKcal = 2000u,
                    targetBeverageInMilliliter = 2000u,
                    targetWeight = 70.0,
                    targetSleepDuration = LocalTime(8, 0),
                    targetSteps = 10000u
                )
                profileRepository.upsertProfile(defaultProfile)
                settingsRepository.saveSettings(settings.copy(selectedProfileId = defaultProfile.id))
            }
        }
    }
}