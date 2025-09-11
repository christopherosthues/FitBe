package org.darthacheron.fitbe.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_verified
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImageWithDefault(imageUri: String?, imageResource: DrawableResource?, default: Boolean, contentDescription: String?, modifier: Modifier, defaultContentDescription: String?) {
    Box {
        FitBeImage(imageResource, imageUri, contentDescription, modifier)

        if (default) {
            Icon(
                painter = painterResource(Res.drawable.ic_verified),
                contentDescription = defaultContentDescription,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
