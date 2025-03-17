package org.example
import org.example.utils.DistanceUtils
import org.example.utils.CSVReader
fun main() {
    val filePath = "RouteAnalyzer/src/main/resources/waypoints.csv"
    val waypoints = CSVReader.readWaypoints(filePath)
    if (waypoints.isEmpty()) {
        println("No waypoints loaded.")
        return
    }
    println("Loaded ${waypoints.size} waypoints:")
    waypoints.forEach { println(it) }
    val (farthestWaypoint, maxDistance) = DistanceUtils.maxDistanceFromStart(waypoints)

    if (farthestWaypoint != null) {
        println("Farthest waypoint from start: ${farthestWaypoint.latitude}, ${farthestWaypoint.longitude}")
        println("Max distance from start: $maxDistance km")
    } else {
        println("Could not determine max distance.")
    }


}