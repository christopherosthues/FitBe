package org.darthacheron.fitbe.ui.state

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class TopBarConfig(
    val title: StringResource? = null, // Null means RootScreen will use its default logic
    val actions: List<TopBarAction> = emptyList(),
    val navigationIconVisible: Boolean? = null // Null means RootScreen will use its default logic
)
