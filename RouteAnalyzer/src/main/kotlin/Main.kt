package org.routeanalyzer
import org.routeanalyzer.models.MaxDistanceFromStartH3.Companion.maxDistanceFromStartH3
import org.routeanalyzer.models.MostFrequentedAreaH3.Companion.mostFrequentedAreaH3
import org.routeanalyzer.utils.DistanceUtils
import org.routeanalyzer.utils.CSVReader
import org.routeanalyzer.utils.OutputWriter
import org.routeanalyzer.utils.H3Utils
import org.routeanalyzer.models.WaypointsOutsideGeofenceH3.Companion.waypointsOutsideGeofenceH3
import org.routeanalyzer.utils.ConfigReader

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: java -jar app.jar <config-file> <waypoints-file>")
        return
    } //comment

    val configFilePath = args[0]
    val waypointsFilePath = args[1]
    println("Loading config from: $configFilePath")
    val config = ConfigReader.readConfig(configFilePath)
    val outputPath = "output.json"
    val h3OutputPath="/app/output/output.json"
    val waypoints = CSVReader.readWaypoints(waypointsFilePath)
    if (waypoints.isEmpty()) {
        println("No waypoints loaded.")
        return
    }
    println("Loaded ${waypoints.size} waypoints:")

    val (farthestWaypoint, maxDistance) = DistanceUtils.maxDistanceFromStart(waypoints,config.earthRadiusKm)


    if (farthestWaypoint != null) {
        println("Farthest waypoint from start: ${farthestWaypoint.latitude}, ${farthestWaypoint.longitude}")
        println("Max distance from start: $maxDistance km")
    } else {
        println("Could not determine max distance.")
    }

    val maxDistanceFromStart = maxDistanceFromStartH3(waypoints)
    val waypointFromMaxDistFromStart = maxDistanceFromStart.waypoint?: throw Exception("max Waypoint should not be null")
    println("Farthest waypoint (H3): ${waypointFromMaxDistFromStart.latitude}, ${waypointFromMaxDistFromStart.longitude}")
    println("Max H3 grid distance from start: ${maxDistanceFromStart.distanceKm} km, the farthest waypoint is ${waypointFromMaxDistFromStart.latitude}, ${waypointFromMaxDistFromStart.longitude}.")

    val h3Resolution = 9
    val mostFrequentedArea = mostFrequentedAreaH3(waypoints, h3Resolution)

    if (mostFrequentedArea.centralWaypoint != null) {
        println("Most frequented area (H3 most central waypoint) : ${mostFrequentedArea.centralWaypoint.latitude}, ${mostFrequentedArea.centralWaypoint.longitude}")
        println("Most frequented area(H3 actual hexagon center coordinates):${H3Utils.getHexCenterCoordinatesFromWaypoint(mostFrequentedArea.centralWaypoint, h3Resolution)}")
        println("Area Radius (Hexagon edge length in Km): ${mostFrequentedArea.areaRadiusKm} km")
        println("Number of visits: ${mostFrequentedArea.entriesCount}")
    }

    // Compute waypoints outside geofence using H3
    val waypointsOutsideGeofence =
        waypointsOutsideGeofenceH3(waypoints, config.geofenceCenterLatitude, config.geofenceCenterLongitude, config.geofenceRadiusKm, h3Resolution)

    //println("Waypoints outside geofence (Haversine): $outsideCountHaversine")

    OutputWriter.writeH3Output(
        h3OutputPath,
        maxDistanceFromStart,
        mostFrequentedArea,
        waypointsOutsideGeofence
    )

}
