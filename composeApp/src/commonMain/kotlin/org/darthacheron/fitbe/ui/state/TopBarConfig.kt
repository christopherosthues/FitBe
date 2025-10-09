package org.darthacheron.fitbe.ui.state

import androidx.compose.runtime.Immutable
import org.darthacheron.fitbe.navigation.Screen
import org.jetbrains.compose.resources.StringResource

@Immutable
data class TopBarConfig(
    val title: StringResource? = null,
    val actions: List<TopBarAction> = emptyList(),
    val backNavigationIconVisible: Boolean? = null,
    val bottomBarSelected: Screen? = null
)