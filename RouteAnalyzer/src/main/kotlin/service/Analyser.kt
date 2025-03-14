package org.example.service

import org.example.models.MostFrequentedArea
import org.example.models.WayPoint
import org.example.models.WaypointMetadata

class Analyser {
    companion object {

        fun computeMostFrequentedArea(wayPoints: MutableList<WaypointMetadata>): MostFrequentedArea {
            print("start computation of most frequented area")
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
            print("end computation of most frequented area")
            return result
        }

        private fun computeAreaRadiusKm(maxDistanceFromStartingPoint: Int): Number {
            return if (maxDistanceFromStartingPoint < 1) 0.1 else maxDistanceFromStartingPoint / 100
        }

    }
}