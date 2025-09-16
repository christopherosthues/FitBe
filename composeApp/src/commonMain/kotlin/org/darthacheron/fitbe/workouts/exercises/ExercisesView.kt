package org.darthacheron.fitbe.workouts.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_content_description_card_add_favorite
import fitbe.composeapp.generated.resources.exercise_content_description_card_remove_favorite
import fitbe.composeapp.generated.resources.exercise_content_description_add
import fitbe.composeapp.generated.resources.exercise_content_description_card
import fitbe.composeapp.generated.resources.exercise_content_description_default_exercise
import fitbe.composeapp.generated.resources.exercise_no_exercises
import fitbe.composeapp.generated.resources.filter_exercises_label
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import org.darthacheron.fitbe.components.ImageWithDefault
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

// Helper data class for processing within the Composable
data class DisplayableExercise(
    val exercise: Exercise,
    val localizedName: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun ExercisesView(
    exercisesViewModel: ExercisesViewModel,
) {
    LaunchedEffect(Unit) {
        exercisesViewModel.updateTopBarConfig()
    }
    val rawExercises by exercisesViewModel.rawExercises.collectAsState()
    val favoriteExerciseIds by exercisesViewModel.favoriteExerciseIds.collectAsState()
    val filterText by exercisesViewModel.filterText.collectAsState()

    val localizedExercises = rawExercises.map {
        DisplayableExercise(
            exercise = it,
            localizedName = getExerciseName(it.name, it.default) // Composable call
        )
    }
    val processedExercises = remember(rawExercises, filterText, favoriteExerciseIds) {
        val filteredList = if (filterText.isBlank()) {
            localizedExercises
        } else {
            localizedExercises.filter {
                it.localizedName.contains(filterText, ignoreCase = true)
            }
        }

        filteredList.sortedWith(
            compareByDescending<DisplayableExercise> { favoriteExerciseIds.contains(it.exercise.id) }
                .thenBy { it.localizedName }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = filterText,
                onValueChange = { exercisesViewModel.onFilterTextChanged(it) },
                label = { Text(stringResource(Res.string.filter_exercises_label)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            if (processedExercises.isEmpty()) {
                Text(text = stringResource(Res.string.exercise_no_exercises))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(processedExercises.size, key = { processedExercises[it].exercise.id.toString() }) { index ->
                        val displayableExercise = processedExercises[index]
                        val isFavorite = favoriteExerciseIds.contains(displayableExercise.exercise.id)
                        ExerciseCard(
                            exercise = displayableExercise.exercise,
                            localizedName = displayableExercise.localizedName, // Pass localized name
                            isFavorite = isFavorite,
                            onToggleFavorite = { exercisesViewModel.toggleFavorite(displayableExercise.exercise.id) },
                            onClick = { exercisesViewModel.navigateToExerciseDetail(displayableExercise.exercise.id) },
                            contentDescription = stringResource(
                                Res.string.exercise_content_description_card,
                                displayableExercise.localizedName // Use localized name for content description
                            )
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { exercisesViewModel.navigateToExerciseDetail(null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.exercise_content_description_add)
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    localizedName: String, // Use passed localized name
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .width(200.dp)
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ImageWithDefault(
                imageUri = exercise.imageUri,
                imageResource = getExerciseImage(exercise.imageUri, exercise.default),
                default = exercise.default,
                contentDescription = null,
                defaultContentDescription = stringResource(Res.string.exercise_content_description_default_exercise),
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(if (isFavorite) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border),
                    contentDescription = stringResource(if (isFavorite) Res.string.exercise_content_description_card_remove_favorite else Res.string.exercise_content_description_card_add_favorite),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp,
                                topStart = 0.dp,
                                topEnd = 0.dp
                            )
                        )
                        .background(Color.Black.copy(alpha = 0.6f))
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = localizedName, // Use localized name
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                            color = Color.White
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val chipColors = SuggestionChipDefaults.suggestionChipColors()
                            exercise.targetMuscleGroups.take(2).forEach {
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(text = stringResource(it.localizedString())) },
                                    enabled = false,
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = chipColors.containerColor,
                                        disabledLabelColor = chipColors.labelColor,
                                    ),
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
