package org.darthacheron.fitbe.ui.state

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Immutable
data class TopBarAction(
    val icon: DrawableResource,
    val contentDescription: StringResource?,
    val onClick: () -> Unit,
    val isVisible: Boolean = true
)
