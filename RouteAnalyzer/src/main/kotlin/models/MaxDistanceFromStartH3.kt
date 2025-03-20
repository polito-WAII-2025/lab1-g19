package org.routeanalyzer.models

import com.uber.h3core.LengthUnit
import com.uber.h3core.util.GeoCoord
import org.routeanalyzer.utils.H3Utils.Companion.h3


data class MaxDistanceFromStartH3 (
    val waypoint: Waypoint?,
    val distanceKm: Double
){
    companion object{
        fun maxDistanceFromStartH3(waypoints: List<Waypoint>): MaxDistanceFromStartH3 {

            if (waypoints.isEmpty()) return MaxDistanceFromStartH3(null, 0.0)

            val start = waypoints.first()

            var maxDistance = 0.0
            var farthestWaypoint: Waypoint? = null

            for (wp in waypoints) {
                val dist= h3.pointDist(
                    GeoCoord( start.latitude,start.longitude),
                    GeoCoord(wp.latitude,wp.longitude),
                    LengthUnit.km)

                if (dist> maxDistance) {
                    maxDistance = dist
                    farthestWaypoint = wp
                }
            }

            return MaxDistanceFromStartH3(farthestWaypoint, maxDistance)
        }
    }
}