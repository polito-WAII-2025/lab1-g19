package org.example.utils

import org.example.models.Waypoint
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class OutputData(
    val maxDistanceFromStart: MaxDistanceFromStart,
    val mostFrequentedArea: MostFrequentedArea = MostFrequentedArea.empty(),
    val waypointsOutsideGeofence: WaypointsOutsideGeofence = WaypointsOutsideGeofence.empty()
)

@Serializable
data class MaxDistanceFromStart(
    val waypoint: Waypoint,
    val distanceKm: Double
)

@Serializable
data class MostFrequentedArea(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int
) {
    companion object {
        fun empty() = MostFrequentedArea(Waypoint(0, 0.0, 0.0), 0.0, 0)
    }
}

@Serializable
data class WaypointsOutsideGeofence(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val count: Int,
    val waypoints: List<Waypoint>
) {
    companion object {
        fun empty() = WaypointsOutsideGeofence(Waypoint(0, 0.0, 0.0), 0.0, 0, emptyList())
    }
}

object OutputWriter {
    fun writeOutput(filePath: String, maxDistWaypoint: Waypoint?, maxDistance: Double) {
        val maxDistData: MaxDistanceFromStart =
            if (maxDistWaypoint != null) {
                MaxDistanceFromStart(maxDistWaypoint, maxDistance)
            } else {
                MaxDistanceFromStart(Waypoint(0, 0.0, 0.0), 0.0)
            }

        val outputData = OutputData(maxDistanceFromStart = maxDistData)

        val json = Json { prettyPrint = true }
        File(filePath).writeText(json.encodeToString(outputData))
    }
}
