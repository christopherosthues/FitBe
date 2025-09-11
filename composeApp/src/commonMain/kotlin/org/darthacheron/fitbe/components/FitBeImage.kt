package org.darthacheron.fitbe.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import io.github.vinceglb.filekit.PlatformFile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FitBeImage(
    imageResource: DrawableResource?,
    imageUri: String?,
    contentDescription: String?,
    modifier: Modifier
) {
    if (imageResource == null && imageUri != null) {
        AsyncImage(
            model = PlatformFile(imageUri),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(imageResource ?: Res.drawable.ic_launcher),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}