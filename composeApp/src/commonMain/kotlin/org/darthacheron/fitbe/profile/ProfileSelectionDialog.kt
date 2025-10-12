package org.darthacheron.fitbe.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_profile
import fitbe.composeapp.generated.resources.ic_profile_selected
import fitbe.composeapp.generated.resources.select_profile_dialog_cancel
import fitbe.composeapp.generated.resources.select_profile_dialog_select
import fitbe.composeapp.generated.resources.select_profile_dialog_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProfileSelectionDialog(
    profiles: List<Profile>,
    selectedProfileId: Uuid?,
    onProfileSelected: (Profile) -> Unit,
    onDismiss: () -> Unit
) {
    val initialSelectedIndex = profiles.indexOfFirst { it.id == selectedProfileId }.coerceAtLeast(0)
    var tempSelectedIndex by remember { mutableStateOf(initialSelectedIndex) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.select_profile_dialog_title)) },
        text = {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
            ) {
                itemsIndexed(profiles) { index, profile ->
                    val isSelected = tempSelectedIndex == index
                    val color = if (isSelected) Color.Blue else Color.Black
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = tempSelectedIndex == index,
                                    onClick = { tempSelectedIndex = index }
                                ).padding(vertical = 4.dp)
                                .border(2.dp, color, RoundedCornerShape(5.dp))
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .clickable(
                                    onClick = { tempSelectedIndex = index }
                                )
                    ) {
                        Icon(
                            painter =
                                painterResource(
                                    if (isSelected) Res.drawable.ic_profile_selected else Res.drawable.ic_profile
                                ),
                            tint = color,
                            contentDescription = null
                        )
                        Text(
                            text = profile.name,
                            color = color,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onProfileSelected(profiles[tempSelectedIndex])
                }
            ) {
                Text(text = stringResource(Res.string.select_profile_dialog_select))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.select_profile_dialog_cancel))
            }
        }
    )
}