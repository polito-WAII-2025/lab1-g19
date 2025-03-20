package org.routeanalyzer.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.routeanalyzer.models.*
import java.io.File


class OutputWriter {

    companion object{

        val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .writerWithDefaultPrettyPrinter()

        fun writeH3Output(
            filePath: String,
            maxDistanceFromStart: MaxDistanceFromStartH3,
            mostFrequentedArea: MostFrequentedAreaH3,
            waypointsOutsideGeofence: WaypointsOutsideGeofenceH3
        ) {
            File(filePath).parentFile?.mkdirs()
            val outputData = H3OutputData(
                maxDistanceFromStart = maxDistanceFromStart,
                mostFrequentedArea = mostFrequentedArea,
                waypointsOutsideGeofence = waypointsOutsideGeofence
            )

            File(filePath).writeText(mapper.writeValueAsString(outputData))

            println("H3 Output saved to $filePath")
        }

    }
}