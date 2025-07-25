package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

class BeverageViewModel(
    private val repository: BeverageRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    val todayIntake = settingsRepository.getSettingsFlow().flatMapLatest {
        repository.getTodayBeverages(it.selectedProfileId!!)
    }

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