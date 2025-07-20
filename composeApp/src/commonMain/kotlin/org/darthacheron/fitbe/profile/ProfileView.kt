package org.darthacheron.fitbe.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.profile_add
import fitbe.composeapp.generated.resources.profile_body_height
import fitbe.composeapp.generated.resources.profile_edit
import fitbe.composeapp.generated.resources.profile_gender
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_save
import fitbe.composeapp.generated.resources.profile_select
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_duration
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import org.darthacheron.fitbe.components.DropdownSelection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.health.sleep.AdvancedTimePickerDialog
import org.darthacheron.fitbe.health.sleep.TimePickerDialog
import kotlin.collections.indexOf

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newGender by remember { mutableStateOf(Gender.MALE) }
    var newTargetKcal by remember { mutableStateOf("") }
    var newTargetBeverage by remember { mutableStateOf("") }
    var newTargetWeight by remember { mutableStateOf("") }
    var newTargetSleepDuration by remember { mutableStateOf(LocalTime(8, 0)) }
    var newTargetSteps by remember { mutableStateOf("") }
    var newBodyHeightInCm by remember { mutableStateOf("") }
    var showSleepDurationTimePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())  // Add scrolling capability
        ) {
            if (profiles.isNotEmpty()) {
                val selectedIndex = profiles.indexOf(currentProfile)
                DropdownSelection(
                    initialState = false,
                    items = profiles,
                    selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
                    title = stringResource(Res.string.profile_select),
                    itemContent = { profile, onClick ->
                        DropdownMenuItem(
                            text = { Text(profile.name) },
                            onClick = onClick
                        )
                    },
                    itemToString = { it.name },
                    onItemSelected = { index ->
                        profileViewModel.switchProfile(profiles[index].id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            currentProfile?.let { profile ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = if (isEditing) newName else profile.name,
                        onValueChange = { if (isEditing) newName = it },
                        label = { Text(stringResource(Res.string.profile_name)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                        value = if (isEditing) newTargetKcal else profile.targetKcal.toString(),
                        onValueChange = { if (isEditing) newTargetKcal = it },
                        label = { Text(stringResource(Res.string.profile_target_kcal)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = if (isEditing) newTargetBeverage else profile.targetBeverageInMilliliter.toString(),
                        onValueChange = { if (isEditing) newTargetBeverage = it },
                        label = { Text(stringResource(Res.string.profile_target_beverage)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = if (isEditing) newTargetWeight else profile.targetWeight.toString(),
                        onValueChange = { if (isEditing) newTargetWeight = it },
                        label = { Text(stringResource(Res.string.profile_target_weight)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Replace separate TextFields with TimeInput
                    OutlinedTextField(
                        value = "${
                            profile.targetSleepDuration.hour.toString().padStart(2, '0')
                        }:${profile.targetSleepDuration.minute.toString().padStart(2, '0')}",
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
                        value = if (isEditing) newTargetSteps else profile.targetSteps.toString(),
                        onValueChange = { if (isEditing) newTargetSteps = it },
                        label = { Text(stringResource(Res.string.profile_target_steps)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = if (isEditing) newBodyHeightInCm else profile.bodyHeightInCm.toString(),
                        onValueChange = { if (isEditing) newBodyHeightInCm = it },
                        label = { Text(stringResource(Res.string.profile_body_height)) },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Floating Action Button for adding a new profile
        FloatingActionButton(
            onClick = {
                if (isEditing) {
                    // Save profile
                    currentProfile?.let {
                        val updatedProfile = it.copy(
                            name = newName,
                            gender = newGender,
                            targetKcal = newTargetKcal.toUIntOrNull() ?: it.targetKcal,
                            targetBeverageInMilliliter = newTargetBeverage.toUIntOrNull() ?: it.targetBeverageInMilliliter,
                            targetWeight = newTargetWeight.toDoubleOrNull() ?: it.targetWeight,
                            targetSleepDuration = newTargetSleepDuration,
                            targetSteps = newTargetSteps.toUIntOrNull() ?: it.targetSteps,
                            bodyHeightInCm = newBodyHeightInCm.toUIntOrNull() ?: it.bodyHeightInCm
                        )
                        profileViewModel.editProfile(updatedProfile)
                    }
                } else {
                    // Entering edit mode: initialize input fields once
                    currentProfile?.let {
                        newName = it.name
                        newGender = it.gender
                        newTargetKcal = it.targetKcal.toString()
                        newTargetBeverage = it.targetBeverageInMilliliter.toString()
                        newTargetWeight = it.targetWeight.toString()
                        newTargetSleepDuration = it.targetSleepDuration
                        newTargetSteps = it.targetSteps.toString()
                        newBodyHeightInCm = it.bodyHeightInCm.toString()
                    }
                }
                isEditing = !isEditing
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF2196F3) // Example: blue
        ) {
            AnimatedVisibility(!isEditing) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = stringResource(Res.string.profile_edit)
                )
            }
            AnimatedVisibility(isEditing) {
                Icon(
                    painter = painterResource(Res.drawable.ic_save),
                    contentDescription = stringResource(Res.string.profile_save)
                )
            }
        }

        if (showSleepDurationTimePicker) {
            TimePickerDialog2(
                initialHour = newTargetSleepDuration.hour,
                initialMinute = newTargetSleepDuration.minute,
                onTimeSelected = { hour, minute ->
                    newTargetSleepDuration = LocalTime(hour, minute)
                    showSleepDurationTimePicker = false
                },
                onDismiss = { showSleepDurationTimePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog2(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )
    AdvancedTimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = { onTimeSelected(timePickerState.hour, timePickerState.minute) }
    ) {
        TimeInput(state = timePickerState)
    }
}