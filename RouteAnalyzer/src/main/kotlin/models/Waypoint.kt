package org.example.models

import kotlinx.serialization.*


@Serializable
data class Waypoint(
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)

