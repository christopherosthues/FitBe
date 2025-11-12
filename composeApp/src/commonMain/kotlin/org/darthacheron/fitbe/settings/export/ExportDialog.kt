package org.darthacheron.fitbe.settings.export

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_execution_duration_seconds
import fitbe.composeapp.generated.resources.export_all
import fitbe.composeapp.generated.resources.export_beverages
import fitbe.composeapp.generated.resources.export_cancel
import fitbe.composeapp.generated.resources.export_content_description_file_path
import fitbe.composeapp.generated.resources.export_equipment
import fitbe.composeapp.generated.resources.export_exercises
import fitbe.composeapp.generated.resources.export_export
import fitbe.composeapp.generated.resources.export_file_path
import fitbe.composeapp.generated.resources.export_include_defaults
import fitbe.composeapp.generated.resources.export_sleep
import fitbe.composeapp.generated.resources.export_steps
import fitbe.composeapp.generated.resources.export_title
import fitbe.composeapp.generated.resources.export_weight
import fitbe.composeapp.generated.resources.files
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ExportDialog(
    viewModel: ExportDialogViewModel = koinInject(),
    onDismissRequest: () -> Unit,
    onExportClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val dialogSettings = FileKitDialogSettings.createDefault()
    val galleryLauncher =
        rememberFileSaverLauncher(
            dialogSettings = dialogSettings,
            onResult = {
                viewModel.onExportPathChanged(it?.absolutePath() ?: "")
            }
        )
//        rememberFilePickerLauncher(
//            type = FileKitType.File("json", "xml", "csv"),
//            mode = FileKitMode.Single,
//            onResult = {
//                viewModel.onExportPathChanged(it?.absolutePath() ?: "")
//            }
//        )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.export_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CheckboxWithLabel(
                    uiState.exportAll,
                    viewModel::onExportAllChanged,
                    stringResource(Res.string.export_all)
                )
                CheckboxWithLabel(
                    uiState.exportBeverages,
                    viewModel::onExportBeveragesChanged,
                    stringResource(Res.string.export_beverages),
                    enabled = !uiState.exportAll
                )
                CheckboxWithLabel(
                    uiState.exportSleep,
                    viewModel::onExportSleepChanged,
                    stringResource(Res.string.export_sleep),
                    enabled = !uiState.exportAll
                )
                CheckboxWithLabel(
                    uiState.exportSteps,
                    viewModel::onExportStepsChanged,
                    stringResource(Res.string.export_steps),
                    enabled = !uiState.exportAll
                )
                CheckboxWithLabel(
                    uiState.exportWeight,
                    viewModel::onExportWeightChanged,
                    stringResource(Res.string.export_weight),
                    enabled = !uiState.exportAll
                )
                CheckboxWithLabel(
                    uiState.exportExercises,
                    viewModel::onExportExercisesChanged,
                    stringResource(Res.string.export_exercises),
                    enabled = !uiState.exportAll
                ) {
                    CheckboxWithLabel(
                        uiState.exportExercisesIncludeDefaults,
                        viewModel::onExportExercisesIncludeDefaultsChanged,
                        stringResource(Res.string.export_include_defaults),
                        enabled = uiState.exportExercises
                    )
                }
                CheckboxWithLabel(
                    uiState.exportEquipment,
                    viewModel::onExportEquipmentChanged,
                    stringResource(Res.string.export_equipment),
                    enabled = !uiState.exportAll
                ) {
                    CheckboxWithLabel(
                        uiState.exportEquipmentIncludeDefaults,
                        viewModel::onExportEquipmentIncludeDefaultsChanged,
                        stringResource(Res.string.export_include_defaults),
                        enabled = uiState.exportEquipment
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = uiState.exportPath,
                        onValueChange = viewModel::onExportPathChanged,
                        readOnly = true,
                        label = { Text(text = stringResource(Res.string.export_file_path)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        galleryLauncher.launch(suggestedName = "FitBeExport", extension = "json")
                    }) {
                        Icon(
                            painter = painterResource(Res.drawable.files),
                            contentDescription = stringResource(Res.string.export_content_description_file_path)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onExportClick()
                onDismissRequest()
            }) {
                Text(text = stringResource(Res.string.export_export))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.export_cancel))
            }
        }
    )
}

@Composable
private fun CheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    enabled: Boolean = true,
    subContent: (@Composable () -> Unit)? = null
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
            Text(text = label, modifier = Modifier.padding(start = 8.dp))
        }
        AnimatedVisibility(visible = subContent != null && checked) {
            if (subContent != null) {
                Row(modifier = Modifier.padding(start = 32.dp)) {
                    subContent()
                }
            }
        }
    }
}