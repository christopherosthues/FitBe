package org.darthacheron.fitbe.health

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_detail
import fitbe.composeapp.generated.resources.ic_overview
import org.jetbrains.compose.resources.DrawableResource

enum class ViewState {
    Overview,
    DetailView;

    @Composable
    fun drawableResource(): DrawableResource {
        return when (this) {
            Overview -> Res.drawable.ic_overview
            DetailView -> Res.drawable.ic_detail
        }
    }
}