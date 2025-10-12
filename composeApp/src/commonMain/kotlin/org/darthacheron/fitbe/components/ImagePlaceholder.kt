package org.darthacheron.fitbe.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import fitbe.composeapp.generated.resources.ic_verified
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImagePlaceholder(
    isEditing: Boolean,
    default: Boolean,
    contentDescription: String,
    defaultContentDescription: String
) {
    Box(
        modifier =
            Modifier
                .size(256.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_launcher),
            contentDescription = contentDescription,
            modifier = Modifier.size(128.dp),
            contentScale = ContentScale.Fit
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .fillMaxSize()
        )
        if (default && !isEditing) {
            Icon(
                painter = painterResource(Res.drawable.ic_verified),
                contentDescription = defaultContentDescription,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}