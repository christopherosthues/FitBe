package org.darthacheron.fitbe.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val name: String,
    val gender: Gender,
    val targetKcal: Int,
    val targetBeverageInMilliliter: Int,
    val targetWeight: Double,
    val targetSleepHours: Int,
    val targetSleepMinutes: Int,
    val targetSteps: Int
) {
    fun toProfile(): Profile = Profile(
        id = id,
        name = name,
        gender = gender,
        targetKcal = targetKcal.toUInt(),
        targetBeverageInMilliliter = targetBeverageInMilliliter.toUInt(),
        targetWeight = targetWeight,
        targetSleepHours = targetSleepHours.toUInt(),
        targetSleepMinutes = targetSleepMinutes.toUInt(),
        targetSteps = targetSteps.toUInt()
    )

    companion object {
        fun fromProfile(profile: Profile): ProfileEntity = ProfileEntity(
            id = profile.id,
            name = profile.name,
            gender = profile.gender,
            targetKcal = profile.targetKcal.toInt(),
            targetBeverageInMilliliter = profile.targetBeverageInMilliliter.toInt(),
            targetWeight = profile.targetWeight,
            targetSleepHours = profile.targetSleepHours.toInt(),
            targetSleepMinutes = profile.targetSleepMinutes.toInt(),
            targetSteps = profile.targetSteps.toInt()
        )
    }
}

