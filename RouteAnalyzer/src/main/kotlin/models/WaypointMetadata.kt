package org.example.models

data class WaypointMetadata(
    val waypoint: WayPoint = WayPoint(
        timestamp = "",
        laitude = 0.0,
        longitude = 0.0
    ),
    var cell: Long = 0L,
    var distanceFromStartingPoint: Int = 0
)
