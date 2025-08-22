package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class BeverageViewModel(
    private val repository: BeverageRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val targetSteps: StateFlow<UInt> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetSteps ?: ProfileDefaults.STEPS }
            } else {
                flowOf(ProfileDefaults.STEPS)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.STEPS)

    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    val todayIntake: Flow<List<Beverage>> = settingsRepository.getSettingsFlow().flatMapLatest {
        repository.getTodayBeverages(it.selectedProfileId!!)
    }

    val todayProgress: StateFlow<Double> = combine(todayIntake, targetSteps) {
        todayIntake, targetSteps ->
        todayIntake.sumOf { it.unit.toMilliliter(it.amount) } * 100 / targetSteps.toDouble()
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    @OptIn(ExperimentalUuidApi::class)
    fun addBeverage(amount: UInt, unit: FluidUnit, beverage: String) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            repository.addBeverage(
                amount = amount.toInt(),
                beverage = beverage,
                unit = unit,
                profileId = settings.selectedProfileId!!
            )
        }
    }
}