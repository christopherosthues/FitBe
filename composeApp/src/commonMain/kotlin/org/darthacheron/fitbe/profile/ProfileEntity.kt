package org.darthacheron.fitbe.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val name: String,
    val gender: Gender,
    val targetKcal: Int?,
    val targetBeverageInMilliliter: Int?,
    val targetWeight: Double?,
    val targetSleepDuration: LocalTime?,
    val targetSteps: Int?,
    val bodyHeightInCm: Double?,
    val dateOfBirth: LocalDate?,
) {
    fun toProfile(): Profile = Profile(
        id = id,
        name = name,
        gender = gender,
        targetKcal = targetKcal?.toUInt(),
        targetBeverageInMilliliter = targetBeverageInMilliliter?.toUInt(),
        targetWeight = targetWeight,
        targetSleepDuration = targetSleepDuration,
        targetSteps = targetSteps?.toUInt(),
        bodyHeight = bodyHeightInCm,
        dateOfBirth = dateOfBirth
    )

    companion object {
        fun fromProfile(profile: Profile): ProfileEntity = ProfileEntity(
            id = profile.id,
            name = profile.name,
            gender = profile.gender,
            targetKcal = profile.targetKcal?.toInt(),
            targetBeverageInMilliliter = profile.targetBeverageInMilliliter?.toInt(),
            targetWeight = profile.targetWeight,
            targetSleepDuration = profile.targetSleepDuration,
            targetSteps = profile.targetSteps?.toInt(),
            bodyHeightInCm = profile.bodyHeight,
            dateOfBirth = profile.dateOfBirth,
        )
    }
}

