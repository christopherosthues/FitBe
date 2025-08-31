package org.darthacheron.fitbe.exercises

import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_select_image
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_take_photo
import fitbe.composeapp.generated.resources.add_edit_training_equipment_fab_save_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_image_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_label_name
// Removed title string imports as TopAppBar is removed
import org.darthacheron.fitbe.composables.ImagePicker // Expecting this to be a KMP compatible image picker
import org.darthacheron.fitbe.composables.rememberImagePickerLauncher // Expecting this
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun AddEditTrainingEquipmentView(
    equipmentId: Uuid?,
    viewModel: AddEditTrainingEquipmentViewModel,
    navHostController: NavHostController // Kept for navigateBackEvent, though TopAppBar back is gone
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(equipmentId) {
        viewModel.loadEquipment(equipmentId?.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collect {
            navHostController.popBackStack() // Still needed for programmatic back navigation
        }
    }

    // Common KMP image picker launcher
    // TODO: Use CameraK for taking pictures and FileKit for selecting files
    val imagePickerLauncher = rememberImagePickerLauncher { imageUri ->
        viewModel.onImageUriChange(imageUri)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Apply padding directly to the content Box
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text(stringResource(Res.string.add_edit_training_equipment_label_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.nameError != null
                )
                if (uiState.nameError != null) {
                    Text(
                        text = uiState.nameError!!, // Should be a string resource ideally
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Image display (KMP compatible)
                if (uiState.imageUri != null) {
                    // This is a placeholder. For real KMP image loading from URI:
                    // implementation("io.coil-kt:coil-compose:VERSION")
                    // then use an AsyncImage composable.
                    Text("Image URI: ${uiState.imageUri}") // Placeholder
                } else {
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(Res.string.add_edit_training_equipment_image_content_description))
                    }
                }

                Button(
                    onClick = { imagePickerLauncher.launch(ImagePicker.MediaType.GALLERY) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text(stringResource(Res.string.add_edit_training_equipment_button_select_image))
                }

                Button(
                    onClick = { imagePickerLauncher.launch(ImagePicker.MediaType.CAMERA) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text(stringResource(Res.string.add_edit_training_equipment_button_take_photo))
                }

                Spacer(modifier = Modifier.weight(1f)) // Pushes save button to the bottom

                Button(
                    onClick = { viewModel.saveEquipment() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading // Disable button while loading/saving
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null, // Content description provided by text
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(Res.string.add_edit_training_equipment_fab_save_content_description))
                }

                if (uiState.saveError != null) {
                    Text(
                        text = uiState.saveError!!, // Should be a string resource
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
