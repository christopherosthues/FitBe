package org.darthacheron.fitbe.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.fluid_unit_milliliter
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_profile
import fitbe.composeapp.generated.resources.ic_profile_selected
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.ic_switch
import fitbe.composeapp.generated.resources.profile_add
import fitbe.composeapp.generated.resources.profile_body_height
import fitbe.composeapp.generated.resources.profile_cancel
import fitbe.composeapp.generated.resources.profile_date_of_birth
import fitbe.composeapp.generated.resources.profile_delete
import fitbe.composeapp.generated.resources.profile_edit
import fitbe.composeapp.generated.resources.profile_error_beverage
import fitbe.composeapp.generated.resources.profile_error_height_cm
import fitbe.composeapp.generated.resources.profile_error_height_inch
import fitbe.composeapp.generated.resources.profile_error_kcal
import fitbe.composeapp.generated.resources.profile_error_name
import fitbe.composeapp.generated.resources.profile_error_steps
import fitbe.composeapp.generated.resources.profile_error_weight_kg
import fitbe.composeapp.generated.resources.profile_error_weight_lb
import fitbe.composeapp.generated.resources.profile_gender
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_save
import fitbe.composeapp.generated.resources.profile_select
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_duration
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.DatePickerModal
import org.darthacheron.fitbe.components.DropdownSelection
import org.darthacheron.fitbe.components.TimeInputDialog
import org.darthacheron.fitbe.components.validators.BeverageValidator
import org.darthacheron.fitbe.components.validators.BodyHeightValidator
import org.darthacheron.fitbe.components.validators.StepsValidator
import org.darthacheron.fitbe.components.validators.BodyWeightValidator
import org.darthacheron.fitbe.components.validators.KcalValidator
import org.darthacheron.fitbe.components.validators.PositiveDecimalValidator
import org.darthacheron.fitbe.components.validators.PositiveNumberValidator
import org.darthacheron.fitbe.settings.BodyMeasurementUnit
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.utils.toDoubleString
import org.darthacheron.fitbe.utils.toUintString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel,
    settingsRepository: SettingsRepository
) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())
    var isAdding by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newDateOfBirth by remember { mutableStateOf<LocalDate?>(Clock.System.now().toLocalDateTime(TimeZone.UTC).date) }
    var newGender by remember { mutableStateOf(ProfileDefaults.gender) }
    var newTargetKcal by remember { mutableStateOf(ProfileDefaults.KCAL.toString()) }
    var newTargetBeverage by remember { mutableStateOf(ProfileDefaults.BEVERAGE.toString()) }
    var newTargetWeight by remember { mutableStateOf(ProfileDefaults.WEIGHT_IN_KG.toString()) }
    var newTargetSleepDuration by remember { mutableStateOf<LocalTime?>(ProfileDefaults.SLEEP_DURATION) }
    var newTargetSteps by remember { mutableStateOf(ProfileDefaults.STEPS.toString()) }
    var newBodyHeightInCm by remember { mutableStateOf(ProfileDefaults.BODY_HEIGHT_IN_CM.toString()) }
    var showSleepDurationTimePicker by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showDateOfBirthDialog by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var kcalError by remember { mutableStateOf(false) }
    var beverageError by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var stepsError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            currentProfile?.let { profile ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (isEditing) newName else profile.name,
                        onValueChange = {
                            if (isEditing) {
                                newName = it
                                nameError =
                                    it.isBlank() || profiles.any { profile ->
                                        profile.name == it && profile.id != currentProfile?.id
                                    }
                            }
                        },
                        label = { Text(stringResource(Res.string.profile_name)) },
                        readOnly = !isEditing,
                        isError = nameError,
                        supportingText = {
                            if (nameError) Text(stringResource(Res.string.profile_error_name))
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { showProfileDialog = true },
                        enabled = !isEditing
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_switch),
                            contentDescription = stringResource(Res.string.profile_select)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newDateOfBirth.toString() else profile.dateOfBirth.toString(),
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.profile_date_of_birth)) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            enabled = isEditing,
                            onClick = {
                                showDateOfBirthDialog = true
                            }) {
                            Icon(
                                painterResource(Res.drawable.ic_date_range),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.width(8.dp))

                val genders = Gender.entries
                val selectedIndex = genders.indexOf(if (isEditing) newGender else profile.gender)
                DropdownSelection(
                    initialState = false,
                    items = genders,
                    selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
                    title = stringResource(Res.string.profile_gender),
                    isEnabled = isEditing,
                    itemContent = { gender, onClick ->
                        DropdownMenuItem(
                            text = { Text(text = stringResource(gender.localizedString())) },
                            onClick = onClick
                        )
                    },
                    itemToString = { stringResource(it.localizedString()) },
                    onItemSelected = { index ->
                        if (isEditing) newGender = genders[index]
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newTargetKcal else profile.targetKcal.toUintString(),
                    onValueChange = {
                        if (isEditing) {
                            newTargetKcal = it
                            kcalError =
                                !PositiveNumberValidator().validate(it) || !KcalValidator().validate(
                                    it.toUIntOrNull()
                                )
                        }
                    },
                    label = { Text(stringResource(Res.string.profile_target_kcal)) },
                    readOnly = !isEditing,
                    isError = kcalError,
                    supportingText = {
                        if (kcalError) Text(stringResource(Res.string.profile_error_kcal))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newTargetBeverage else profile.targetBeverageInMilliliter.toUintString(),
                    onValueChange = {
                        if (isEditing) {
                            newTargetBeverage = it
                            beverageError =
                                !PositiveNumberValidator().validate(it) || !BeverageValidator().validate(
                                    it.toUIntOrNull()
                                )
                        }
                    },
                    label = {
                        Text(
                            stringResource(
                                Res.string.profile_target_beverage,
                                stringResource(Res.string.fluid_unit_milliliter)
                            )
                        )
                    },
                    readOnly = !isEditing,
                    isError = beverageError,
                    supportingText = {
                        if (beverageError) Text(
                            stringResource(
                                Res.string.profile_error_beverage,
                                stringResource(Res.string.fluid_unit_milliliter)
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newTargetWeight else profile.targetWeight.toDoubleString(),
                    onValueChange = {
                        if (isEditing) {
                            newTargetWeight = it
                            weightError =
                                !PositiveDecimalValidator().validate(it) ||
                                        !BodyWeightValidator().validate(
                                            it.toDoubleOrNull(),
                                            settings.weightUnit
                                        )
                        }
                    },
                    label = {
                        Text(
                            stringResource(
                                Res.string.profile_target_weight,
                                stringResource(settings.weightUnit.localizedString())
                            )
                        )
                    },
                    readOnly = !isEditing,
                    isError = weightError,
                    supportingText = {
                        if (weightError) Text(stringResource(when(settings.weightUnit){
                            WeightUnit.KG -> Res.string.profile_error_weight_kg
                            WeightUnit.POUND -> Res.string.profile_error_weight_lb
                        }))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sleepDurationText(if (isEditing) newTargetSleepDuration else profile.targetSleepDuration),
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.profile_target_sleep_duration)) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            enabled = isEditing,
                            onClick = {
                                showSleepDurationTimePicker = true
                            }) {
                            Icon(
                                painterResource(Res.drawable.ic_access_time),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newTargetSteps else profile.targetSteps.toUintString(),
                    onValueChange = {
                        if (isEditing) {
                            newTargetSteps = it
                            stepsError =
                                !PositiveNumberValidator().validate(it) || !StepsValidator().validate(
                                    it.toUIntOrNull()
                                )
                        }
                    },
                    label = { Text(stringResource(Res.string.profile_target_steps)) },
                    readOnly = !isEditing,
                    isError = stepsError,
                    supportingText = {
                        if (stepsError) Text(stringResource(Res.string.profile_error_steps))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newBodyHeightInCm else profile.bodyHeight.toDoubleString(),
                    onValueChange = {
                        if (isEditing) {
                            newBodyHeightInCm = it
                            heightError =
                                !PositiveDecimalValidator().validate(it) || !BodyHeightValidator().validate(
                                    it.toDoubleOrNull()
                                )
                        }
                    },
                    label = {
                        Text(
                            stringResource(
                                Res.string.profile_body_height,
                                stringResource(settings.bodyMeasurementUnit.localizedString())
                            )
                        )
                    },
                    readOnly = !isEditing,
                    isError = heightError,
                    supportingText = {
                        if (heightError) Text(stringResource(when(settings.bodyMeasurementUnit){
                            BodyMeasurementUnit.CM -> Res.string.profile_error_height_cm
                            BodyMeasurementUnit.INCH -> Res.string.profile_error_height_inch
                        }))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(!isEditing) {
                FloatingActionButton(
                    onClick = {
                        currentProfile?.let { profile ->
                            profileViewModel.deleteProfile(profile.id)

                            // If no profiles left, create and select a default one
                            if (profiles.size <= 1) {
                                val defaultProfile = Profile()
                                profileViewModel.addAndSelectProfile(defaultProfile)
                            } else {
                                profileViewModel.switchProfile(profiles.first { it.id != profile.id }.id )
                            }
                        }
                    },
                    containerColor = Color.Red,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = stringResource(Res.string.profile_delete)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(isEditing) {
                FloatingActionButton(
                    onClick = {
                        // Cancel editing and reset fields
                        currentProfile?.let {
                            newName = it.name
                            newGender = it.gender
                            newTargetKcal = it.targetKcal.toUintString()
                            newTargetBeverage = it.targetBeverageInMilliliter.toUintString()
                            newTargetWeight = it.targetWeight.toDoubleString()
                            newTargetSleepDuration = it.targetSleepDuration
                            newTargetSteps = it.targetSteps.toUintString()
                            newBodyHeightInCm = it.bodyHeight.toDoubleString()
                            newDateOfBirth = it.dateOfBirth
                        }
                        isEditing = false
                        isAdding = false

                        nameError = false
                        kcalError = false
                        beverageError = false
                        weightError = false
                        stepsError = false
                        heightError = false
                    },
                    containerColor = Color.Red,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = stringResource(Res.string.profile_cancel)
                    )
                }
            }

            AnimatedVisibility(isEditing) {
                val isFormValid = listOf(
                    !nameError, !kcalError, !beverageError, !weightError,
                    !stepsError, !heightError
                ).all { it }
                FloatingActionButton(
                    onClick = {
                        if (isFormValid) {
                            val profile = Profile(
                                id = if (isAdding) Uuid.random() else currentProfile?.id
                                    ?: Uuid.random(),
                                name = newName,
                                gender = newGender,
                                targetKcal = newTargetKcal.toUIntOrNull(),
                                targetBeverageInMilliliter = newTargetBeverage.toUIntOrNull(),
                                targetWeight = newTargetWeight.replace(',', '.').toDoubleOrNull(),
                                targetSleepDuration = newTargetSleepDuration,
                                targetSteps = newTargetSteps.toUIntOrNull(),
                                bodyHeight = newBodyHeightInCm.replace(',', '.').toDoubleOrNull(),
                                dateOfBirth = newDateOfBirth
                            )
                            if (isAdding) {
                                profileViewModel.addAndSelectProfile(profile)
                            } else {
                                profileViewModel.editProfile(profile)
                            }
                            isEditing = false
                            isAdding = false
                        }
                    },
                    containerColor = if (isFormValid) Color(0xFF2196F3) else Color.Gray
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.profile_save)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(!isEditing) {
                FloatingActionButton(
                    onClick = {
                        currentProfile?.let {
                            newName = it.name
                            newGender = it.gender
                            newTargetKcal = it.targetKcal.toUintString()
                            newTargetBeverage = it.targetBeverageInMilliliter.toUintString()
                            newTargetWeight = it.targetWeight.toDoubleString()
                            newTargetSleepDuration = it.targetSleepDuration
                            newTargetSteps = it.targetSteps.toUintString()
                            newBodyHeightInCm = it.bodyHeight.toDoubleString()
                            newDateOfBirth = it.dateOfBirth
                        }
                        isEditing = true
                    },
                    containerColor = Color(0xFF2196F3),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = stringResource(Res.string.profile_edit)
                    )
                }
            }

            AnimatedVisibility(!isEditing) {
                FloatingActionButton(
                    onClick = {
                        currentProfile?.let {
                            val newProfile = Profile()
                            newName = newProfile.name
                            newGender = newProfile.gender
                            newTargetKcal = newProfile.targetKcal.toUintString()
                            newTargetBeverage = newProfile.targetBeverageInMilliliter.toUintString()
                            newTargetWeight = newProfile.targetWeight.toDoubleString()
                            newTargetSleepDuration = newProfile.targetSleepDuration
                            newTargetSteps = newProfile.targetSteps.toUintString()
                            newBodyHeightInCm = newProfile.bodyHeight.toDoubleString()
                            newDateOfBirth = newProfile.dateOfBirth
                        }
                        isEditing = true
                        isAdding = true
                    },
                    containerColor = Color(0xFF2196F3)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = stringResource(Res.string.profile_add)
                    )
                }
            }
        }

        if (showProfileDialog && profiles.isNotEmpty()) {
            ProfileSelectionDialog(
                profiles = profiles,
                selectedProfileId = currentProfile?.id,
                onProfileSelected = {
                    profileViewModel.switchProfile(it.id)
                    showProfileDialog = false
                },
                onDismiss = { showProfileDialog = false }
            )
        }

        if (showDateOfBirthDialog) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                        newDateOfBirth = date
                    }
                    showDateOfBirthDialog = false
                },
                onDismiss = { showDateOfBirthDialog = false }
            )
        }

        if (showSleepDurationTimePicker) {
            TimeInputDialog(
                initialHour = newTargetSleepDuration?.hour ?: ProfileDefaults.SLEEP_DURATION.hour,
                initialMinute = newTargetSleepDuration?.minute ?: ProfileDefaults.SLEEP_DURATION.minute,
                onTimeSelected = { hour, minute ->
                    newTargetSleepDuration = LocalTime(hour, minute)

                    showSleepDurationTimePicker = false
                },
                onDismiss = { showSleepDurationTimePicker = false }
            )
        }
    }
}

private fun sleepDurationText(sleepDuration: LocalTime?): String = "${
    sleepDuration?.hour.toString().padStart(2, '0')
}:${sleepDuration?.minute.toString().padStart(2, '0')}"

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
        title = { Text(text = stringResource(Res.string.profile_select)) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp) // Limit height to enable scrolling if needed
            ) {
                itemsIndexed(profiles) { index, profile ->
                    val isSelected = tempSelectedIndex == index
                    val color = if (isSelected) Color.Blue else Color.Black
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = tempSelectedIndex == index,
                                onClick = { tempSelectedIndex = index }
                            )
                            .padding(vertical = 4.dp)
                            .border(2.dp, color, RoundedCornerShape(5.dp))
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clickable(
                                onClick = { tempSelectedIndex = index }
                            )
                    ) {
                        Icon(
                            painter = painterResource(if (isSelected) Res.drawable.ic_profile_selected else Res.drawable.ic_profile),
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
                Text(text = stringResource(Res.string.profile_select))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.profile_cancel))
            }
        }
    )
}

