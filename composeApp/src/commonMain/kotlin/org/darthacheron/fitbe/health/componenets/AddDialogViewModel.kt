package org.darthacheron.fitbe.health.componenets

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class AddDialogViewModel<UiState : DialogUiState> : ViewModel() {
    abstract val uiState: StateFlow<UiState>

    abstract fun dismissDialog()
}