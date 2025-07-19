package org.darthacheron.fitbe.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileRepository(private val profileDao: ProfileDao) {
    val profiles: Flow<List<Profile>> = getAllProfiles()

    fun getAllProfiles(): Flow<List<Profile>> =
        profileDao.getAllProfiles()
            .map { profileEntities -> profileEntities.map { it.toProfile() } }

    suspend fun getProfileById(id: Uuid): Profile? =
        profileDao.getProfileById(id)?.toProfile()

    suspend fun upsertProfile(profile: Profile) {
        profileDao.upsertProfile(ProfileEntity.fromProfile(profile))
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(ProfileEntity.fromProfile(profile))
    }
}

