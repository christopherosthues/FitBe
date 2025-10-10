package org.darthacheron.fitbe.profile

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.ui.UiState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProfileUiState(
    isLoading: Boolean = false,
    val currentProfile: Profile? = null,
    val currentProfileDisplay: Profile? = null,
    val allProfilesDisplay: List<Profile> = emptyList(),
    error: ProfileError = ProfileError(),
    val isEditing: Boolean = false,
    val editingProfileId: Uuid? = null,
    val inputName: String = "",
    val inputDateOfBirth: LocalDate? = null,
    val inputGender: Gender = ProfileDefaults.gender,
    val inputTargetKcal: String = "",
    val inputTargetBeverage: String = "",
    val inputTargetWeight: String = "",
    val inputTargetSleepDuration: UInt? = null,
    val inputTargetSteps: String = "",
    val inputBodyHeight: String = "",
    val currentSettings: Settings = Settings()
) : UiState<ProfileError>(isLoading, error) {
    fun copy(
        isLoading: Boolean = this.isLoading,
        currentProfile: Profile? = this.currentProfile,
        currentProfileDisplay: Profile? = this.currentProfileDisplay,
        allProfilesDisplay: List<Profile> = this.allProfilesDisplay,
        error: ProfileError = this.error,
        isEditing: Boolean = this.isEditing,
        editingProfileId: Uuid? = this.editingProfileId,
        inputName: String = this.inputName,
        inputDateOfBirth: LocalDate? = this.inputDateOfBirth,
        inputGender: Gender = this.inputGender,
        inputTargetKcal: String = this.inputTargetKcal,
        inputTargetBeverage: String = this.inputTargetBeverage,
        inputTargetWeight: String = this.inputTargetWeight,
        inputTargetSleepDuration: UInt? = this.inputTargetSleepDuration,
        inputTargetSteps: String = this.inputTargetSteps,
        inputBodyHeight: String = this.inputBodyHeight,
        currentSettings: Settings = this.currentSettings
    ): ProfileUiState =
        ProfileUiState(
            isLoading,
            currentProfile,
            currentProfileDisplay,
            allProfilesDisplay,
            error,
            isEditing,
            editingProfileId,
            inputName,
            inputDateOfBirth,
            inputGender,
            inputTargetKcal,
            inputTargetBeverage,
            inputTargetWeight,
            inputTargetSleepDuration,
            inputTargetSteps,
            inputBodyHeight,
            currentSettings
        )
}