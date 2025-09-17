package org.darthacheron.fitbe.workouts.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.workout_template_content_description_add
import fitbe.composeapp.generated.resources.workout_template_content_description_card
import fitbe.composeapp.generated.resources.workout_template_content_description_card_add_favorite
import fitbe.composeapp.generated.resources.workout_template_content_description_card_remove_favorite
import fitbe.composeapp.generated.resources.workout_template_content_description_default_workout_template
import fitbe.composeapp.generated.resources.workout_template_exercises
import fitbe.composeapp.generated.resources.workout_template_filter_label
import fitbe.composeapp.generated.resources.workout_template_no_filtered_templates
import fitbe.composeapp.generated.resources.workout_template_no_templates
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.components.ImageWithDefault
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

data class DisplayableWorkoutTemplate(
    val workoutTemplate: WorkoutTemplate,
    val localizedName: String
)

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WorkoutTemplatesOverviewView(viewModel: WorkoutTemplatesOverviewViewModel) {
    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }
    val uiState by viewModel.uiState.collectAsState()
    val filterText by viewModel.filterText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentErrorMessage: StringResource? =
        uiState.workoutTemplateListError ?: uiState.favoriteStateError

    currentErrorMessage?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearErrorMessage()
            }
        }
    }

    val localizedList: List<DisplayableWorkoutTemplate> = uiState.rawWorkoutTemplateList.map {
        DisplayableWorkoutTemplate(
            workoutTemplate = it,
            localizedName = getWorkoutName(it.name, it.default)
        )
    }

    val processedWorkoutTemplateList: List<DisplayableWorkoutTemplate> =
        remember(uiState.rawWorkoutTemplateList, filterText, uiState.favoriteWorkoutTemplateIds) {
            val filtered = if (filterText.isBlank()) {
                localizedList
            } else {
                localizedList.filter {
                    it.localizedName.contains(filterText, ignoreCase = true)
                }
            }

            filtered.sortedWith(
                compareByDescending<DisplayableWorkoutTemplate> {
                    uiState.favoriteWorkoutTemplateIds.contains(
                        it.workoutTemplate.id
                    )
                }
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
                onValueChange = { viewModel.onFilterTextChanged(it) },
                label = { Text(stringResource(Res.string.workout_template_filter_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (processedWorkoutTemplateList.isEmpty() && filterText.isNotBlank()) {
                Text(text = stringResource(Res.string.workout_template_no_filtered_templates))
            } else if (uiState.rawWorkoutTemplateList.isEmpty() && uiState.workoutTemplateListError == null) {
                Text(text = stringResource(Res.string.workout_template_no_templates))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp) // Add padding for FAB
                ) {
                    items(
                        processedWorkoutTemplateList.size,
                        key = { processedWorkoutTemplateList[it].workoutTemplate.id }) { templateIndex ->
                        val displayableTemplate = processedWorkoutTemplateList[templateIndex]
                        val isFavorite =
                            uiState.favoriteWorkoutTemplateIds.contains(displayableTemplate.workoutTemplate.id)
                        WorkoutTemplateCard(
                            workoutTemplate = displayableTemplate.workoutTemplate,
                            localizedName = displayableTemplate.localizedName,
                            isFavorite = isFavorite,
                            onAddFavorite = { viewModel.addFavorite(displayableTemplate.workoutTemplate.id) },
                            onRemoveFavorite = { viewModel.removeFavorite(displayableTemplate.workoutTemplate.id) },
                            onClick = {
                                viewModel.navigateToWorkoutTemplateDetail(
                                    displayableTemplate.workoutTemplate.id
                                )
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.navigateToWorkoutTemplateDetail(null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.workout_template_content_description_add)
            )
        }
    }
}

@Composable
fun WorkoutTemplateCard(
    workoutTemplate: WorkoutTemplate,
    localizedName: String, // Use passed localized name
    isFavorite: Boolean,
    onAddFavorite: () -> Unit,
    onRemoveFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardContentDescription = stringResource(
        Res.string.workout_template_content_description_card,
        localizedName // Use localized name for content description
    )

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .width(200.dp)
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = cardContentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ImageWithDefault(
                imageUri = workoutTemplate.imageUri,
                imageResource = getWorkoutImage(workoutTemplate.imageUri, workoutTemplate.default),
                default = workoutTemplate.default,
                contentDescription = null,
                defaultContentDescription = stringResource(Res.string.workout_template_content_description_default_workout_template),
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { if (isFavorite) onRemoveFavorite() else onAddFavorite() },
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(Res.drawable.ic_favorite) else painterResource(
                        Res.drawable.ic_favorite_border
                    ),
                    contentDescription = stringResource(if (isFavorite) Res.string.workout_template_content_description_card_remove_favorite else Res.string.workout_template_content_description_card_add_favorite),
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = localizedName,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp) // Adjusted padding
                        )
                        val chipColors = SuggestionChipDefaults.suggestionChipColors()
                        SuggestionChip(
                            onClick = { }, // Non-interactive, for display only
                            label = {
                                Text(
                                    text = stringResource(
                                        Res.string.workout_template_exercises,
                                        workoutTemplate.exercises.size
                                    )
                                )
                            },
                            enabled = false, // Visually appears as a chip but not interactive
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = chipColors.containerColor,
                                disabledLabelColor = chipColors.labelColor,
                            ),
                            modifier = Modifier.height(24.dp) // Adjust height as needed
                        )
                    }
                }
            }
        }
    }
}
