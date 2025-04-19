package com.example.womensafety.model

import java.util.UUID

data class IncidentReport(
    val id: String = UUID.randomUUID().toString(), // Anonymous ID
    val latitude: Double,
    val longitude: Double,
    val city: String?, // Nullable for cases where city is unavailable
    val category: String,
    val description: String?,
    val mediaPath: String?, // File path to photo/video
    val timestamp: Long = System.currentTimeMillis()
)