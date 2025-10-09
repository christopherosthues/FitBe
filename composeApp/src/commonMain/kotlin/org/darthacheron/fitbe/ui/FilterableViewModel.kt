package org.darthacheron.fitbe.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class FilterableViewModel(
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> = _filterText.asStateFlow()

    fun onFilterTextChanged(text: String) {
        _filterText.value = text
    }
}