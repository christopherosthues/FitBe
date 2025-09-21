package org.darthacheron.fitbe.workouts.programs


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ProgramOverviewView(programOverviewViewModel: ProgramOverviewViewModel) {
    LaunchedEffect(Unit) {
        programOverviewViewModel.updateTopBarConfig()
    }
}

