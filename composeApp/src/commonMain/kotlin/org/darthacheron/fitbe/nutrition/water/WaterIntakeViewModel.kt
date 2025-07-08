package org.darthacheron.fitbe.nutrition.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterIntakeViewModel(private val repository: WaterIntakeRepository) : ViewModel() {
    val todayIntake = repository.todayIntake.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun setIntake(waterIntake: WaterIntake?, amount: Int) {
        viewModelScope.launch {
            repository.setIntake((waterIntake?.amount ?: 0) + amount)
        }
    }
}