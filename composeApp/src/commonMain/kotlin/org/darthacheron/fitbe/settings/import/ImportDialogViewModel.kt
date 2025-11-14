package org.darthacheron.fitbe.settings.import

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.darthacheron.fitbe.health.components.DialogViewModel
import org.darthacheron.fitbe.profile.Profile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ImportDialogViewModel : DialogViewModel<ImportDialogUiState>() {
    override val uiState = MutableStateFlow(ImportDialogUiState())

    override fun dismissDialog() {
        uiState.update { ImportDialogUiState() }
    }

    fun onImportPathChanged(importPath: String) {
        uiState.update { it.copy(importPath = importPath) }
    }

    fun onProfilesLoaded(profiles: List<Profile>) {
        uiState.update { it.copy(profiles = profiles) }
    }

    fun onProfileSelected(profileId: Uuid) {
        uiState.update { it.copy(selectedProfileId = profileId, importProfile = false) }
    }

    fun onImportProfileChanged(createNew: Boolean) {
        uiState.update { it.copy(importProfile = createNew, selectedProfileId = null) }
    }
}
