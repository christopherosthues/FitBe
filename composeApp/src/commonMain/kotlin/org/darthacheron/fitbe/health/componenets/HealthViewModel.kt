package org.darthacheron.fitbe.health.componenets


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.darthacheron.fitbe.health.ViewState

class HealthViewModel : ViewModel() {
    val viewStateFlow = MutableStateFlow(ViewState.Overview)

    fun setViewState(viewState: ViewState) {
        viewStateFlow.value = viewState
    }
}