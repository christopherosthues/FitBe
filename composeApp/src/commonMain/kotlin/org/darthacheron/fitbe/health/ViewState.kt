package org.darthacheron.fitbe.health

import fitbe.composeapp.generated.resources.health_view_content_description_detail
import fitbe.composeapp.generated.resources.health_view_content_description_overview
import fitbe.composeapp.generated.resources.ic_detail
import fitbe.composeapp.generated.resources.ic_overview
import fitbe.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class ViewState {
    Overview,
    DetailView;

    fun drawableResource(): DrawableResource =
        when (this) {
            Overview -> Res.drawable.ic_overview
            DetailView -> Res.drawable.ic_detail
        }

    fun contentDescriptionStringResource(): StringResource =
        when (this) {
            Overview -> Res.string.health_view_content_description_overview
            DetailView -> Res.string.health_view_content_description_detail
        }
}