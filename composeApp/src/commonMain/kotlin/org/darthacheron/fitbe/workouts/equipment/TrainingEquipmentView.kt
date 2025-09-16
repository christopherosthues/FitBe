package org.darthacheron.fitbe.workouts.equipment

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.filter_equipment_label
import fitbe.composeapp.generated.resources.training_equipment_content_description_add
import fitbe.composeapp.generated.resources.training_equipment_content_description_card
import fitbe.composeapp.generated.resources.training_equipment_content_description_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_no_equipments
import fitbe.composeapp.generated.resources.training_equipment_content_description_card_add_favorite
import fitbe.composeapp.generated.resources.training_equipment_content_description_card_remove_favorite
import org.darthacheron.fitbe.components.ImageWithDefault
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Helper data class for processing within the Composable
data class DisplayableTrainingEquipment(
    val equipment: TrainingEquipment,
    val localizedName: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun TrainingEquipmentView(
    viewModel: TrainingEquipmentViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }
    val rawEquipmentList by viewModel.rawEquipmentList.collectAsState()
    val favoriteEquipmentIds by viewModel.favoriteEquipmentIds.collectAsState()
    val filterText by viewModel.filterText.collectAsState()

    val localizedList = rawEquipmentList.map {
        DisplayableTrainingEquipment(
            equipment = it,
            localizedName = getEquipmentName(it.name, it.default) // Composable call
        )
    }
    // Derived list for display: localized, filtered, and sorted
    val processedEquipmentList = remember(rawEquipmentList, filterText, favoriteEquipmentIds) {
        val filteredList = if (filterText.isBlank()) {
            localizedList
        } else {
            localizedList.filter {
                it.localizedName.contains(filterText, ignoreCase = true)
            }
        }

        filteredList.sortedWith(
            compareByDescending<DisplayableTrainingEquipment> { favoriteEquipmentIds.contains(it.equipment.id) }
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
                label = { Text(stringResource(Res.string.filter_equipment_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (processedEquipmentList.isEmpty()) {
                Text(text = stringResource(Res.string.training_equipment_no_equipments))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(processedEquipmentList.size, key = { processedEquipmentList[it].equipment.id.toString() }) { index ->
                        val displayableEquipment = processedEquipmentList[index]
                        val isFavorite = displayableEquipment.equipment.id in favoriteEquipmentIds
                        TrainingEquipmentCard(
                            equipment = displayableEquipment.equipment,
                            localizedName = displayableEquipment.localizedName, // Pass localized name
                            isFavorite = isFavorite,
                            onAddFavorite = { viewModel.addFavorite(displayableEquipment.equipment.id) },
                            onRemoveFavorite = { viewModel.removeFavorite(displayableEquipment.equipment.id) },
                            onClick = { viewModel.navigateToTrainingEquipmentDetail(displayableEquipment.equipment.id) },
                            modifier = Modifier
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.navigateToTrainingEquipmentDetail(null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.training_equipment_content_description_add)
            )
        }
    }
}

@Composable
fun TrainingEquipmentCard(
    equipment: TrainingEquipment,
    localizedName: String, // Use passed localized name
    isFavorite: Boolean,
    onAddFavorite: () -> Unit,
    onRemoveFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardContentDescription = stringResource(
        Res.string.training_equipment_content_description_card,
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
                imageUri = equipment.imageUri,
                imageResource = getEquipmentImage(equipment.imageUri, equipment.default),
                default = equipment.default,
                contentDescription = null, // Image is decorative here, card has main content description
                defaultContentDescription = stringResource(Res.string.training_equipment_content_description_default_equipment),
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { if (isFavorite) onRemoveFavorite() else onAddFavorite() },
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(Res.drawable.ic_favorite) else painterResource(Res.drawable.ic_favorite_border),
                    contentDescription = stringResource(if (isFavorite) Res.string.training_equipment_content_description_card_remove_favorite else Res.string.training_equipment_content_description_card_add_favorite),
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
                    Text(
                        text = localizedName, // Display localized name
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
