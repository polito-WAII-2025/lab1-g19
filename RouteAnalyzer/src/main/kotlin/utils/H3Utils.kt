package org.routeanalyzer.utils
import org.routeanalyzer.models.Waypoint
import com.uber.h3core.H3Core
import com.uber.h3core.util.GeoCoord

class H3Utils {

    companion object{
        val h3 = H3Core.newInstance()

        fun getHexCenterCoordinatesFromWaypoint(waypoint: Waypoint, resolution: Int): GeoCoord {
            val hexIndex= h3.geoToH3(waypoint.latitude, waypoint.longitude, resolution)
            return(h3.h3ToGeo(hexIndex))
        }
    }

}

