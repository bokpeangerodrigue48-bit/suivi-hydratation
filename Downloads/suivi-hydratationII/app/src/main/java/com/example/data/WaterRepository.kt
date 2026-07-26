package com.example.data

import kotlinx.coroutines.flow.Flow

class WaterRepository(private val waterIntakeDao: WaterIntakeDao) {
    fun getIntakeByDate(date: String): Flow<WaterIntake?> {
        return waterIntakeDao.getIntakeByDate(date)
    }

    suspend fun saveIntake(intake: WaterIntake) {
        waterIntakeDao.insertOrUpdate(intake)
    }
}
