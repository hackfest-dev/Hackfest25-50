package com.example.womensafety.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "police_helpline")
data class PoliceHelpline(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val mobileNumber: String,
    val email: String
)

