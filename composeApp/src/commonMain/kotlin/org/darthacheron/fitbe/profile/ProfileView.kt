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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
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
import fitbe.composeapp.generated.resources.profile_add
import fitbe.composeapp.generated.resources.profile_cancel
import fitbe.composeapp.generated.resources.profile_gender
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_save
import fitbe.composeapp.generated.resources.profile_select
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_duration
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import kotlin.uuid.ExperimentalUuidApi
import org.darthacheron.fitbe.components.DropdownSelection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.datetime.LocalTime
import kotlin.collections.indexOf

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newGender by remember { mutableStateOf(Gender.MALE) }
    var newTargetKcal by remember { mutableStateOf("") }
    var newTargetBeverage by remember { mutableStateOf("") }
    var newTargetWeight by remember { mutableStateOf("") }
    var newTargetSleepDuration by remember { mutableStateOf(LocalTime(8, 0))}
    var newTargetSleepHours by remember { mutableStateOf("") }
    var newTargetSleepMinutes by remember { mutableStateOf("") }
    var newTargetSteps by remember { mutableStateOf("") }

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
                ProfileDetails(profile)
            }
        }

        // Floating Action Button for adding a new profile
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF2196F3) // Example: blue
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.profile_add)
            )
        }

        // Dialog for adding a new profile
        if (showAddDialog) {
            val timePickerState = remember {
                TimePickerState(
                    initialHour = newTargetSleepHours.toIntOrNull() ?: 0,
                    initialMinute = newTargetSleepMinutes.toIntOrNull() ?: 0,
                    is24Hour = true
                )
            }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(stringResource(Res.string.profile_add)) },
                text = {
                    Column {
                        TextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text(stringResource(Res.string.profile_name)) }
                        )
                        val genders = Gender.entries
                        val selectedIndex = genders.indexOf(currentProfile?.gender)
                        DropdownSelection(
                            initialState = false,
                            items = genders,
                            selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
                            title = stringResource(Res.string.profile_gender),
                            itemContent = { gender, onClick ->
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(gender.localizedString())) },
                                    onClick = onClick
                                )
                            },
                            itemToString = { stringResource(it.localizedString()) },
                            onItemSelected = { index ->
                                newGender = genders[index]
                            }
                        )
                        TextField(
                            value = newTargetKcal,
                            onValueChange = { newTargetKcal = it.filter { c -> c.isDigit() } },
                            label = { Text(stringResource(Res.string.profile_target_kcal)) }
                        )
                        TextField(
                            value = newTargetBeverage,
                            onValueChange = { newTargetBeverage = it.filter { c -> c.isDigit() } },
                            label = { Text(stringResource(Res.string.profile_target_beverage)) }
                        )
                        TextField(
                            value = newTargetWeight,
                            onValueChange = {
                                newTargetWeight = it.filter { c -> c.isDigit() || c == '.' }
                            },
                            label = { Text(stringResource(Res.string.profile_target_weight)) }
                        )
                        TimeInput(
                            state = timePickerState,
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newTargetSteps,
                            onValueChange = { newTargetSteps = it.filter { c -> c.isDigit() } },
                            label = { Text(stringResource(Res.string.profile_target_steps)) }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Basic validation
                            if (newName.isNotBlank()) {
                                val profile = Profile(
                                    name = newName,
                                    gender = newGender,
                                    targetKcal = newTargetKcal.toUIntOrNull() ?: 0u,
                                    targetBeverageInMilliliter = newTargetBeverage.toUIntOrNull()
                                        ?: 0u,
                                    targetWeight = newTargetWeight.toDoubleOrNull() ?: 0.0,
                                    targetSleepDuration = LocalTime(timePickerState.hour, timePickerState.minute),
                                    targetSteps = newTargetSteps.toUIntOrNull() ?: 0u
                                )
                                profileViewModel.addAndSelectProfile(profile)
                                // Reset fields
                                newName = ""
                                newGender = Gender.UNKNOWN
                                newTargetKcal = ""
                                newTargetBeverage = ""
                                newTargetWeight = ""
                                newTargetSleepHours = ""
                                newTargetSleepMinutes = ""
                                newTargetSteps = ""
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text(stringResource(Res.string.profile_save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text(stringResource(Res.string.profile_cancel))
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(profile: Profile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = profile.name,
            onValueChange = { /* Will be handled in edit mode */ },
            label = { Text(stringResource(Res.string.profile_name)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        val genders = Gender.entries
        val selectedIndex = genders.indexOf(profile.gender)
        DropdownSelection(
            initialState = false,
            items = genders,
            selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
            title = stringResource(Res.string.profile_gender),
            isEnabled = false,
            itemContent = { gender, onClick ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(gender.localizedString())) },
                    onClick = onClick
                )
            },
            itemToString = { stringResource(it.localizedString()) },
            onItemSelected = { index ->
                // TODO: store new gender
//                newGender = genders[index]
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = profile.targetKcal.toString(),
            onValueChange = { /* Will be handled in edit mode */ },
            label = { Text(stringResource(Res.string.profile_target_kcal)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = profile.targetBeverageInMilliliter.toString(),
            onValueChange = { /* Will be handled in edit mode */ },
            label = { Text(stringResource(Res.string.profile_target_beverage)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = profile.targetWeight.toString(),
            onValueChange = { /* Will be handled in edit mode */ },
            label = { Text(stringResource(Res.string.profile_target_weight)) },
            readOnly = true,
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
                IconButton(onClick = {
                    // sleepDurationTimePicker = true
                }) {
                    Icon(painterResource(Res.drawable.ic_access_time), contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = profile.targetSteps.toString(),
            onValueChange = { /* Will be handled in edit mode */ },
            label = { Text(stringResource(Res.string.profile_target_steps)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}