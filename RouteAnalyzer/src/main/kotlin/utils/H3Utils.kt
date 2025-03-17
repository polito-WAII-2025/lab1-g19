package org.example.utils
import org.example.models.Waypoint
import com.uber.h3core.H3Core


fun maxDistanceFromStartH3(waypoints: List<Waypoint>, resolution: Int): Pair<Waypoint?, Int> {

     val h3 = H3Core.newInstance()
    if (waypoints.isEmpty()) return Pair(null, 0)

    val start = waypoints.first()
    val startIndex = h3.geoToH3(start.latitude, start.longitude, resolution)

    var maxDistance = 0
    var farthestWaypoint: Waypoint? = null

    for (wp in waypoints) {
        val currentIndex = h3.geoToH3(wp.latitude, wp.longitude, resolution)
        val gridDistance = h3.h3Distance(startIndex, currentIndex)

        if (gridDistance > maxDistance) {
            maxDistance = gridDistance
            farthestWaypoint = wp
        }
    }

    return Pair(farthestWaypoint, maxDistance)
}