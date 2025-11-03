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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.fluid_unit_milliliter
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_switch
import fitbe.composeapp.generated.resources.local_date_format
import fitbe.composeapp.generated.resources.profile_body_height
import fitbe.composeapp.generated.resources.profile_content_description_add
import fitbe.composeapp.generated.resources.profile_content_description_cancel
import fitbe.composeapp.generated.resources.profile_content_description_delete
import fitbe.composeapp.generated.resources.profile_content_description_edit
import fitbe.composeapp.generated.resources.profile_content_description_save
import fitbe.composeapp.generated.resources.profile_content_description_select
import fitbe.composeapp.generated.resources.profile_date_of_birth
import fitbe.composeapp.generated.resources.profile_date_of_birth_select
import fitbe.composeapp.generated.resources.profile_gender
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_no_profiles_prompt_add
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_duration
import fitbe.composeapp.generated.resources.profile_target_sleep_duration_edit
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.DropdownSelection
import org.darthacheron.fitbe.components.SaveCancelFloatingActionButtonRow
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.components.date.TimeInputDialog
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    LaunchedEffect(Unit) {
        profileViewModel.updateTopBarConfig()
    }
    val uiState by profileViewModel.uiState.collectAsState()

    var showSleepDurationTimePicker by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showDateOfBirthDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.error.generalError?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                profileViewModel.clearGeneralError()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading && !uiState.isEditing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
            ) {
                if (uiState.currentProfileDisplay != null || uiState.editingProfileId == null && uiState.isEditing) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.inputName,
                            onValueChange = { profileViewModel.onNameChanged(it) },
                            label = { Text(stringResource(Res.string.profile_name)) },
                            readOnly = !uiState.isEditing,
                            isError = uiState.error.hasNameError,
                            supportingText = {
                                if (uiState.error.hasNameError) {
                                    uiState.error.nameError?.let { Text(stringResource(it)) }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { showProfileDialog = true },
                            enabled = !uiState.isEditing && uiState.allProfilesDisplay.size > 1
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_switch),
                                contentDescription = stringResource(Res.string.profile_content_description_select)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputDateOfBirth?.format(stringResource(Res.string.local_date_format)) ?: "",
                        onValueChange = {},
                        label = { Text(text = stringResource(Res.string.profile_date_of_birth)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                enabled = uiState.isEditing,
                                onClick = { showDateOfBirthDialog = true }
                            ) {
                                Icon(
                                    painterResource(Res.drawable.ic_date_range),
                                    contentDescription = stringResource(Res.string.profile_date_of_birth_select)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val genders = Gender.entries
                    val selectedIndex = genders.indexOf(uiState.inputGender)

                    DropdownSelection(
                        initialState = false,
                        items = genders,
                        selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
                        title = stringResource(Res.string.profile_gender),
                        isEnabled = uiState.isEditing,
                        itemContent = { gender, onClick ->
                            DropdownMenuItem(
                                text = { Text(text = stringResource(gender.toStringResource())) },
                                onClick = onClick
                            )
                        },
                        itemToString = { stringResource(it.toStringResource()) },
                        onItemSelected = { index ->
                            profileViewModel.onGenderChanged(genders[index])
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputTargetKcal,
                        onValueChange = { profileViewModel.onTargetKcalChanged(it) },
                        label = { Text(stringResource(Res.string.profile_target_kcal)) },
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasKcalError,
                        supportingText = {
                            if (uiState.error.hasKcalError) {
                                uiState.error.kcalError?.let { Text(stringResource(it)) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputTargetBeverage,
                        onValueChange = { profileViewModel.onTargetBeverageChanged(it) },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.profile_target_beverage,
                                    stringResource(Res.string.fluid_unit_milliliter)
                                )
                            )
                        },
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasBeverageError,
                        supportingText = {
                            if (uiState.error.hasBeverageError) {
                                uiState.error.beverageError?.let { Text(text = stringResource(it)) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputTargetWeight,
                        onValueChange = { profileViewModel.onTargetWeightChanged(it) },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.profile_target_weight,
                                    stringResource(uiState.currentSettings.weightUnit.toStringResource())
                                )
                            )
                        },
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasWeightError,
                        supportingText = {
                            if (uiState.error.hasWeightError) {
                                uiState.error.weightError?.let { Text(stringResource(it)) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = sleepDurationText(uiState.inputTargetSleepDuration),
                        onValueChange = {},
                        label = { Text(text = stringResource(Res.string.profile_target_sleep_duration)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                enabled = uiState.isEditing,
                                onClick = { showSleepDurationTimePicker = true }
                            ) {
                                Icon(
                                    painterResource(Res.drawable.ic_access_time),
                                    contentDescription = stringResource(Res.string.profile_target_sleep_duration_edit)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputTargetSteps,
                        onValueChange = { profileViewModel.onTargetStepsChanged(it) },
                        label = { Text(stringResource(Res.string.profile_target_steps)) },
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasStepsError,
                        supportingText = {
                            if (uiState.error.hasStepsError) {
                                uiState.error.stepsError?.let { Text(stringResource(it)) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.inputBodyHeight,
                        onValueChange = { profileViewModel.onBodyHeightChanged(it) },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.profile_body_height,
                                    stringResource(uiState.currentSettings.bodyMeasurementUnit.toStringResource())
                                )
                            )
                        },
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasHeightError,
                        supportingText = {
                            if (uiState.error.hasHeightError) {
                                uiState.error.heightError?.let { Text(stringResource(it)) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 72.dp)
                    )
                } else if (!uiState.isLoading) {
                    Text(
                        text = stringResource(Res.string.profile_no_profiles_prompt_add),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                    )
                }
            }
        }

        // FABs Management
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            AnimatedVisibility(
                visible = !uiState.isEditing,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    AnimatedVisibility(visible = uiState.currentProfileDisplay != null) {
                        FloatingActionButton(
                            onClick = { profileViewModel.startEditingCurrentProfile() },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_edit),
                                contentDescription = stringResource(Res.string.profile_content_description_edit)
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = { profileViewModel.startAddingNewProfile() },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription = stringResource(Res.string.profile_content_description_add)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = !uiState.isEditing,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                FloatingActionButton(
                    onClick = {
                        uiState.currentProfileDisplay?.id?.let {
                            profileViewModel.deleteProfile(it)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = stringResource(Res.string.profile_content_description_delete)
                    )
                }
            }

            SaveCancelFloatingActionButtonRow(
                onSave = { profileViewModel.saveProfile() },
                onCancel = { profileViewModel.cancelEditingOrAdding() },
                isEditing = uiState.isEditing,
                isLoading = uiState.isLoading,
                hasError = uiState.error.hasAnyFieldError,
                saveButtonContentDescription = Res.string.profile_content_description_save,
                cancelButtonContentDescription = Res.string.profile_content_description_cancel,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }

        if (showProfileDialog && uiState.allProfilesDisplay.isNotEmpty()) {
            ProfileSelectionDialog(
                profiles = uiState.allProfilesDisplay,
                selectedProfileId = uiState.currentProfileDisplay?.id,
                onProfileSelected = {
                    if (it.id != uiState.currentProfileDisplay?.id) {
                        profileViewModel.switchProfile(it.id)
                    }
                    showProfileDialog = false
                },
                onDismiss = { showProfileDialog = false }
            )
        }

        if (showDateOfBirthDialog) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        profileViewModel.onDateOfBirthChanged(
                            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                        )
                    }
                    showDateOfBirthDialog = false
                },
                onDismiss = { showDateOfBirthDialog = false }
            )
        }

        if (showSleepDurationTimePicker) {
            val initialTime = uiState.inputTargetSleepDuration ?: ProfileDefaults.SLEEP_DURATION
            TimeInputDialog(
                initialHour = initialTime.toInt() / 60,
                initialMinute = initialTime.toInt() % 60,
                onTimeSelected = { hour, minute ->
                    profileViewModel.onTargetSleepDurationChanged((hour * 60 + minute).toUInt())
                    showSleepDurationTimePicker = false
                },
                onDismiss = { showSleepDurationTimePicker = false }
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun sleepDurationText(sleepDuration: UInt?): String =
    "${
        sleepDuration?.div(60u)?.toString()?.padStart(2, '0') ?: "--"
    }:${sleepDuration?.mod(60u)?.toString()?.padStart(2, '0') ?: "--"}"