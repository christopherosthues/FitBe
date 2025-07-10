package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BeverageViewModel(private val repository: BeverageRepository) : ViewModel() {
    val todayIntake = repository.todayDrinks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        listOf()
    )

    fun setIntake(amount: Int, unit: FluidUnit, beverage: String) {
        viewModelScope.launch {
            repository.addDrink(amount = amount, beverage =  beverage, unit = unit)
        }
    }
}