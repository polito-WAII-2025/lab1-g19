package org.routeanalyzer.models

import com.uber.h3core.LengthUnit
import com.uber.h3core.util.GeoCoord
import org.routeanalyzer.utils.H3Utils.Companion.h3

data class MostFrequentedAreaH3(
    val centralWaypoint: Waypoint?,
    val areaRadiusKm: Double,
    val entriesCount: Int
){
    companion object{
        fun mostFrequentedAreaH3(waypoints: List<Waypoint>, resolution: Int): MostFrequentedAreaH3 {
            if (waypoints.isEmpty()) return MostFrequentedAreaH3(null, 0.0, 0)

            val frequencyMap = mutableMapOf<String, Int>()
            val timeSpentMap = mutableMapOf<String, Long>()
            val waypointLen = waypoints.size

            for (i in 0 until waypointLen - 1) {
                val wp = waypoints[i]
                val nextWp = waypoints[i + 1]

                val hexIndex = h3.h3ToString(h3.geoToH3(wp.latitude, wp.longitude, resolution))
                frequencyMap[hexIndex] = frequencyMap.getOrDefault(hexIndex, 0) + 1

                val timeSpent = nextWp.timestamp - wp.timestamp
                timeSpentMap[hexIndex] = timeSpentMap.getOrDefault(hexIndex, 0) + timeSpent
            }

            if (waypoints.isNotEmpty()) {
                val lastWp = waypoints.last()
                val lastHex = h3.h3ToString(h3.geoToH3(lastWp.latitude, lastWp.longitude, resolution))
                frequencyMap[lastHex] = frequencyMap.getOrDefault(lastHex, 0) + 1
                timeSpentMap[lastHex] = timeSpentMap.getOrDefault(lastHex, 0)  // No time update
            }

            val maxVisits = frequencyMap.values.maxOrNull() ?: 0
            val candidates = frequencyMap.filter { it.value == maxVisits }.keys

            val mostVisitedHex = candidates.maxByOrNull { timeSpentMap[it] ?: 0 }

            val mostVisitedCount = frequencyMap[mostVisitedHex] ?: 0

            val waypointsInHex = waypoints.filter { wp ->
                h3.geoToH3(wp.latitude, wp.longitude, resolution) == h3.stringToH3(mostVisitedHex)
            }

            val centralWaypoint = waypointsInHex.minByOrNull { wp ->
                h3.pointDist(GeoCoord( wp.latitude,wp.longitude), GeoCoord(wp.latitude,wp.longitude), LengthUnit.km)
            }

            val areaRadiusKm = h3.edgeLength(resolution, LengthUnit.km)

            return MostFrequentedAreaH3(centralWaypoint, areaRadiusKm, mostVisitedCount)
        }
    }
}
