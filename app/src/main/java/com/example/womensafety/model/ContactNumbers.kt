package com.example.womensafety.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactNumbers(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    val phoneNumber: String,
    val email: String? = null
)