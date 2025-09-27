package org.darthacheron.fitbe.profile

import org.darthacheron.fitbe.health.componenets.UiStateError
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ProfileError(
    generalError: StringResource? = null,
    val nameError: StringResource? = null,
    val kcalError: StringResource? = null,
    val beverageError: StringResource? = null,
    val weightError: StringResource? = null,
    val stepsError: StringResource? = null,
    val heightError: StringResource? = null
) : UiStateError(generalError) {
    val hasNameError: Boolean get() = nameError != null
    val hasKcalError: Boolean get() = kcalError != null
    val hasBeverageError: Boolean get() = beverageError != null
    val hasWeightError: Boolean get() = weightError != null
    val hasStepsError: Boolean get() = stepsError != null
    val hasHeightError: Boolean get() = heightError != null

    val hasAnyFieldError: Boolean
        get() = hasNameError || hasKcalError || hasBeverageError || hasWeightError || hasStepsError || hasHeightError

    fun copy(
        generalError: StringResource? = this.generalError,
        nameError: StringResource? = this.nameError,
        kcalError: StringResource? = this.kcalError,
        beverageError: StringResource? = this.beverageError,
        weightError: StringResource? = this.weightError,
        stepsError: StringResource? = this.stepsError,
        heightError: StringResource? = this.heightError
    ): ProfileError {
        return ProfileError(generalError, nameError, kcalError, beverageError, weightError, stepsError, heightError)
    }
}