package org.darthacheron.fitbe.ui

import kotlinx.coroutines.flow.StateFlow
import org.darthacheron.fitbe.ui.state.TopBarConfig

interface TopBarManager {
    val topBarConfigFlow: StateFlow<TopBarConfig>

    fun setConfig(config: TopBarConfig)

    fun resetConfig()
}