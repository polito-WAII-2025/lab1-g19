package org.example
import org.example.utils.DistanceUtils
import org.example.utils.CSVReader
import org.example.utils.OutputWriter
import org.example.utils.maxDistanceFromStartH3
fun main() {
    val filePath = "RouteAnalyzer/src/main/resources/waypoints.csv"
    val outputPath = "output.json"
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

    // Compute max distance using H3
    val h3Resolution = 9  // Adjust resolution for accuracy
    val (farthestWaypointH3, maxDistanceH3) = maxDistanceFromStartH3(waypoints, h3Resolution)
    println("Farthest waypoint (H3): ${farthestWaypointH3?.latitude}, ${farthestWaypointH3?.longitude}")
    println("Max H3 grid distance from start: $maxDistanceH3 hexagons")

    OutputWriter.writeOutput(outputPath, farthestWaypoint, maxDistance)


}