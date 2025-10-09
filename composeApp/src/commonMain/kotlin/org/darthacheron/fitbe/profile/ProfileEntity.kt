package org.darthacheron.fitbe.profile

import androidx.room.Entity
import androidx.room.Index // Added import
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "profiles",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false) val id: Uuid = Uuid.random(),
    val name: String,
    val gender: Gender,
    val targetKcal: Int?,
    val targetBeverageInMilliliter: Int?,
    val targetWeight: Double?,
    val targetSleepDuration: Int?,
    val targetSteps: Int?,
    val bodyHeightInCm: Double?,
    val dateOfBirth: LocalDate?
) {
    fun toProfile(): Profile =
        Profile(
            id = id,
            name = name,
            gender = gender,
            targetKcal = targetKcal?.toUInt(),
            targetBeverageInMilliliter = targetBeverageInMilliliter?.toUInt(),
            targetWeight = targetWeight,
            targetSleepDuration = targetSleepDuration?.toUInt(),
            targetSteps = targetSteps?.toUInt(),
            bodyHeight = bodyHeightInCm,
            dateOfBirth = dateOfBirth
        )
}

@OptIn(ExperimentalUuidApi::class)
fun Profile.toProfileEntity(): ProfileEntity =
    ProfileEntity(
        id = this.id,
        name = this.name,
        gender = this.gender,
        targetKcal = this.targetKcal?.toInt(),
        targetBeverageInMilliliter = this.targetBeverageInMilliliter?.toInt(),
        targetWeight = this.targetWeight,
        targetSleepDuration = this.targetSleepDuration?.toInt(),
        targetSteps = this.targetSteps?.toInt(),
        bodyHeightInCm = this.bodyHeight,
        dateOfBirth = this.dateOfBirth
    )