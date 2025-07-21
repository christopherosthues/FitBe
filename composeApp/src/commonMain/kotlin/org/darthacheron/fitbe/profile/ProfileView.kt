package org.darthacheron.fitbe.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
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
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.ic_switch
import fitbe.composeapp.generated.resources.profile_add
import fitbe.composeapp.generated.resources.profile_body_height
import fitbe.composeapp.generated.resources.profile_cancel
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
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.components.DropdownSelection
import org.darthacheron.fitbe.health.sleep.AdvancedTimePickerDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()
    var isAdding by remember { mutableStateOf(false) }
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
    var showProfileDialog by remember { mutableStateOf(false) }

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
                        onValueChange = { if (isEditing) newName = it },
                        label = { Text(stringResource(Res.string.profile_name)) },
                        readOnly = !isEditing,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { showProfileDialog = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_switch),
                            contentDescription = stringResource(Res.string.profile_select)
                        )
                    }
                }

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
                            newTargetKcal = it.targetKcal.toString()
                            newTargetBeverage = it.targetBeverageInMilliliter.toString()
                            newTargetWeight = it.targetWeight.toString()
                            newTargetSleepDuration = it.targetSleepDuration
                            newTargetSteps = it.targetSteps.toString()
                            newBodyHeightInCm = it.bodyHeightInCm.toString()
                        }
                        isEditing = false
                        isAdding = false
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
                FloatingActionButton(
                    onClick = {
                        // Save profile
                        val profile = Profile(
                            id = if (isAdding) Uuid.random() else currentProfile?.id ?: Uuid.random(),
                            name = newName,
                            gender = newGender,
                            targetKcal = newTargetKcal.toUIntOrNull() ?: 0u,
                            targetBeverageInMilliliter = newTargetBeverage.toUIntOrNull() ?: 0u,
                            targetWeight = newTargetWeight.toDoubleOrNull() ?: 0.0,
                            targetSleepDuration = newTargetSleepDuration,
                            targetSteps = newTargetSteps.toUIntOrNull() ?: 0u,
                            bodyHeightInCm = newBodyHeightInCm.toUIntOrNull() ?: 0u
                        )
                        if (isAdding) {
                            profileViewModel.addAndSelectProfile(profile)
                        } else {
                            profileViewModel.editProfile(profile)
                        }
                        isEditing = false
                        isAdding = false
                    },
                    containerColor = Color(0xFF2196F3)
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
                            newTargetKcal = it.targetKcal.toString()
                            newTargetBeverage = it.targetBeverageInMilliliter.toString()
                            newTargetWeight = it.targetWeight.toString()
                            newTargetSleepDuration = it.targetSleepDuration
                            newTargetSteps = it.targetSteps.toString()
                            newBodyHeightInCm = it.bodyHeightInCm.toString()
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
                            newTargetKcal = newProfile.targetKcal.toString()
                            newTargetBeverage = newProfile.targetBeverageInMilliliter.toString()
                            newTargetWeight = newProfile.targetWeight.toString()
                            newTargetSleepDuration = newProfile.targetSleepDuration
                            newTargetSteps = newProfile.targetSteps.toString()
                            newBodyHeightInCm = newProfile.bodyHeightInCm.toString()
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

//        if (isEditing) {
//            IconButton(
//                onClick = {
//                    currentProfile?.let {
//                        newName = it.name
//                        newGender = it.gender
//                        newTargetKcal = it.targetKcal.toString()
//                        newTargetBeverage = it.targetBeverageInMilliliter.toString()
//                        newTargetWeight = it.targetWeight.toString()
//                        newTargetSleepDuration = it.targetSleepDuration
//                        newTargetSteps = it.targetSteps.toString()
//                        newBodyHeightInCm = it.bodyHeightInCm.toString()
//                    }
//                    isEditing = false
//                },
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(16.dp)
//            ) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_cancel),
//                    contentDescription = stringResource(Res.string.profile_cancel)
//                )
//            }
//        }
//
//        Row {  }
//        // Floating Action Button for adding a new profile
//        FloatingActionButton(
//            onClick = {
//                if (isEditing) {
//                    // Save profile
//                    currentProfile?.let {
//                        val updatedProfile = it.copy(
//                            name = newName,
//                            gender = newGender,
//                            targetKcal = newTargetKcal.toUIntOrNull() ?: it.targetKcal,
//                            targetBeverageInMilliliter = newTargetBeverage.toUIntOrNull()
//                                ?: it.targetBeverageInMilliliter,
//                            targetWeight = newTargetWeight.toDoubleOrNull() ?: it.targetWeight,
//                            targetSleepDuration = newTargetSleepDuration,
//                            targetSteps = newTargetSteps.toUIntOrNull() ?: it.targetSteps,
//                            bodyHeightInCm = newBodyHeightInCm.toUIntOrNull() ?: it.bodyHeightInCm
//                        )
//                        profileViewModel.editProfile(updatedProfile)
//                    }
//                } else {
//                    // Entering edit mode: initialize input fields once
//                    currentProfile?.let {
//                        newName = it.name
//                        newGender = it.gender
//                        newTargetKcal = it.targetKcal.toString()
//                        newTargetBeverage = it.targetBeverageInMilliliter.toString()
//                        newTargetWeight = it.targetWeight.toString()
//                        newTargetSleepDuration = it.targetSleepDuration
//                        newTargetSteps = it.targetSteps.toString()
//                        newBodyHeightInCm = it.bodyHeightInCm.toString()
//                    }
//                }
//                isEditing = !isEditing
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp),
//            containerColor = Color(0xFF2196F3) // Example: blue
//        ) {
//            AnimatedVisibility(!isEditing) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_edit),
//                    contentDescription = stringResource(Res.string.profile_edit)
//                )
//            }
//            AnimatedVisibility(isEditing) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_save),
//                    contentDescription = stringResource(Res.string.profile_save)
//                )
//            }
//        }

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

private fun sleepDurationText(sleepDuration: LocalTime): String = "${
    sleepDuration.hour.toString().padStart(2, '0')
}:${sleepDuration.minute.toString().padStart(2, '0')}"

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProfileSelectionDialog(
    profiles: List<Profile>,
    selectedProfileId: Uuid?,
    onProfileSelected: (Profile) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text(text = stringResource(Res.string.profile_select)) },
        text = {
            val selectedIndex = profiles.indexOfFirst { it.id == selectedProfileId }
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
                    onProfileSelected(profiles[index])
                }
            )
        }
    )
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