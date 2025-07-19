package org.darthacheron.fitbe.profile

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileRepository(private val profileDao: ProfileDao) {
    suspend fun getAllProfiles(): List<Profile> =
        profileDao.getAllProfiles().map { it.toProfile() }

    suspend fun getProfileById(id: Uuid): Profile? =
        profileDao.getProfileById(id)?.toProfile()

    suspend fun addOrUpdateProfile(profile: Profile) {
        profileDao.upsertProfile(ProfileEntity.fromProfile(profile))
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(ProfileEntity.fromProfile(profile))
    }
}

