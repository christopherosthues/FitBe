package org.darthacheron.fitbe.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    navHostController: NavHostController,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }

    val errorResId = uiState.error.errorResId
    val errorMessage = if (errorResId != null && uiState.error.hasError) {
        stringResource(errorResId)
    } else {
        null
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearError() // Clear error after showing
            }
        }
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
                actions = {} // No actions in the top bar
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End, // Align FABs to the end of the Column
                verticalArrangement = Arrangement.spacedBy(16.dp) // Space between FABs
            ) {
                FloatingActionButton( // RESET FAB
                    onClick = { viewModel.resetToDefaults() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer, // Visually differentiate
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_reset),
                        contentDescription = stringResource(Res.string.settings_reset)
                    )
                }
                FloatingActionButton( // SAVE FAB
                    onClick = {
                        viewModel.saveSettings { navHostController.navigateUp() }
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.profile_save) // Existing resource for save
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // This padding already considers FAB if Scaffold is set up for it
                .padding(16.dp)
                .fillMaxSize()
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
            }
        }
    }
}
