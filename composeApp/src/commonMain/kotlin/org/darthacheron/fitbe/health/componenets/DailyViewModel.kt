package org.darthacheron.fitbe.health.componenets

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.days

abstract class DailyViewModel<Error : UiStateError, State : UiState<Error>>(
    protected val settingsRepository: SettingsRepository,
    topBarManager: TopBarManager
) : FitBeViewModel(topBarManager) {
    protected val date =
        MutableStateFlow(
            Clock.System
                .now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .toInstant(TimeZone.currentSystemDefault())
        )

    val dateFlow: StateFlow<Instant> = date

    protected val isLoading = MutableStateFlow(true)
    protected val errorMessage = MutableStateFlow<StringResource?>(null)

    abstract val uiState: StateFlow<State>

    fun movePast() {
        val range = dateFlow.value.minus(1.days)
        setDate(range)
    }

    fun moveFuture() {
        val range = dateFlow.value.plus(1.days)
        setDate(range)
    }

    fun setDate(dateInstant: Instant) {
        date.value = dateInstant
    }

    fun clearErrorMessage() {
        errorMessage.value = null
    }
}