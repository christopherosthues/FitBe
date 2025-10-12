package org.darthacheron.fitbe.health.sleep

import kotlinx.coroutines.flow.StateFlow
import org.darthacheron.fitbe.health.components.DailyViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource

class SleepDailyViewModel(
    private val repository: SleepRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : DailyViewModel<SleepDailyError, SleepDailyUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = TODO("Not yet implemented")

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val addButtonContentDescription: StringResource
        get() = TODO("Not yet implemented")

    override val uiState: StateFlow<SleepDailyUiState>
        get() = TODO("Not yet implemented")
}