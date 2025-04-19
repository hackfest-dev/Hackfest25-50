package com.example.womensafety.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val phoneNumber: String,
    val name: String,
    val email: String?,
    val password: String?,
    val isGuardian: Boolean? = false, // True if guardian for any user
    val guardianPhoneNumber: String? = null // Null if no guardian
)