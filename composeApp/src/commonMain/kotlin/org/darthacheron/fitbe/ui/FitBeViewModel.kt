package org.darthacheron.fitbe.ui


import androidx.lifecycle.ViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.state.TopBarAction
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.jetbrains.compose.resources.StringResource

abstract class FitBeViewModel(private val topBarManager: TopBarManager) : ViewModel() {
    abstract val title: StringResource
    abstract val bottomBarSelected: Screen?
    open val actions: List<TopBarAction> =  emptyList()
    open val backNavigationIconVisible: Boolean? = null

    init {
        updateTopBarConfig()
    }

    fun updateTopBarConfig() {
        topBarManager.setConfig(
            TopBarConfig(
                title = title,
                actions = actions,
                bottomBarSelected = bottomBarSelected,
                backNavigationIconVisible = backNavigationIconVisible
            )
        )
    }
}