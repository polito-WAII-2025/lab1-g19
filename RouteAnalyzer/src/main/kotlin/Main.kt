package org.example

import org.example.models.Waypoint
import org.example.utils.Utils

fun main(args: Array<String>) {
    Utils.writeResultAsJson(mostFrequentedArea(Utils.parseWayPointFile(args[0])), args[1])
    println("Hello World!")
}

fun mostFrequentedArea(wayPoints: MutableList<Waypoint>): Collection<Waypoint> {
    val cellCount = mutableMapOf<Long, Int>()
    wayPoints.forEach { waypoint ->
        cellCount[waypoint.cell] = cellCount.getOrDefault(waypoint.cell, 0) + 1
    }
    val cellWithMaxCount = cellCount.maxByOrNull { it.value }
    return wayPoints.filter { it.cell == cellWithMaxCount?.key }
}