package org.darthacheron.fitbe.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_add
import fitbe.composeapp.generated.resources.profile_add_title
import fitbe.composeapp.generated.resources.profile_cancel
import fitbe.composeapp.generated.resources.profile_delete
import fitbe.composeapp.generated.resources.profile_edit
import fitbe.composeapp.generated.resources.profile_edit_title
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_save
import fitbe.composeapp.generated.resources.profile_switch
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_hours
import fitbe.composeapp.generated.resources.profile_target_sleep_minutes
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import fitbe.composeapp.generated.resources.profile_title
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var profileToEdit by remember { mutableStateOf<Profile?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(Res.string.profile_title),
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(profiles.size) { index ->
                val profile = profiles[index]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = profile.name)
                    Row {
                        TextButton(onClick = {
                            profileToEdit = profile; showAddEditDialog = true
                        }) {
                            Text(stringResource(Res.string.profile_edit))
                        }
                        TextButton(onClick = { profileViewModel.deleteProfile(profile.id) }) {
                            Text(stringResource(Res.string.profile_delete))
                        }
                        TextButton(onClick = { profileViewModel.switchProfile(profile.id) }) {
                            Text(stringResource(Res.string.profile_switch))
                        }
                    }
                }
            }
        }

        Button(onClick = { profileToEdit = null; showAddEditDialog = true }) {
            Text(stringResource(Res.string.profile_add))
        }
    }

    if (showAddEditDialog) {
        AddEditProfileDialog(
            profile = profileToEdit,
            onSave = { profile ->
                if (profileToEdit == null) {
                    profileViewModel.addProfile(profile)
                } else {
                    profileViewModel.editProfile(profile)
                }
                showAddEditDialog = false
            },
            onCancel = { showAddEditDialog = false }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddEditProfileDialog(profile: Profile?, onSave: (Profile) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var targetKcal by remember { mutableStateOf(profile?.targetKcal?.toString() ?: "") }
    var targetBeverage by remember {
        mutableStateOf(
            profile?.targetBeverageInMilliliter?.toString() ?: ""
        )
    }
    var targetWeight by remember { mutableStateOf(profile?.targetWeight?.toString() ?: "") }
    var targetSleepHours by remember { mutableStateOf(profile?.targetSleepHours?.toString() ?: "") }
    var targetSleepMinutes by remember {
        mutableStateOf(
            profile?.targetSleepMinutes?.toString() ?: ""
        )
    }
    var targetSteps by remember { mutableStateOf(profile?.targetSteps?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = stringResource(if (profile == null) Res.string.profile_add_title else Res.string.profile_edit_title)) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(Res.string.profile_name)) })
                TextField(
                    value = targetKcal,
                    onValueChange = { targetKcal = it },
                    label = { Text(stringResource(Res.string.profile_target_kcal)) })
                TextField(
                    value = targetBeverage,
                    onValueChange = { targetBeverage = it },
                    label = { Text(stringResource(Res.string.profile_target_beverage)) })
                TextField(
                    value = targetWeight,
                    onValueChange = { targetWeight = it },
                    label = { Text(stringResource(Res.string.profile_target_weight)) })
                TextField(
                    value = targetSleepHours,
                    onValueChange = { targetSleepHours = it },
                    label = { Text(stringResource(Res.string.profile_target_sleep_hours)) })
                TextField(
                    value = targetSleepMinutes,
                    onValueChange = { targetSleepMinutes = it },
                    label = { Text(stringResource(Res.string.profile_target_sleep_minutes)) })
                TextField(
                    value = targetSteps,
                    onValueChange = { targetSteps = it },
                    label = { Text(stringResource(Res.string.profile_target_steps)) })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newProfile = Profile(
                    id = profile?.id ?: Uuid.random(),
                    name = name,
                    targetKcal = targetKcal.toUIntOrNull() ?: 0u,
                    targetBeverageInMilliliter = targetBeverage.toUIntOrNull() ?: 0u,
                    targetWeight = targetWeight.toDoubleOrNull() ?: 0.0,
                    targetSleepHours = targetSleepHours.toUIntOrNull() ?: 0u,
                    targetSleepMinutes = targetSleepMinutes.toUIntOrNull() ?: 0u,
                    targetSteps = targetSteps.toUIntOrNull() ?: 0u
                )
                onSave(newProfile)
            }) {
                Text(stringResource(Res.string.profile_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(Res.string.profile_cancel))
            }
        }
    )
}