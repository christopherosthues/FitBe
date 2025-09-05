package org.darthacheron.fitbe.exercises

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_select_image
import fitbe.composeapp.generated.resources.add_edit_training_equipment_image_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_label_name
import fitbe.composeapp.generated.resources.add_edit_training_equipment_reset_to_default
import fitbe.composeapp.generated.resources.ic_delete
// import fitbe.composeapp.generated.resources.ic_delete // Assuming you will add this
import fitbe.composeapp.generated.resources.ic_launcher
import fitbe.composeapp.generated.resources.ic_photo_library
import fitbe.composeapp.generated.resources.ic_remove
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.ic_verified
import fitbe.composeapp.generated.resources.profile_save
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.darthacheron.fitbe.components.ImageWithDefault
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun AddEditTrainingEquipmentView(
    equipmentId: Uuid?,
    viewModel: AddEditTrainingEquipmentViewModel,
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val galleryLauncher = rememberFilePickerLauncher(
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
            navHostController.popBackStack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 64.dp) // Added padding for FAB
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
                    Box {
                        if (uiState.imageUri != null) {
                            ImageWithDefault(
                                imageUri = uiState.imageUri,
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.add_edit_training_equipment_image_content_description),
                                modifier = Modifier.size(256.dp).align(Alignment.Center)
                            )
                            IconButton(
                                onClick = { viewModel.onImageUriChange(null) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_remove),
                                    contentDescription = "Remove image" // TODO: String resource
                                )
                            }
                        } else {
                            ImagePlaceholder(uiState)
                        }
                        IconButton(
                            onClick = { galleryLauncher.launch() },
                            modifier = Modifier.align(Alignment.BottomStart)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_photo_library),
                                contentDescription = stringResource(Res.string.add_edit_training_equipment_button_select_image)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = getLocalizedName(uiState.name, uiState.default),
                        onValueChange = { viewModel.onNameChange(it) },
                        label = { Text(stringResource(Res.string.add_edit_training_equipment_label_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.error != null,
                        supportingText = { if (uiState.error != null) Text(uiState.error!!) }
                    )

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

        if (uiState.isEditing && uiState.default) {
            FloatingActionButton(
                onClick = { viewModel.resetEquipmentToDefault() },
                containerColor = Color.Red, // Consider a less aggressive color or theme color
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_default),
                    contentDescription = stringResource(Res.string.add_edit_training_equipment_reset_to_default)
                )
            }
        }

        // Save FAB
        FloatingActionButton(
            onClick = {
                if (!uiState.isLoading && uiState.error == null) {
                    viewModel.saveEquipment()
                }
            },
            containerColor = if (!uiState.isLoading && uiState.error == null) Color(0xFF2196F3) else Color.Gray,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_save),
                contentDescription = stringResource(Res.string.profile_save)
            )
        }

        // Delete FAB - New
        if (uiState.isEditing && !uiState.default) {
            FloatingActionButton(
                onClick = {
                    if (!uiState.isLoading) {
                        viewModel.deleteEquipment()
                    }
                },
                containerColor = Color.Red, // Consider a less aggressive color or theme color
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Delete Equipment" // TODO: Replace with stringResource(Res.string.delete_equipment_content_description)
                )
            }
        }
    }
}

@Composable
private fun ImagePlaceholder(uiState: AddEditTrainingEquipmentUiState) {
    Box(
        modifier = Modifier
            .size(256.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_launcher),
            contentDescription = null, // Decorative
            modifier = Modifier.size(256.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.6f))
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(Res.string.add_edit_training_equipment_image_content_description),
                modifier = Modifier.align(
                    Alignment.Center
                ).padding(16.dp)
            )
        }
        if (uiState.default) {
            Icon(
                painter = painterResource(Res.drawable.ic_verified),
                contentDescription = "Default equipment", // TODO: String resource
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
