package org.routeanalyzer.models

import com.uber.h3core.AreaUnit
import org.routeanalyzer.utils.H3Utils.Companion.h3
import kotlin.math.pow

data class WaypointsOutsideGeofenceH3(
    val centralWaypoint: Waypoint?,
    val areaRadiusKm: Double,
    val count: Int,
    val waypoints: List<Waypoint>
){
    companion object{
        fun waypointsOutsideGeofenceH3(
            waypoints: List<Waypoint>,
            centerLat: Double,
            centerLon: Double,
            geofenceRadiusKm: Double,
            resolution: Int
        ): WaypointsOutsideGeofenceH3 {
            if (waypoints.isEmpty()) return WaypointsOutsideGeofenceH3(
                Waypoint(0, centerLat, centerLon),
                geofenceRadiusKm,
                0,
                listOf()
            )

            val geofenceHex = h3.geoToH3(centerLat, centerLon, resolution)

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

            return WaypointsOutsideGeofenceH3(
                Waypoint(0, centerLat, centerLon),
                geofenceRadiusKm,
                waypointsOutside.size,
                waypointsOutside
            )
        }
    }
}