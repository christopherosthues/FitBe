package org.darthacheron.fitbe.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import fitbe.composeapp.generated.resources.ic_verified
import io.github.vinceglb.filekit.PlatformFile
import org.darthacheron.fitbe.exercises.equipment.getEquipmentImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImageWithDefault(imageUri: String?, default: Boolean, contentDescription: String?, modifier: Modifier) {
    val imageResource = getEquipmentImage(imageUri, default)
    Box {
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

        if (default) {
            Icon(
                painter = painterResource(Res.drawable.ic_verified),
                contentDescription = "Default equipment",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}