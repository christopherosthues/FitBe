package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_body_fat
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_body_water
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_bone_mass
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_date
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_muscle_mass
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_title
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_total_weight
import fitbe.composeapp.generated.resources.body_weight_body_fat_error
import fitbe.composeapp.generated.resources.body_weight_body_water_error
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_cancel
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_save
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_kg
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_lb
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.WeightUnit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddBodyWeightDialog(
    settings: Settings,
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
    var weight by remember { mutableStateOf(0.0.toString()) }
    var bodyFat by remember { mutableStateOf(0.0.toString()) }
    var muscleMass by remember { mutableStateOf(0.0.toString()) }
    var boneMass by remember { mutableStateOf(0.0.toString()) }
    var bodyWater by remember { mutableStateOf(0.0.toString()) }
    var date by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date) }
    var showDateDialog by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var bodyFatError by remember { mutableStateOf(false) }
    var muscleMassError by remember { mutableStateOf(false) }
    var boneMassError by remember { mutableStateOf(false) }
    var bodyWaterError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.body_weight_add_body_weight_title)) },
        confirmButton = {
            TextButton(onClick = {
                val partsSum = listOfNotNull(
                    bodyFat.toDoubleOrNull(),
                    muscleMass.toDoubleOrNull(),
                    boneMass.toDoubleOrNull(),
                    bodyWater.toDoubleOrNull(),
                ).sum()

                if (partsSum <= weight.toDouble()) {
                    onSave(
                        date,
                        weight.toDouble(),
                        bodyFat.toDoubleOrNull(),
                        muscleMass.toDoubleOrNull(),
                        boneMass.toDoubleOrNull(),
                        bodyWater.toDoubleOrNull()
                    )
                }
            }) { Text(stringResource(Res.string.body_weight_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.body_weight_cancel)) }
        },
        text = {
            Column(Modifier.verticalScroll(scrollState)) {
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.body_weight_add_body_weight_date)) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDateDialog = true }) {
                            Icon(
                                painterResource(Res.drawable.ic_date_range),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                        weightError = it.startsWith("-") && it.length > 1
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(
                            stringResource(
                                Res.string.body_weight_add_body_weight_total_weight,
                                stringResource(settings.weightUnit.localizedString())
                            )
                        )
                    },
                    isError = weightError,
                    supportingText = {
                        if (weightError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_total_weight_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_total_weight_error_lb
                                }
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bodyFat,
                    onValueChange = { bodyFat = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text(stringResource(Res.string.body_weight_add_body_weight_body_fat)) },
                    isError = bodyFatError,
                    supportingText = {
                        if (bodyFatError) Text(
                            stringResource(Res.string.body_weight_body_fat_error)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = muscleMass,
                    onValueChange = { muscleMass = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(
                            stringResource(
                                Res.string.body_weight_add_body_weight_muscle_mass,
                                stringResource(settings.weightUnit.localizedString())
                            )
                        )
                    },
                    isError = muscleMassError,
                    supportingText = {
                        if (muscleMassError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_muscle_mass_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_muscle_mass_error_lb
                                }
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = boneMass,
                    onValueChange = { boneMass = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = {
                        Text(
                            stringResource(
                                Res.string.body_weight_add_body_weight_bone_mass,
                                stringResource(settings.weightUnit.localizedString())
                            )
                        )
                    },
                    isError = boneMassError,
                    supportingText = {
                        if (boneMassError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_bone_mass_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_bone_mass_error_lb
                                }
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bodyWater,
                    onValueChange = { bodyWater = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text(stringResource(Res.string.body_weight_add_body_weight_body_water)) },
                    isError = bodyWaterError,
                    supportingText = {
                        if (bodyWaterError) Text(
                            stringResource(Res.string.body_weight_body_water_error)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )

    if (showDateDialog) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                }
                showDateDialog = false
            },
            onDismiss = { showDateDialog = false }
        )
    }
}
