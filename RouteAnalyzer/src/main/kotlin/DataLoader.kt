package org.example

import java.io.File
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.yaml.snakeyaml.Yaml
import java.nio.charset.StandardCharsets

/**
 Data class for configuration parameters
 */
data class Config (
    val earthRadiusKm: Double,
    val geofenceCenterLatitude: Double,
    val geofenceCenterLongitude: Double,
    val geofenceRadiusKm: Double,
    val mostFrequentedAreaRadiusKm: Double? = null
)

/**
 Data class to represent a waypoint
 */
data class Waypoint(val timestamp: Long, val latitude: Double, val longitude: Double)

/**
 Data class for the result of the most frequented area.
 */
data class MostFrequentedAreaResult(
    val centralWaypoint: Waypoint,
    val areaRadiusKm: Double,
    val entriesCount: Int
)

/**
 Loads the configuration from a YAML file
 */
fun loadConfig(filePath: String): Config {
    val yaml = Yaml()
    val file = File(filePath).readText()
    val map: Map<String, Any> = yaml.load(file)

    return Config(
        earthRadiusKm = map["earthRadiusKm"] as Double,
        geofenceCenterLatitude = map["geofenceCenterLatitude"] as Double,
        geofenceCenterLongitude = map["geofenceCenterLongitude"] as Double,
        geofenceRadiusKm = map["geofenceRadiusKm"] as Double,
        mostFrequentedAreaRadiusKm = map["mostFrequentedAreaRadiusKm"] as? Double
    )
}

/**
 Loads waypoints from a CSV file
 */
fun parseCsv(filePath: String): List<Waypoint> {
    val file = File(filePath)
    val csvFormat = CSVFormat.DEFAULT.builder()
        .setDelimiter(';')
        .setHeader()
        .setSkipHeaderRecord(true)
        .build()

    val parsedFile = CSVParser(
        file.bufferedReader(StandardCharsets.UTF_8),
        csvFormat
    )

    return parsedFile.records.map{
        Waypoint(
            timestamp = it[0].toDouble().toLong(),
            latitude = it[1].toDouble(),
            longitude = it[2].toDouble()
        )
    }
}