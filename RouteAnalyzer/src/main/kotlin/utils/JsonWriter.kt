package org.example.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.example.models.Config
import org.example.models.MostFrequentedAreaResult
import org.example.models.Waypoint
import java.io.File

/**
Write the results to the output.json file
 */
fun writeResultsToJson(outputFile: String, maxWaypoint: Waypoint, maxDistance: Double, mostFrequented: MostFrequentedAreaResult, waypointsOutside: List<Waypoint>, config: Config){
    val output = mapOf(
        "maxDistanceFromStart" to mapOf(
            "waypoint" to mapOf(
                "timestamp" to maxWaypoint.timestamp,
                "latitude" to maxWaypoint.latitude,
                "longitude" to maxWaypoint.longitude
            ),
            "distanceKm" to maxDistance
        ),
        "mostFrequentedArea" to mapOf(
            "centralWaypoint" to mapOf(
                "timestamp" to mostFrequented.centralWaypoint.timestamp,
                "latitude" to mostFrequented.centralWaypoint.latitude,
                "longitude" to mostFrequented.centralWaypoint.longitude
            ),
            "areaRadiusKm" to mostFrequented.areaRadiusKm,
            "entriesCount" to mostFrequented.entriesCount,
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