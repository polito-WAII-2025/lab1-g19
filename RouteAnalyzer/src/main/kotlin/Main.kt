package org.routeanalyzer
import org.routeanalyzer.utils.DistanceUtils
import org.routeanalyzer.utils.CSVReader
import org.routeanalyzer.utils.OutputWriter
import org.routeanalyzer.utils.H3OutputWriter
import org.routeanalyzer.utils.H3Utils

fun main() {
    val filePath = "RouteAnalyzer/src/main/resources/waypoints.csv"
    val outputPath = "output.json"
    val h3OutputPath = "RouteAnalyzer/src/main/resources/output_h3.json"
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
    val (centralWaypointH3, visitCountH3, areaRadiusH3) = H3Utils.mostFrequentedAreaH3(waypoints, h3Resolution)

    if (centralWaypointH3 != null) {
        println("Most frequented area (H3 most central waypoint) : ${centralWaypointH3.latitude}, ${centralWaypointH3.longitude}")
        println("Most frequented area(H3 actual hexagon center coordinates):${H3Utils.getHexCenterCoordinatesFromWaypoint(centralWaypointH3, h3Resolution)}")
        println("Area Radius (Hexagon edge length in Km): $areaRadiusH3 km")
        println("Number of visits: $visitCountH3")
    }

    OutputWriter.writeOutput(outputPath, farthestWaypoint, maxDistance)
    H3OutputWriter.writeH3Output(h3OutputPath,farthestWaypointH3,maxDistanceH3, centralWaypointH3, areaRadiusH3, visitCountH3)




}