package org.darthacheron.fitbe.health.componenets


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.darthacheron.fitbe.health.ViewState

class HealthViewModel : ViewModel() {
    val viewStateFlow = MutableStateFlow(ViewState.Overview)

    fun toggleViewState() {
        val viewState = if (viewStateFlow.value == ViewState.Overview) {
            ViewState.DetailView
        } else {
            ViewState.Overview
        }
        viewStateFlow.value = viewState
    }
}