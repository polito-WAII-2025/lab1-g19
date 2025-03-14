package org.example

fun main(){
    val configPath = "src/main/resources/custom-parameters.yml"
    val waypointsPath = "src/main/resources/waypoints.csv"

    val config = loadConfig(configPath)
    val waypoints = parseCsv(waypointsPath)

    //Calcola il waypoint più lontano
    val(maxWp, maxDist) = maxDistanceFromStart(waypoints, config)
    println("Max Distance from Start: ${maxDist}km - Waypoint: $maxWp")

    //Trova i Waypoint fuori dal Geofence
    val waypointsOutside = waypointsOutsideGeofence(waypoints, config)
    println("Waypoints Outside from Geofence: ${waypointsOutside.size}")

    //Scrive i risultati in output.json
    writeResultsToJson("output.json", maxWp, maxDist, waypointsOutside, config)
}