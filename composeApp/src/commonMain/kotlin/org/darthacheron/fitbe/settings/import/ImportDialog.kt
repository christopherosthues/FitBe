package org.darthacheron.fitbe.settings.import

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.file_import
import fitbe.composeapp.generated.resources.import_cancel
import fitbe.composeapp.generated.resources.import_content_description_file_path
import fitbe.composeapp.generated.resources.import_file_path
import fitbe.composeapp.generated.resources.import_import
import fitbe.composeapp.generated.resources.import_import_profile
import fitbe.composeapp.generated.resources.import_select_profile
import fitbe.composeapp.generated.resources.import_title
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ImportDialog(
    viewModel: ImportDialogViewModel = koinInject(),
    onDismissRequest: () -> Unit,
    onImportClick: (importState: ImportDialogUiState) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val filePickerLauncher = rememberFilePickerLauncher(
        dialogSettings = FileKitDialogSettings.createDefault(),
        onResult = { file ->
            file?.let {
                viewModel.onImportPathChanged(it.absolutePath())
            }
        }
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.import_title)) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = uiState.importPath,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(text = stringResource(Res.string.import_file_path)) },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { filePickerLauncher.launch() }) {
                        Icon(
                            painter = painterResource(Res.drawable.file_import),
                            contentDescription = stringResource(Res.string.import_content_description_file_path)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.importPath.isNotEmpty()) {
                    Text(stringResource(Res.string.import_select_profile))
                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.profiles.forEach { profile ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (uiState.selectedProfileId == profile.id),
                                    onClick = { viewModel.onProfileSelected(profile.id) }
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.selectedProfileId == profile.id),
                                onClick = { viewModel.onProfileSelected(profile.id) }
                            )
                            Text(
                                text = profile.name,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = uiState.importProfile,
                                onClick = { viewModel.onImportProfileChanged(true) }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.importProfile,
                            onClick = { viewModel.onImportProfileChanged(true) }
                        )
                        Text(
                            text = stringResource(Res.string.import_import_profile),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onImportClick(uiState)
                    onDismissRequest()
                },
                enabled = uiState.canSave
            ) {
                Text(text = stringResource(Res.string.import_import))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.import_cancel))
            }
        }
    )
}
