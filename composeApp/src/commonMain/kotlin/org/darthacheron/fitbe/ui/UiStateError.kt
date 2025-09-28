package org.darthacheron.fitbe.ui

import org.jetbrains.compose.resources.StringResource

abstract class UiStateError(
    val generalError: StringResource? = null
) {
    val hasGeneralError: Boolean get() = generalError != null
}