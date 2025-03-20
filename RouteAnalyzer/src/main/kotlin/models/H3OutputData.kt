package org.routeanalyzer.models


data class H3OutputData (
    val maxDistanceFromStart: MaxDistanceFromStartH3,
    val mostFrequentedArea: MostFrequentedAreaH3,
    val waypointsOutsideGeofence: WaypointsOutsideGeofenceH3
)