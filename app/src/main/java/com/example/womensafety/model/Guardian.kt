package com.example.womensafety.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guardian")
data class Guardian(
    @PrimaryKey val phoneNumber: String, // Links to Contact.phoneNumber
    val name: String,
    val verified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)