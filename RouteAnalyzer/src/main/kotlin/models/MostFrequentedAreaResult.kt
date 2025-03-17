package org.example.models

/**
Data class for the result of the most frequented area.
 */
data class MostFrequentedAreaResult(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int
)