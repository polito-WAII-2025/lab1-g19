package org.example

import kotlin.math.*

/**
 * Calcola la distanza tra due punti sulla Terra usando la formula dell'Haversine.
 */
fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, radius: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

/**
 * Trova il waypoint più lontano dal punto di partenza.
 */
fun maxDistanceFromStart(waypoints: List<Waypoint>, config: Config): Pair<Waypoint, Double> {
    if (waypoints.isEmpty()) throw IllegalArgumentException("Lista waypoints vuota!")

    val start = waypoints.first()
    var maxWaypoint = start
    var maxDistance = 0.0

    for (wp in waypoints) {
        val distance = haversineDistance(start.latitude, start.longitude, wp.latitude, wp.longitude, config.earthRadiusKm)
        if (distance > maxDistance) {
            maxDistance = distance
            maxWaypoint = wp
        }
    }

    return Pair(maxWaypoint, maxDistance)
}

/**
 * Trova i waypoint fuori dal geofence.
 */
fun waypointsOutsideGeofence(waypoints: List<Waypoint>, config: Config): List<Waypoint> {
    return waypoints.filter { wp ->
        val distance = haversineDistance(config.geofenceCenterLatitude, config.geofenceCenterLongitude,
            wp.latitude, wp.longitude, config.earthRadiusKm)
        distance > config.geofenceRadiusKm
    }
}
