package org.example.models

import org.example.utils.Utils.Companion.computeAreaRadiusKm


data class MostFrequentedArea(
    var centralWaypoint: WayPoint,
    var areaRadiusKm: Number,
    var entriesCount: Long
){
    companion object{
        fun computeMostFrequentedArea(wayPoints: MutableList<WaypointMetadata>): MostFrequentedArea {
            println("start computation of most frequented area")
            val cellCount = mutableMapOf<Long, Int>()
            wayPoints.forEach { waypoint ->
                cellCount[waypoint.cell] = cellCount.getOrDefault(waypoint.cell, 0) + 1
            }
            val cellWithMaxCount = cellCount.maxBy { it.value }
            val result = MostFrequentedArea(
                centralWaypoint = (wayPoints.find { it.cell == cellWithMaxCount.key }
                    ?: throw Exception("A waypoint should be present for this cell: ${cellWithMaxCount.key}")).let {
                    val waypoint = it.waypoint
                    WayPoint(
                        timestamp = waypoint.timestamp,
                        laitude = waypoint.laitude,
                        longitude = waypoint.longitude
                    )
                },
                areaRadiusKm = computeAreaRadiusKm(wayPoints.maxBy { it.distanceFromStartingPoint }.distanceFromStartingPoint),
                entriesCount = cellWithMaxCount.value.toLong()
            )
            println("end computation of most frequented area")
            return result
        }
    }
}
