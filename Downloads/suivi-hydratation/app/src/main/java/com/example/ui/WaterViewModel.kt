package com.example.suivihydratation.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WaterDatabase
import com.example.data.WaterIntake
import com.example.data.WaterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class WaterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WaterRepository
    val todayDate: String = LocalDate.now().toString()
    val targetMl = 2000

    init {
        val database = WaterDatabase.getDatabase(application)
        repository = WaterRepository(database.waterIntakeDao())
    }

    val todayIntake: StateFlow<WaterIntake> = repository.getIntakeByDate(todayDate)
        .map { it ?: WaterIntake(todayDate, 0) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WaterIntake(todayDate, 0)
        )

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            val currentAmount = todayIntake.value.amountMl
            val newAmount = (currentAmount + amountMl).coerceIn(0, 10000)
            repository.saveIntake(WaterIntake(todayDate, newAmount))
        }
    }

    fun resetWater() {
        viewModelScope.launch {
            repository.saveIntake(WaterIntake(todayDate, 0))
        }
    }
}
