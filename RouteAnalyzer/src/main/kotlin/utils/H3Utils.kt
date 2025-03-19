package org.routeanalyzer.utils
import com.uber.h3core.AreaUnit
import org.routeanalyzer.models.Waypoint
import com.uber.h3core.H3Core
import com.uber.h3core.LengthUnit
import com.uber.h3core.util.GeoCoord
import kotlin.collections.*
import kotlin.math.*

object H3Utils {

    private val h3 = H3Core.newInstance()

    fun maxDistanceFromStartH3(waypoints: List<Waypoint>): Pair<Waypoint?, Double> {

        if (waypoints.isEmpty()) return Pair(null, 0.0)

        val start = waypoints.first()

        var maxDistance = 0.0
        var farthestWaypoint: Waypoint? = null

        for (wp in waypoints) {
            val dist=h3.pointDist(GeoCoord( start.latitude,start.longitude),GeoCoord(wp.latitude,wp.longitude),LengthUnit.km)

            if (dist> maxDistance) {
                maxDistance = dist
                farthestWaypoint = wp
            }
        }

        return Pair(farthestWaypoint, maxDistance)
    }

    fun mostFrequentedAreaH3(waypoints: List<Waypoint>, resolution: Int): Triple<Waypoint?, Int, Double> {
        if (waypoints.isEmpty()) return Triple(null, 0, 0.0)

        val frequencyMap = mutableMapOf<String, Int>() // Hex index → Count
        val timeSpentMap = mutableMapOf<String, Long>() // Hex index → Total time spent

        for (i in 0 until waypoints.size - 1) {
            val wp = waypoints[i]
            val nextWp = waypoints[i + 1]

            val hexIndex = h3.h3ToString(h3.geoToH3(wp.latitude, wp.longitude, resolution))
            frequencyMap[hexIndex] = frequencyMap.getOrDefault(hexIndex, 0) + 1

            // Calculate time spent in this hex (difference between consecutive timestamps)
            val timeSpent = nextWp.timestamp - wp.timestamp
            timeSpentMap[hexIndex] = timeSpentMap.getOrDefault(hexIndex, 0) + timeSpent
        }

        // Handle the last waypoint separately
        if (waypoints.isNotEmpty()) {
            val lastWp = waypoints.last()
            val lastHex = h3.h3ToString(h3.geoToH3(lastWp.latitude, lastWp.longitude, resolution))
            frequencyMap[lastHex] = frequencyMap.getOrDefault(lastHex, 0) + 1
            timeSpentMap[lastHex] = timeSpentMap.getOrDefault(lastHex, 0)  // No time update
        }

        // Find the most visited hexagon
        val maxVisits = frequencyMap.values.maxOrNull() ?: 0
        val candidates = frequencyMap.filter { it.value == maxVisits }.keys

        // If there's more than one candidate, choose the one with the most time spent
        val mostVisitedHex = candidates.maxByOrNull { timeSpentMap[it] ?: 0 }

        // Get the visit count and time spent
        val mostVisitedCount = frequencyMap[mostVisitedHex] ?: 0
        val totalTimeSpent = timeSpentMap[mostVisitedHex] ?: 0

        // Get the hexagon center
        val hexCenter = h3.h3ToGeo(h3.stringToH3(mostVisitedHex))
        val centerLat = hexCenter.lat
        val centerLon = hexCenter.lng

        // Filter waypoints that fall in the most visited hexagon
        val waypointsInHex = waypoints.filter { wp ->
            h3.geoToH3(wp.latitude, wp.longitude, resolution) == h3.stringToH3(mostVisitedHex)
        }

        // Find the waypoint closest to the hexagon center
        val centralWaypoint = waypointsInHex.minByOrNull { wp ->
            DistanceUtils.haversine(wp.latitude, wp.longitude, centerLat, centerLon)
        }

        // Get the hexagon size to define the area radius
        val areaRadiusKm = h3.edgeLength(resolution, LengthUnit.km)

        return Triple(centralWaypoint, mostVisitedCount, areaRadiusKm)
    }


    fun getHexCenterCoordinatesFromWaypoint(waypoint: Waypoint, resolution: Int): GeoCoord {
       val hexIndex= h3.geoToH3(waypoint.latitude, waypoint.longitude, resolution)
        return(h3.h3ToGeo(hexIndex))
    }

    fun waypointsOutsideGeofenceH3(
        waypoints: List<Waypoint>,
        centerLat: Double,
        centerLon: Double,
        geofenceRadiusKm: Double,
        resolution: Int
    ): Triple<List<Waypoint>, Int, Double> {
        if (waypoints.isEmpty()) return Triple(emptyList(), 0, geofenceRadiusKm)

        val geofenceHex = h3.geoToH3(centerLat, centerLon, resolution)

// Use hex width instead of edge length for better approximation
        val hexWidthKm = h3.hexArea(resolution, AreaUnit.km2).pow(0.5) * 2
        val estimatedHexRadius = (geofenceRadiusKm / (hexWidthKm / 2)).toInt()

        val geofenceHexes = h3.kRing(geofenceHex, estimatedHexRadius)

        val waypointsOutside = mutableListOf<Waypoint>()

        for (wp in waypoints) {
            val waypointHex = h3.geoToH3(wp.latitude, wp.longitude, resolution)

            if (!geofenceHexes.contains(waypointHex)) {
                waypointsOutside.add(wp)
            }
        }

        return Triple(waypointsOutside, waypointsOutside.size, geofenceRadiusKm)
    }


}

