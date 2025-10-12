package org.darthacheron.fitbe.health.components

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.daily_view_content_description_move_future
import fitbe.composeapp.generated.resources.daily_view_content_description_move_past
import fitbe.composeapp.generated.resources.overview_content_description_move_future
import fitbe.composeapp.generated.resources.overview_content_description_move_past
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.FitBeViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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

    abstract val addButtonContentDescription: StringResource

    @Composable
    fun movePastContentDescription(): String {
        return stringResource(Res.string.daily_view_content_description_move_past)
    }

    @Composable
    fun moveFutureContentDescription(): String {
        return stringResource(Res.string.daily_view_content_description_move_future)
    }

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