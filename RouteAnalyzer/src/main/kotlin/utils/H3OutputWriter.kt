package org.routeanalyzer.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import org.routeanalyzer.models.Waypoint

@Serializable
data class H3OutputData(
    val maxDistanceFromStartH3: MaxDistanceFromStartH3,
    val mostFrequentedAreaH3: MostFrequentedAreaH3
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

object H3OutputWriter {
    fun writeH3Output(filePath: String, maxDistWaypoint: Waypoint?, maxDistance: Double,centralWaypoint: Waypoint?, areaRadius: Double, visitCount: Int) {
        val outputData = H3OutputData(MaxDistanceFromStartH3(maxDistWaypoint,maxDistance),
            mostFrequentedAreaH3 = MostFrequentedAreaH3(
                centralWaypoint = centralWaypoint,
                areaRadiusKm = areaRadius,
                entriesCount = visitCount
            )
        )

        val json = Json { prettyPrint = true }
        File(filePath).writeText(json.encodeToString(outputData))
        println("H3 Output saved to $filePath")
    }
}