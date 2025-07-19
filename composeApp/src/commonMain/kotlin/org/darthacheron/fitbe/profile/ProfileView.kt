package org.darthacheron.fitbe.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.DropdownSelection
import org.jetbrains.compose.resources.stringResource
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_name
import fitbe.composeapp.generated.resources.profile_switch
import fitbe.composeapp.generated.resources.profile_target_beverage
import fitbe.composeapp.generated.resources.profile_target_kcal
import fitbe.composeapp.generated.resources.profile_target_sleep_hours
import fitbe.composeapp.generated.resources.profile_target_sleep_minutes
import fitbe.composeapp.generated.resources.profile_target_steps
import fitbe.composeapp.generated.resources.profile_target_weight
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val profiles by profileViewModel.profiles.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (!profiles.isEmpty()) {
            val selectedIndex = profiles.indexOf(currentProfile)
            DropdownSelection(
                initialState = false,
                items = profiles,
                selectedIndex = if (selectedIndex != -1) selectedIndex else 0,
                title = stringResource(Res.string.profile_switch),
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
}

@Composable
fun ProfileDetails(profile: Profile) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "${stringResource(Res.string.profile_name)}: ${profile.name}")
        Text(text = "${stringResource(Res.string.profile_target_kcal)}: ${profile.targetKcal}")
        Text(text = "${stringResource(Res.string.profile_target_beverage)}: ${profile.targetBeverageInMilliliter}")
        Text(text = "${stringResource(Res.string.profile_target_weight)}: ${profile.targetWeight}")
        Text(text = "${stringResource(Res.string.profile_target_sleep_hours)}: ${profile.targetSleepHours}")
        Text(text = "${stringResource(Res.string.profile_target_sleep_minutes)}: ${profile.targetSleepMinutes}")
        Text(text = "${stringResource(Res.string.profile_target_steps)}: ${profile.targetSteps}")
    }
}