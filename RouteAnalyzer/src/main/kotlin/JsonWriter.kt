package org.example

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

/**
 Scrive i risultati nel file output.json
 */
fun writeResultsToJson(outputFile: String, maxWaypoint: Waypoint, maxDistance: Double, waypointsOutside: List<Waypoint>, config: Config){
    val output = mapOf(
        "maxDistanceFromStart" to mapOf(
            "waypoint" to mapOf(
                "timestamp" to maxWaypoint.timestamp,
                "latitude" to maxWaypoint.latitude,
                "longitude" to maxWaypoint.longitude
            ),
            "distanceKm" to maxDistance
        ),
        "waypointsOutsideGeofence" to mapOf(
            "centralWaypoint" to mapOf(
                "timestamp" to 0,
                "latitude" to config.geofenceCenterLatitude,
                "longitude" to config.geofenceCenterLongitude
            ),
            "areaRadiusKm" to config.geofenceRadiusKm,
            "count" to waypointsOutside.size,
            "waypoints" to waypointsOutside.map{
                mapOf("timestamp" to it.timestamp, "latitude" to it.latitude, "longitude" to it.longitude)
            }
        )
    )

    val mapper = jacksonObjectMapper().writerWithDefaultPrettyPrinter()
    mapper.writeValue(File(outputFile), output)
}