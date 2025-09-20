package org.darthacheron.fitbe.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    navHostController: NavHostController,
    viewModel: SettingsViewModel
) {
    val settings = viewModel.currentSettings

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.top_bar_title_settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.revertChanges()
                        navHostController.navigateUp()
                    }) {
                        // Use custom back arrow drawable here instead of Icons
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveSettings()
                            navHostController.navigateUp()
                        }) {
                        // Use custom save drawable here
                        Icon(
                            painter = painterResource(Res.drawable.ic_save),
                            contentDescription = "Save"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.settings_weight_unit),
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                WeightUnit.entries.forEach { unit ->
                    Row {
                        RadioButton(
                            selected = settings.weightUnit == unit,
                            onClick = { viewModel.setWeightUnit(unit) }
                        )
                        Text(text = stringResource(unit.toStringResource()))
                    }
                }
            }

            Text(
                text = stringResource(Res.string.settings_distance_unit),
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                DistanceUnit.entries.forEach { unit ->
                    Row {
                        RadioButton(
                            selected = settings.distanceUnit == unit,
                            onClick = { viewModel.setDistanceUnit(unit) }
                        )
                        Text(stringResource(unit.toResourceString()))
                    }
                }
            }

            Text(
                text = stringResource(Res.string.settings_body_measurement_unit),
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                BodyMeasurementUnit.entries.forEach { unit ->
                    Row {
                        RadioButton(
                            selected = settings.bodyMeasurementUnit == unit,
                            onClick = { viewModel.setBodyMeasurementUnit(unit) }
                        )
                        Text(stringResource(unit.toStringResource()))
                    }
                }
            }

            Text(
                text = stringResource(Res.string.settings_theme),
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                ThemeMode.entries.forEach { mode ->
                    Row {
                        RadioButton(
                            selected = settings.themeMode == mode,
                            onClick = { viewModel.setThemeMode(mode) }
                        )
                        Text(
                            text = stringResource(mode.toStringResource())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.resetToDefaults() },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Use custom reset drawable here
                Icon(
                    painter = painterResource(Res.drawable.ic_reset),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.settings_reset))
            }
        }
    }
}
