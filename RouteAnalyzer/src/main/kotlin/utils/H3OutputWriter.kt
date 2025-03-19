package org.routeanalyzer.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import org.routeanalyzer.models.Waypoint

@Serializable
data class H3OutputData(
    val maxDistanceFromStartH3: MaxDistanceFromStartH3,
    val mostFrequentedAreaH3: MostFrequentedAreaH3,
    val waypointsOutsideGeofence: WaypointsOutsideGeofenceH3
)

@Serializable
data class MaxDistanceFromStartH3(
    val waypoint: Waypoint?,
    val distanceKm: Double
)

@Serializable
data class MostFrequentedAreaH3(
    val centralWaypoint: Waypoint?,
    val areaRadiusKm: Double,
    val entriesCount: Int
)

@Serializable
data class WaypointsOutsideGeofenceH3(
    val centralWaypoint: Waypoint?,
    val areaRadiusKm: Double,
    val count: Int,
    val waypoints: List<Waypoint>
)



object H3OutputWriter {
    fun writeH3Output(
        filePath: String,
        maxDistWaypoint: Waypoint?,
        maxDistance: Double,
        centralWaypoint: Waypoint?,
        areaRadius: Double,
        visitCount: Int,
        outsideWaypoints: List<Waypoint>,
        outsideCount: Int,
        geofenceRadius: Double,
        geofenceCenter: Waypoint
    ) {
        val outputData = H3OutputData(
            maxDistanceFromStartH3 = MaxDistanceFromStartH3(maxDistWaypoint, maxDistance),
            mostFrequentedAreaH3 = MostFrequentedAreaH3(
                centralWaypoint = centralWaypoint,
                areaRadiusKm = areaRadius,
                entriesCount = visitCount
            ),
            waypointsOutsideGeofence = WaypointsOutsideGeofenceH3(
                centralWaypoint = geofenceCenter,
                areaRadiusKm = geofenceRadius,
                count = outsideCount,
                waypoints = outsideWaypoints
            )
        )

        val json = Json { prettyPrint = true }
        File(filePath).writeText(json.encodeToString(outputData))

        println("H3 Output saved to $filePath")
    }
}