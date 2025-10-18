package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_add_dialog_body_fat
import fitbe.composeapp.generated.resources.body_weight_add_dialog_body_water
import fitbe.composeapp.generated.resources.body_weight_add_dialog_bone_mass
import fitbe.composeapp.generated.resources.body_weight_add_dialog_cancel
import fitbe.composeapp.generated.resources.body_weight_add_dialog_content_description_date
import fitbe.composeapp.generated.resources.body_weight_add_dialog_muscle_mass
import fitbe.composeapp.generated.resources.body_weight_add_dialog_save
import fitbe.composeapp.generated.resources.body_weight_add_dialog_title
import fitbe.composeapp.generated.resources.body_weight_add_dialog_total_weight
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.local_date_format
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.settings.Settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddBodyWeightDialog(
    viewModel: AddBodyWeightDialogViewModel,
    initialDate: Instant? = null,
    onDismiss: () -> Unit,
    onSave: (
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double?,
        muscleMassInKg: Double?,
        boneMassInKg: Double?,
        bodyWaterInPercentage: Double?
    ) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState(initial = Settings())

    if (initialDate != null) {
        viewModel.onDateChange(initialDate.toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = viewModel::dismissDialog,
        title = { Text(stringResource(Res.string.body_weight_add_dialog_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = { showDatePicker = true }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = uiState.date.format(stringResource(Res.string.local_date_format)))
                        Icon(
                            painter = painterResource(Res.drawable.ic_date_range),
                            contentDescription =
                                stringResource(Res.string.body_weight_add_dialog_content_description_date)
                        )
                    }
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            millis?.let {
                                val selectedDate =
                                    Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                                viewModel.onDateChange(selectedDate)
                            }
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false },
                        initialSelectedDateMillis = uiState.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                    )
                }

                TextField(
                    value = uiState.weight,
                    onValueChange = viewModel::onWeightChange,
                    label = {
                        Text(
                            text =
                                stringResource(
                                    Res.string.body_weight_add_dialog_total_weight,
                                    stringResource(settings.weightUnit.toStringResource())
                                )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.weightError != null,
                    supportingText = {
                        uiState.weightError?.let { Text(text = stringResource(it)) }
                    }
                )
                TextField(
                    value = uiState.bodyFatInPercentage,
                    onValueChange = viewModel::onBodyFatChange,
                    label = { Text(stringResource(Res.string.body_weight_add_dialog_body_fat)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.bodyFatError != null,
                    supportingText = {
                        uiState.bodyFatError?.let { Text(text = stringResource(it)) }
                    }
                )
                TextField(
                    value = uiState.muscleMass,
                    onValueChange = viewModel::onMuscleMassChange,
                    label = {
                        Text(
                            stringResource(
                                Res.string.body_weight_add_dialog_muscle_mass,
                                stringResource(settings.weightUnit.toStringResource())
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.muscleMassError != null,
                    supportingText = {
                        uiState.muscleMassError?.let { Text(text = stringResource(it)) }
                    }
                )
                TextField(
                    value = uiState.boneMass,
                    onValueChange = viewModel::onBoneMassChange,
                    label = {
                        Text(
                            stringResource(
                                Res.string.body_weight_add_dialog_bone_mass,
                                stringResource(settings.weightUnit.toStringResource())
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.boneMassError != null,
                    supportingText = {
                        uiState.boneMassError?.let { Text(text = stringResource(it)) }
                    }
                )
                TextField(
                    value = uiState.bodyWaterInPercentage,
                    onValueChange = viewModel::onBodyWaterChange,
                    label = { Text(stringResource(Res.string.body_weight_add_dialog_body_water)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.bodyWaterError != null,
                    supportingText = {
                        uiState.bodyWaterError?.let { Text(text = stringResource(it)) }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.dismissDialog()

                    val muscleMassInKg =
                        if (uiState.muscleMass.toDoubleOrNull() != null) {
                            settings.weightUnit.toKilogram(uiState.muscleMass.toDouble())
                        } else {
                            null
                        }
                    val boneMassInKg =
                        if (uiState.boneMass.toDoubleOrNull() != null) {
                            settings.weightUnit.toKilogram(uiState.boneMass.toDouble())
                        } else {
                            null
                        }

                    onSave(
                        uiState.date,
                        settings.weightUnit.toKilogram(uiState.weight.toDouble()),
                        uiState.bodyFatInPercentage.toDoubleOrNull(),
                        muscleMassInKg,
                        boneMassInKg,
                        uiState.bodyWaterInPercentage.toDoubleOrNull()
                    )
                },
                enabled = uiState.canSave
            ) {
                Text(text = stringResource(Res.string.body_weight_add_dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissDialog()
                onDismiss()
            }) {
                Text(stringResource(Res.string.body_weight_add_dialog_cancel))
            }
        }
    )
}
