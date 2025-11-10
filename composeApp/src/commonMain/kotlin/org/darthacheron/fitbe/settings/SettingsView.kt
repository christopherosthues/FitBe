package org.darthacheron.fitbe.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_back
import fitbe.composeapp.generated.resources.ic_reset
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.settings_body_measurement_unit
import fitbe.composeapp.generated.resources.settings_content_description_back
import fitbe.composeapp.generated.resources.settings_content_description_reset
import fitbe.composeapp.generated.resources.settings_content_description_save
import fitbe.composeapp.generated.resources.settings_distance_unit
import fitbe.composeapp.generated.resources.settings_export_all
import fitbe.composeapp.generated.resources.settings_export_beverages
import fitbe.composeapp.generated.resources.settings_export_button
import fitbe.composeapp.generated.resources.settings_export_equipment
import fitbe.composeapp.generated.resources.settings_export_exercises
import fitbe.composeapp.generated.resources.settings_export_include_defaults
import fitbe.composeapp.generated.resources.settings_export_sleep
import fitbe.composeapp.generated.resources.settings_export_steps
import fitbe.composeapp.generated.resources.settings_export_title
import fitbe.composeapp.generated.resources.settings_export_weight
import fitbe.composeapp.generated.resources.settings_theme
import fitbe.composeapp.generated.resources.settings_weight_unit
import fitbe.composeapp.generated.resources.top_bar_title_settings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    navHostController: NavHostController,
    viewModel: SettingsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }
    val uiState by viewModel.uiState.collectAsState()
    var showExportDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.error.generalError?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearError()
            }
        }
    }

    if (showExportDialog) {
        ExportDialog(
            uiState = uiState,
            onDismissRequest = { showExportDialog = false },
            onExportClick = { viewModel.exportData() },
            onExportAllChanged = viewModel::onExportAllChanged,
            onExportBeveragesChanged = viewModel::onExportBeveragesChanged,
            onExportSleepChanged = viewModel::onExportSleepChanged,
            onExportStepsChanged = viewModel::onExportStepsChanged,
            onExportWeightChanged = viewModel::onExportWeightChanged,
            onExportExercisesChanged = viewModel::onExportExercisesChanged,
            onExportExercisesIncludeDefaultsChanged = viewModel::onExportExercisesIncludeDefaultsChanged,
            onExportEquipmentChanged = viewModel::onExportEquipmentChanged,
            onExportEquipmentIncludeDefaultsChanged = viewModel::onExportEquipmentIncludeDefaultsChanged
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.top_bar_title_settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.revertChanges()
                        navHostController.navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = stringResource(Res.string.settings_content_description_back)
                        )
                    }
                },
                actions = {}
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { viewModel.resetToDefaults() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_reset),
                        contentDescription = stringResource(Res.string.settings_content_description_reset)
                    )
                }
                FloatingActionButton(
                    onClick = {
                        viewModel.saveSettings { navHostController.navigateUp() }
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.settings_content_description_save)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Text(
                    text = stringResource(Res.string.settings_weight_unit),
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    WeightUnit.entries.forEach { unit ->
                        Row(modifier = Modifier.padding(end = 8.dp)) {
                            RadioButton(
                                selected = uiState.currentWeightUnit == unit,
                                onClick = { viewModel.onWeightUnitChanged(unit) }
                            )
                            Text(
                                text = stringResource(unit.toStringResource()),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.settings_distance_unit),
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    DistanceUnit.entries.forEach { unit ->
                        Row(modifier = Modifier.padding(end = 8.dp)) {
                            RadioButton(
                                selected = uiState.currentDistanceUnit == unit,
                                onClick = { viewModel.onDistanceUnitChanged(unit) }
                            )
                            Text(
                                stringResource(unit.toResourceString()),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.settings_body_measurement_unit),
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    BodyMeasurementUnit.entries.forEach { unit ->
                        Row(modifier = Modifier.padding(end = 8.dp)) {
                            RadioButton(
                                selected = uiState.currentBodyMeasurementUnit == unit,
                                onClick = { viewModel.onBodyMeasurementUnitChanged(unit) }
                            )
                            Text(
                                stringResource(unit.toStringResource()),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.settings_theme),
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    ThemeMode.entries.forEach { mode ->
                        Row(modifier = Modifier.padding(end = 8.dp)) {
                            RadioButton(
                                selected = uiState.currentThemeMode == mode,
                                onClick = { viewModel.onThemeModeChanged(mode) }
                            )
                            Text(
                                text = stringResource(mode.toStringResource()),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.settings_export_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Button(onClick = { showExportDialog = true }) {
                    Text(text = stringResource(Res.string.settings_export_button))
                }
            }
        }
    }
}

@Composable
private fun CheckboxWithLabel(checked: Boolean, onCheckedChange: (Boolean) -> Unit, label: String, enabled: Boolean = true, subContent: (@Composable () -> Unit)? = null) {
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

@Composable
private fun ExportDialog(
    uiState: SettingsUiState,
    onDismissRequest: () -> Unit,
    onExportClick: () -> Unit,
    onExportAllChanged: (Boolean) -> Unit,
    onExportBeveragesChanged: (Boolean) -> Unit,
    onExportSleepChanged: (Boolean) -> Unit,
    onExportStepsChanged: (Boolean) -> Unit,
    onExportWeightChanged: (Boolean) -> Unit,
    onExportExercisesChanged: (Boolean) -> Unit,
    onExportExercisesIncludeDefaultsChanged: (Boolean) -> Unit,
    onExportEquipmentChanged: (Boolean) -> Unit,
    onExportEquipmentIncludeDefaultsChanged: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.settings_export_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CheckboxWithLabel(uiState.exportAll, onExportAllChanged, stringResource(Res.string.settings_export_all))
                CheckboxWithLabel(uiState.exportBeverages, onExportBeveragesChanged, stringResource(Res.string.settings_export_beverages), enabled = !uiState.exportAll)
                CheckboxWithLabel(uiState.exportSleep, onExportSleepChanged, stringResource(Res.string.settings_export_sleep), enabled = !uiState.exportAll)
                CheckboxWithLabel(uiState.exportSteps, onExportStepsChanged, stringResource(Res.string.settings_export_steps), enabled = !uiState.exportAll)
                CheckboxWithLabel(uiState.exportWeight, onExportWeightChanged, stringResource(Res.string.settings_export_weight), enabled = !uiState.exportAll)
                CheckboxWithLabel(uiState.exportExercises, onExportExercisesChanged, stringResource(Res.string.settings_export_exercises), enabled = !uiState.exportAll) {
                    CheckboxWithLabel(uiState.exportExercisesIncludeDefaults, onExportExercisesIncludeDefaultsChanged, stringResource(Res.string.settings_export_include_defaults), enabled = uiState.exportExercises)
                }
                CheckboxWithLabel(uiState.exportEquipment, onExportEquipmentChanged, stringResource(Res.string.settings_export_equipment), enabled = !uiState.exportAll) {
                    CheckboxWithLabel(uiState.exportEquipmentIncludeDefaults, onExportEquipmentIncludeDefaultsChanged, stringResource(Res.string.settings_export_include_defaults), enabled = uiState.exportEquipment)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onExportClick()
                onDismissRequest()
            }) {
                Text(text = stringResource(Res.string.settings_export_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}
