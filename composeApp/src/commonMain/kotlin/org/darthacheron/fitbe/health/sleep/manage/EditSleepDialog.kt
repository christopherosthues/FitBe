package org.darthacheron.fitbe.health.sleep.manage

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun EditSleepDialog(
    viewModel: EditSleepDialogViewModel = koinViewModel(),
    id: Uuid,
    onSave: (id: Uuid, start: Instant, end: Instant) -> Any,
    onDismiss: () -> Any
) {

}