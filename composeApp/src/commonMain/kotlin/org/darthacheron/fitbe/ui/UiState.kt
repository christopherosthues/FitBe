package org.darthacheron.fitbe.ui

abstract class UiState<Error : UiStateError>(val isLoading: Boolean = false, val error: Error)
