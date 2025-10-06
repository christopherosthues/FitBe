package org.darthacheron.fitbe.health

import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError

abstract class OverviewUiState<Error : UiStateError>(
    isLoading: Boolean = false,
    error: Error,
    val viewState: ViewState) : UiState<Error>(isLoading, error) {

    val isDetail
        get() = viewState == ViewState.DetailView

    val isOverview
        get() = viewState == ViewState.Overview
}