package org.darthacheron.fitbe.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_back
import fitbe.composeapp.generated.resources.ic_reset
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.profile_save
import fitbe.composeapp.generated.resources.settings_body_measurement_unit
import fitbe.composeapp.generated.resources.settings_content_description_back
import fitbe.composeapp.generated.resources.settings_distance_unit
import fitbe.composeapp.generated.resources.settings_reset
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

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.error.generalError?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearError() // Clear error after showing
            }
        }
    }
    val errorResId = uiState.error.generalError
    val errorMessage =
        if (errorResId != null && uiState.error.hasGeneralError) {
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
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.top_bar_title_settings)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
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
                        contentDescription = stringResource(Res.string.profile_save)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
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