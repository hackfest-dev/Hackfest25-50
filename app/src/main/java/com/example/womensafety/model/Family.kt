package com.example.womensafety.model

data class Family(
    val familyId: String,
    val guardianPhone: String,
    val childPhone: String,
    val linked: Boolean,
    val createdAt: Long
)