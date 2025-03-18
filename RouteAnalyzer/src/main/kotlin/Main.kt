package org.routeanalyzer
import org.routeanalyzer.utils.DistanceUtils
import org.routeanalyzer.utils.CSVReader
import org.routeanalyzer.utils.OutputWriter
import org.routeanalyzer.utils.H3Utils

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
    val (farthestWaypointH3, maxDistanceH3) = H3Utils.maxDistanceFromStartH3(waypoints)
    if (farthestWaypointH3 != null) {
        println("Farthest waypoint (H3): ${farthestWaypointH3.latitude}, ${farthestWaypointH3.longitude}")
    }
    println("Max H3 grid distance from start: $maxDistanceH3 km, the farthest waypoint is ${farthestWaypointH3?.latitude}, ${farthestWaypointH3?.longitude}.")

    val h3Resolution = 9  // Adjust resolution for accuracy
    val (centralWaypoint, visitCount, areaRadius) = H3Utils.mostFrequentedAreaH3(waypoints, h3Resolution)

    if (centralWaypoint != null) {
        println("Most frequented area (H3 most central waypoint) : ${centralWaypoint.latitude}, ${centralWaypoint.longitude}")
        println("Most frequented area(H3 actual hexagon center coordinates):${H3Utils.getHexCenterCoordinatesFromWaypoint(centralWaypoint, h3Resolution)}")
        println("Area Radius (Hexagon edge length in Km): $areaRadius km")
        println("Number of visits: $visitCount")
    }

    OutputWriter.writeOutput(outputPath, farthestWaypoint, maxDistance)



}