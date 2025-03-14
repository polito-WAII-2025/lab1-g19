package org.example

fun main(){
    val configPath = "src/main/resources/custom-parameters.yml"
    val waypointsPath = "src/main/resources/waypoints.csv"

    val config = loadConfig(configPath)
    val waypoints = parseCsv(waypointsPath)

    //Calculate the farthest waypoint
    val(maxWp, maxDist) = maxDistanceFromStart(waypoints, config)
    println("Max Distance from Start: ${maxDist}km - Waypoint: $maxWp")

    //Find the most frequented area
    val mostFrequented = mostFrequentedArea(waypoints, maxDist)
    println("Most frequented Area: ${mostFrequented.centralWaypoint}")

    //Find waypoints outside the geofence
    val waypointsOutside = waypointsOutsideGeofence(waypoints, config)
    println("Waypoints Outside from Geofence: ${waypointsOutside.size}")

    //Write the results to output.json
    writeResultsToJson("output.json", maxWp, maxDist, mostFrequented, waypointsOutside, config)
}