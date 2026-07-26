package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey val date: String, // Format YYYY-MM-DD
    val amountMl: Int
)
