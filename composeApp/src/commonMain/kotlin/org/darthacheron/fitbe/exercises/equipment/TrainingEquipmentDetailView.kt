package org.darthacheron.fitbe.exercises.equipment

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_photo_library
import fitbe.composeapp.generated.resources.ic_remove
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_cancel_editing
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_default_equipment
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_delete
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_edit
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_image
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_remove_image
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_reset_to_default
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_save
import fitbe.composeapp.generated.resources.training_equipment_detail_content_description_select_image
import fitbe.composeapp.generated.resources.training_equipment_detail_name
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.darthacheron.fitbe.components.ImagePlaceholder
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

    val galleryLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = {
            if (it != null && uiState.isEditing) {
                viewModel.onImageUriChange(it.absolutePath())
            }
        }
    )

    LaunchedEffect(equipmentId) {
        viewModel.loadEquipment(equipmentId?.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 72.dp)
        ) {
            if (uiState.isLoading && uiState.equipmentId != null && !uiState.isEditing) {
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
                                imageResource = getEquipmentImage(uiState.imageUri, uiState.default),
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.training_equipment_detail_content_description_image),
                                defaultContentDescription = stringResource(Res.string.training_equipment_detail_content_description_default_equipment),
                                modifier = Modifier.size(256.dp).align(Alignment.Center)
                            )
                            if (uiState.isEditing) {
                                IconButton(
                                    onClick = { viewModel.onImageUriChange(null) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    enabled = uiState.isEditing
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_remove),
                                        contentDescription = stringResource(Res.string.training_equipment_detail_content_description_remove_image)
                                    )
                                }
                            }
                        } else {
                            ImagePlaceholder(
                                isEditing = uiState.isEditing,
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.training_equipment_detail_content_description_default_equipment)
                            )
                        }
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { galleryLauncher.launch() },
                                modifier = Modifier.align(Alignment.BottomStart),
                                enabled = uiState.isEditing
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_photo_library),
                                    contentDescription = stringResource(Res.string.training_equipment_detail_content_description_select_image)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = getEquipmentName(uiState.name, uiState.default),
                        onValueChange = { if (uiState.isEditing) viewModel.onNameChange(it) },
                        label = { Text(stringResource(Res.string.training_equipment_detail_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasNameError,
                        supportingText = {
                            if (uiState.error.hasNameError) {
                                Text(stringResource(uiState.error.nameError!!))
                            }
                        }
                    )

                    if (uiState.error.hasGeneralError) {
                        Text(
                            text = stringResource(uiState.error.generalError!!),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.isEditing && uiState.equipmentId != null && uiState.default && uiState.isModifiedFromPersistedDefault,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            FloatingActionButton(
                onClick = { viewModel.resetEquipmentToDefault() },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_default),
                    contentDescription = stringResource(Res.string.training_equipment_detail_content_description_reset_to_default)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(!uiState.isEditing && uiState.equipmentId != null && !uiState.default) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.deleteEquipment()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = stringResource(Res.string.training_equipment_detail_content_description_delete)
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
            AnimatedVisibility(uiState.isEditing && uiState.equipmentId != null && !uiState.isModifiedFromPersistedDefault) {
                FloatingActionButton(
                    onClick = {
                        viewModel.loadEquipment(uiState.equipmentId.toString()) // Reload original state
                        viewModel.setEditing(false)
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = stringResource(Res.string.training_equipment_detail_content_description_cancel_editing)
                    )
                }
            }

            AnimatedVisibility(uiState.isEditing) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading && !uiState.error.hasError) {
                            viewModel.saveEquipment()
                        }
                    },
                    containerColor = if (!uiState.isLoading && !uiState.error.hasError) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.12f
                    ),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.training_equipment_detail_content_description_save)
                    )
                }
            }

            AnimatedVisibility(!uiState.isEditing && uiState.equipmentId != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setEditing(true)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = stringResource(Res.string.training_equipment_detail_content_description_edit)
                    )
                }
            }
        }
    }
}
