package org.darthacheron.fitbe.settings.import

import org.darthacheron.fitbe.health.components.DialogUiState
import org.darthacheron.fitbe.profile.Profile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ImportDialogUiState(
    val importPath: String = "",
    val profiles: List<Profile> = emptyList(),
    val selectedProfileId: Uuid? = null,
    val importProfile: Boolean = false
) : DialogUiState {
    override val canSave: Boolean = (selectedProfileId != null || importProfile) && importPath.isNotEmpty()
}
