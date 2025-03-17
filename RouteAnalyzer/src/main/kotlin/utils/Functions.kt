package org.example.utils

import kotlin.math.*
import org.example.models.Waypoint
import org.example.models.Config
import org.example.models.MostFrequentedAreaResult
/**
 * Calculates the distance between two points on Earth using the Haversine formula.
 */
fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, radius: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

/**
 * Finds the farthest waypoint from the starting point.
 */
fun maxDistanceFromStart(waypoints: List<Waypoint>, config: Config): Pair<Waypoint, Double> {
    if (waypoints.isEmpty()) throw IllegalArgumentException("Waypoints list is empty!")

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
 * Finds the most frequented area
 */
fun mostFrequentedArea(waypoints: List<Waypoint>, maxDistance: Double): MostFrequentedAreaResult {
    if (waypoints.isEmpty()) throw IllegalArgumentException("Waypoints list is empty!")

    // Automatically calculates the radius
    val areaRadiusKm = if (maxDistance < 1.0) 0.1 else maxDistance * 0.1

    var bestWaypoint: Waypoint = waypoints.first()
    var maxEntries = 0

    // Analyzes each waypoint as a potential center for the most frequented area
    for (wp in waypoints) {
        val count = waypoints.count { other ->
            haversineDistance(wp.latitude, wp.longitude, other.latitude, other.longitude, 6371.0) <= areaRadiusKm
        }

        // If an area with more nearby waypoints is found, updates the result
        if (count > maxEntries) {
            maxEntries = count
            bestWaypoint = wp
        }
    }

    return MostFrequentedAreaResult(
        centralWaypoint = bestWaypoint,
        areaRadiusKm = areaRadiusKm,
        entriesCount = maxEntries
    )
}



/**
 Finds the waypoints outside the geofence.
 */
fun waypointsOutsideGeofence(waypoints: List<Waypoint>, config: Config): List<Waypoint> {
    return waypoints.filter { wp ->
        val distance = haversineDistance(config.geofenceCenterLatitude, config.geofenceCenterLongitude,
            wp.latitude, wp.longitude, config.earthRadiusKm)
        distance > config.geofenceRadiusKm
    }
}
