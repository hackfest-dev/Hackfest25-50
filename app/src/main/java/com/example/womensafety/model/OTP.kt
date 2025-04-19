package com.example.womensafety.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "otp")
data class OTP(
    @PrimaryKey val phoneNumber: String, // Guardian’s phone
    val code: String,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = createdAt + 5 * 60 * 1000 // 5 minutes
)