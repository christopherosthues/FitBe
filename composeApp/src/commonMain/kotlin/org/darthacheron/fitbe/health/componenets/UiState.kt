package org.darthacheron.fitbe.health.componenets

import org.jetbrains.compose.resources.StringResource

abstract class UiState<Error : UiStateError>(val isLoading: Boolean = false, val error: Error)

abstract class UiStateError(
    val generalError: StringResource? = null
) {
    val hasGeneralError: Boolean get() = generalError != null
}