package org.darthacheron.fitbe.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.darthacheron.fitbe.ui.state.TopBarConfig

class ActualTopBarManager : TopBarManager {
    private val _topBarConfigFlow = MutableStateFlow(TopBarConfig()) // Default empty config
    override val topBarConfigFlow: StateFlow<TopBarConfig> = _topBarConfigFlow.asStateFlow()

    override fun setConfig(config: TopBarConfig) {
        _topBarConfigFlow.value = config
    }

    override fun resetConfig() {
        _topBarConfigFlow.value = TopBarConfig() // Reset to default
    }
}