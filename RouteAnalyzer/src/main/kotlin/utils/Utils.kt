package org.example.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.uber.h3core.H3Core
import org.example.models.AnalysisResult
import org.example.models.WayPoint
import org.example.models.WaypointMetadata
import java.io.File
import java.lang.Boolean.FALSE
import kotlin.text.Charsets.UTF_8

class Utils {

    companion object {

        val h3 = H3Core.newInstance()

        var startingPoint = WaypointMetadata()

        val mapper = ObjectMapper()
            .writerWithDefaultPrettyPrinter()


        fun parseWayPointFile(path: String): MutableList<WaypointMetadata> {
            print("start parsing waypoint file from: $path")
            val file = File(path)
            val waypointMetadata = mutableListOf<WaypointMetadata>()
            var isFirstLine = true
            var cell: Long = 0

            file.forEachLine { waypoint ->
                run {
                    val waypointDetails = waypoint.split(";")
                    if (isFirstLine) {
                        startingPoint = WaypointMetadata(
                            waypoint = WayPoint(
                                timestamp = waypointDetails[0],
                                laitude = waypointDetails[1].toDouble(),
                                longitude = waypointDetails[2].toDouble()
                            ),
                            cell = h3.geoToH3(waypointDetails[1].toDouble(), waypointDetails[2].toDouble(), 10),
                            distanceFromStartingPoint = 0
                        )
                        isFirstLine = FALSE
                    }
                    cell = h3.geoToH3(waypointDetails[1].toDouble(), waypointDetails[2].toDouble(), 10)
                    waypointMetadata.add(
                        WaypointMetadata(
                            waypoint = WayPoint(
                                timestamp = waypointDetails[0],
                                laitude = waypointDetails[1].toDouble(),
                                longitude = waypointDetails[2].toDouble()
                            ),
                            cell = cell,
                            distanceFromStartingPoint = h3.h3Distance(startingPoint.cell, cell)
                        )
                    )
                }

            }

            print("end parsing waypoint file from: $path")
            return waypointMetadata
        }


        fun writeResultAsJson(analysisResult: AnalysisResult, path: String) {
            print("start writing output file into: $path")

            val result = mapper.writeValueAsString(analysisResult)

            val file = File(path)

            file.createNewFile()

            file.setWritable(true)

            file.setReadable(true)

            file.writeText(result, UTF_8)

            print("end writing output file into: $path")

        }

        fun computeAreaRadiusKm(maxDistanceFromStartingPoint: Int): Number {
            return if (maxDistanceFromStartingPoint < 1) 0.1 else maxDistanceFromStartingPoint / 100
        }


    }


}