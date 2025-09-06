package org.darthacheron.fitbe.exercises.equipment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_edit_training_equipment_button_select_image
import fitbe.composeapp.generated.resources.add_edit_training_equipment_image_content_description
import fitbe.composeapp.generated.resources.add_edit_training_equipment_label_name
import fitbe.composeapp.generated.resources.add_edit_training_equipment_reset_to_default
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
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
fun TrainingEquipmentDetailView(
    equipmentId: Uuid?,
    viewModel: TrainingEquipmentDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isInEditMode by remember { mutableStateOf(equipmentId == null) }

    val galleryLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = {
            if (it != null && isInEditMode) {
                viewModel.onImageUriChange(it.absolutePath())
            }
        }
    )

    LaunchedEffect(equipmentId) {
        viewModel.loadEquipment(equipmentId?.toString())
        isInEditMode = (equipmentId == null)
    }

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
        viewModel.saveCompletedEvent.collect {
            isInEditMode = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 72.dp)
        ) {
            if (uiState.isLoading && uiState.equipmentId != null && !isInEditMode) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                            if (isInEditMode) {
                                IconButton(
                                    onClick = { viewModel.onImageUriChange(null) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    enabled = isInEditMode
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_remove),
                                        contentDescription = "Remove image" // TODO: String resource
                                    )
                                }
                            }
                        } else {
                            ImagePlaceholder(uiState = uiState)
                        }
                        if (isInEditMode) {
                            IconButton(
                                onClick = { galleryLauncher.launch() },
                                modifier = Modifier.align(Alignment.BottomStart),
                                enabled = isInEditMode
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_photo_library),
                                    contentDescription = stringResource(Res.string.add_edit_training_equipment_button_select_image)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = getEquipmentName(uiState.name, uiState.default),
                        onValueChange = { if (isInEditMode) viewModel.onNameChange(it) },
                        label = { Text(stringResource(Res.string.add_edit_training_equipment_label_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !isInEditMode,
                        isError = uiState.error != null && uiState.name.isBlank(),
                        supportingText = { 
                            if (uiState.error != null && uiState.name.isBlank()) Text(uiState.error!!) 
                        }
                    )

                    if (uiState.error != null && !(uiState.name.isBlank() && isInEditMode)) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isInEditMode && uiState.equipmentId != null && uiState.default && uiState.isModifiedFromPersistedDefault,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            FloatingActionButton(
                onClick = { viewModel.resetEquipmentToDefault() },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_default),
                    contentDescription = stringResource(Res.string.add_edit_training_equipment_reset_to_default)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(!isInEditMode && uiState.equipmentId != null && !uiState.default) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.deleteEquipment()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = "Delete Equipment" // TODO: stringResource(Res.string.delete_equipment_content_description)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(isInEditMode && uiState.equipmentId != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.loadEquipment(uiState.equipmentId.toString())
                        isInEditMode = false
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = "Cancel" // TODO: stringResource(Res.string.cancel_editing_content_description)
                    )
                }
            }

            AnimatedVisibility(isInEditMode) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.saveEquipment()
                        }
                    },
                    containerColor = if (!uiState.isLoading) MaterialTheme.colorScheme.primary else Color.Gray,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.profile_save)
                    )
                }
            }

            AnimatedVisibility(!isInEditMode && uiState.equipmentId != null) {
                FloatingActionButton(
                    onClick = {
                        isInEditMode = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = /*Res.string.edit_equipment_content_description*/ "Edit Equipment"
                    )
                }
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
            contentDescription = null, 
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
                modifier = Modifier.align(Alignment.Center).padding(16.dp),
                color = Color.White
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
