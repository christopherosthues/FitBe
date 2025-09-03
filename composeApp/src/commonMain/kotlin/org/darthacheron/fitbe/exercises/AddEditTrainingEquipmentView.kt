package org.darthacheron.fitbe.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_select_image
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_take_photo
import fitbe.composeapp.generated.resources.add_edit_training_equipment_fab_save_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_image_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_label_name
import fitbe.composeapp.generated.resources.ic_photo_camera
import fitbe.composeapp.generated.resources.ic_photo_library
import fitbe.composeapp.generated.resources.ic_save
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.painterResource
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
    val scrollState = rememberScrollState()
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = {
            if (it != null) {
                viewModel.onImageUriChange(it.absolutePath())
            }
        }
    )

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
//    val imagePickerLauncher = rememberImagePickerLauncher { imageUri ->
//        viewModel.onImageUriChange(imageUri)
//    }

    Box(
        modifier = Modifier
            .verticalScroll(scrollState)
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
                    isError = uiState.error != null,
                    supportingText = { if (uiState.error != null) Text(uiState.error!!) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Image display (KMP compatible)
                if (uiState.imageUri != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = PlatformFile(uiState.imageUri.toString()),
                            contentDescription = stringResource(Res.string.add_edit_training_equipment_image_content_description),
                            modifier = Modifier.size(128.dp),
                        )
                        Text("Image URI: ${uiState.imageUri}") // Placeholder
                    }
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            // TODO: Use Calf (https://github.com/MohamedRejeb/Calf) or FileKit (https://github.com/vinceglb/FileKit) for file picker
                            launcher.launch()
                                  },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(painter = painterResource(Res.drawable.ic_photo_library), contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text(stringResource(Res.string.add_edit_training_equipment_button_select_image))
                    }

                    Button(
                        onClick = {
                            // TODO: Use CameraK (https://github.com/Kashif-E/CameraK) to access camera
                            // imagePickerLauncher.launch(ImagePicker.MediaType.CAMERA)
                            },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(painter = painterResource(Res.drawable.ic_photo_camera), contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text(stringResource(Res.string.add_edit_training_equipment_button_take_photo))
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // Pushes save button to the bottom

                Button(
                    onClick = { viewModel.saveEquipment() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading // Disable button while loading/saving
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = null, // Content description provided by text
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(Res.string.add_edit_training_equipment_fab_save_content_description))
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!, // Should be a string resource
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
