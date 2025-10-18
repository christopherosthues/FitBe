package org.darthacheron.fitbe.profile

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ProfileRepository(
    private val profileDao: ProfileDao,
    private val settingsRepository: SettingsRepository
) {
    val profiles: Flow<List<Profile>> = getAllProfiles()

    fun getAllProfiles(): Flow<List<Profile>> =
        profileDao
            .getAllProfiles()
            .map { profileEntities -> profileEntities.map { it.toProfile() } }

    suspend fun getProfileById(id: Uuid): Profile? = profileDao.getProfileFlowById(id).first()?.toProfile()

    suspend fun getProfileFlowById(id: Uuid): Flow<Profile?> = profileDao.getProfileFlowById(id).map { it?.toProfile() }

    suspend fun getProfileByName(name: String): Profile? = profileDao.getProfileByName(name)?.toProfile()

    suspend fun upsertProfile(profile: Profile) {
        profileDao.upsertProfile(profile.toProfileEntity())
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile.toProfileEntity())
    }

    fun <E : Number> getTargetValueFlow(targetValue: (profile: Profile?) -> E?): Flow<E?> =
        settingsRepository.getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    getProfileFlowById(profileId)
                        .map { profile -> targetValue(profile)}
                } else {
                    flowOf(null)
                }
            }

}