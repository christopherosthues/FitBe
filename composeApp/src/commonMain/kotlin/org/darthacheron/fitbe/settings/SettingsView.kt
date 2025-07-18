package org.darthacheron.fitbe.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.settings_distance_unit
import fitbe.composeapp.generated.resources.settings_kg
import fitbe.composeapp.generated.resources.settings_km
import fitbe.composeapp.generated.resources.settings_miles
import fitbe.composeapp.generated.resources.settings_pound
import fitbe.composeapp.generated.resources.settings_theme
import fitbe.composeapp.generated.resources.settings_theme_dark
import fitbe.composeapp.generated.resources.settings_theme_light
import fitbe.composeapp.generated.resources.settings_theme_system
import fitbe.composeapp.generated.resources.settings_weight_unit
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsView(
    viewModel: SettingsViewModel
) {
    val settings = viewModel.settings

    Column {
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
                    Text(
                        text = stringResource(
                            when (unit) {
                                WeightUnit.KG -> Res.string.settings_kg
                                WeightUnit.POUND -> Res.string.settings_pound
                            }
                        )
                    )
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
                    Text(
                        stringResource(
                            when (unit) {
                                DistanceUnit.KM -> Res.string.settings_km
                                DistanceUnit.MILES -> Res.string.settings_miles
                            }
                        )
                    )
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
                        text = stringResource(
                            when (mode) {
                                ThemeMode.LIGHT -> Res.string.settings_theme_light
                                ThemeMode.DARK -> Res.string.settings_theme_dark
                                ThemeMode.SYSTEM -> Res.string.settings_theme_system
                            }
                        )
                    )
                }
            }
        }
    }
}

