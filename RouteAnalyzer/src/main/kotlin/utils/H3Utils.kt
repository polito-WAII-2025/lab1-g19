package org.routeanalyzer.utils
import org.routeanalyzer.models.Waypoint
import com.uber.h3core.H3Core
import com.uber.h3core.LengthUnit
import com.uber.h3core.util.GeoCoord
import kotlin.collections.*

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

        for (wp in waypoints) {
            val hexIndex = h3.h3ToString(h3.geoToH3(wp.latitude, wp.longitude, resolution))
            frequencyMap[hexIndex] = frequencyMap.getOrDefault(hexIndex, 0) + 1
        }

        // Find the most frequently visited hex
        val mostVisitedHex = frequencyMap.maxByOrNull { it.value }?.key
        val mostVisitedCount = frequencyMap[mostVisitedHex] ?: 0

            val hexCenter = h3.h3ToGeo(h3.stringToH3(mostVisitedHex))
            val centerLat =hexCenter.lat
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
        val areaRadiusKm = h3.edgeLength(resolution,LengthUnit.km)

        return Triple(centralWaypoint, mostVisitedCount, areaRadiusKm)
    }

    fun getHexCenterCoordinatesFromWaypoint(waypoint: Waypoint, resolution: Int): GeoCoord {
       val hexIndex= h3.geoToH3(waypoint.latitude, waypoint.longitude, resolution)
        return(h3.h3ToGeo(hexIndex))
    }

}

