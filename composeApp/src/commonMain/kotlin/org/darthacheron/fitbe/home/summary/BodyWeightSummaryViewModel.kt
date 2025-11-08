package org.darthacheron.fitbe.home.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class BodyWeightSummaryViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val bodyWeightRepository: BodyWeightRepository,
    private val weightUnitConverter: WeightUnitConverter,
) : ViewModel() {

    private val settingsFlow = settingsRepository.getSettingsFlow()

    private val targetWeight = profileRepository.getTargetValueFlow { it?.targetWeight }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val bodyWeightFlow =
        settingsRepository.getSettingsFlow().flatMapLatest { settings ->
            settings.selectedProfileId?.let {
                bodyWeightRepository.getBodyWeights(
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                    it
                )
            } ?: flowOf(emptyList())
        }.onStart {
//                isLoading.value = true
//                errorMessage.value = null
        }.catch {
//                isLoading.value = false
//                errorMessage.value = Res.string.beverages_daily_view_error_loading
            emit(emptyList())
        }.map { bodyWeights ->
//                isLoading.value = false
            bodyWeights
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val uiState: StateFlow<BodyWeightSummaryUiState> =
        combine(
            targetWeight,
            bodyWeightFlow,
            settingsFlow
        ) { targetWeight, bodyWeights, settings ->

            val lastWeight = bodyWeights.maxByOrNull { it.date }?.weightInKg

            BodyWeightSummaryUiState(
                lastWeight = weightUnitConverter.convert(lastWeight, WeightUnit.KG, settings.weightUnit),
                targetWeight = weightUnitConverter.convert(targetWeight, WeightUnit.KG, settings.weightUnit),
                weightUnit = settings.weightUnit,
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BodyWeightSummaryUiState())
}
