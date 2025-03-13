package org.example.utils

import com.uber.h3core.H3Core
import org.example.models.Waypoint
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.sql.Timestamp
import kotlin.text.Charsets.UTF_8

class Utils {

    companion object {
        val h3 = H3Core.newInstance()

        /**
         * todo
         * implement check on length of waypointDetails which should be 3
         */
        fun parseWayPointFile(path: String) : MutableList<Waypoint> {
            val file = File(path)
            val waypoints = mutableListOf<Waypoint>()

            file.forEachLine { waypoint ->
                run {
                    val waypointDetails = waypoint.split(";")
                    waypoints.add(
                        Waypoint(
                            timestamp = waypointDetails[0],
                            laitude = waypointDetails[1].toDouble(),
                            longitude = waypointDetails[2].toDouble(),
                            cell = h3.geoToH3(waypointDetails[1].toDouble(), waypointDetails[2].toDouble(), 10)
                        )
                    )
                }

            }

            return waypoints
        }

        fun writeResultAsJson(mostFrequentedArea: Collection<Waypoint>, path: String) {
            val mostFrequentedAreaAsString = "\n\"mostFrequentedArea\" : [\n" +
                    mostFrequentedArea.joinToString(",\n") {
                        "{\n" +
                                "\"timestamp\": \"${it.timestamp}\",\n" +
                                "\"laitude\": ${it.laitude},\n" +
                                "\"longitude\": ${it.longitude},\n" +
                                "\"cell\": ${it.cell}\n" +
                                "}"
                    } + "\n]"

            val result = "{" +
                    "\n$mostFrequentedAreaAsString" +
                    //todo add yours
                    "\n }"

            val file = File(path)

            file.createNewFile()

            file.setWritable(true)

            file.setReadable(true)

            file.writeText(result, UTF_8)

        }


    }




}