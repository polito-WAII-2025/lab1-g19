package org.example.utils

import kotlin.math.*
import org.example.models.Waypoint

object DistanceUtils {
    private const val EARTH_RADIUS_KM = 6371.0  // Earth's radius in kilometers

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c  // Returns distance in kilometers
    }

    fun maxDistanceFromStart(waypoints: List<Waypoint>) : Pair<Waypoint?,Double>{
        if (waypoints.isEmpty()){
            return Pair(null, 0.0)
        }
        val start= waypoints.first()
        var farthestWaypoint : Waypoint? = null
        var maxDistance = 0.0

    for(wp in waypoints){
       val dist=haversine(start.latitude, start.longitude, wp.latitude, wp.longitude)
        if(dist > maxDistance){
        maxDistance=dist
        farthestWaypoint = wp
        }
    }
        return Pair(farthestWaypoint, maxDistance)
    }

}
