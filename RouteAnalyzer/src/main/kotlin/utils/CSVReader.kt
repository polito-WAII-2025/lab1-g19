package org.example.utils


import org.example.models.Waypoint
import java.io.File

object CSVReader {
    fun readWaypoints(filePath: String): List<Waypoint> {
        val waypoints = mutableListOf<Waypoint>()

        File(filePath).useLines { lines ->
            for (line in lines) {
                val parts = line.split(";")

                if (parts.size == 3) {
                    val rawTimestamp = parts[0].trim()
                    val latitude = parts[1].toDoubleOrNull()
                    val longitude = parts[2].toDoubleOrNull()

                    val timestamp = rawTimestamp.toDoubleOrNull()?.toLong()

                    if (timestamp != null  && latitude != null && longitude != null) {
                        waypoints.add(Waypoint(timestamp , latitude, longitude))
                    }else {
                        println("Skipping invalid row: $line")
                    }
                }
            }
        }
        return waypoints
    }
}
