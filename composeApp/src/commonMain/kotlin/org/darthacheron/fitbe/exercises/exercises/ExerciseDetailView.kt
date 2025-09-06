package org.darthacheron.fitbe.exercises.exercises


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ExerciseDetailView(
    exerciseId: Uuid?,
    exerciseDetailViewModel: ExerciseDetailViewModel,
) {
    LaunchedEffect(Unit) {
        exerciseDetailViewModel.updateTopBarConfig()
    }

}

