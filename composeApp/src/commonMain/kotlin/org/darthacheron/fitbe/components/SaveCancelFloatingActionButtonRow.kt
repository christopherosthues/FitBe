package org.darthacheron.fitbe.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_save
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SaveCancelFloatingActionButtonRow(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isEditing: Boolean,
    isLoading: Boolean,
    hasError: Boolean,
    saveButtonContentDescription: StringResource,
    cancelButtonContentDescription: StringResource,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = isEditing,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FloatingActionButton(
                onClick = { onCancel() },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_cancel),
                    contentDescription = stringResource(cancelButtonContentDescription)
                )
            }
            FloatingActionButton(
                onClick = { onSave() },
                containerColor = if (!isLoading && !hasError) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_save),
                    contentDescription = stringResource(saveButtonContentDescription)
                )
            }
        }
    }
}