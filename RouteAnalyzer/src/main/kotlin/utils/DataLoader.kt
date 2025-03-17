package org.example.utils

import java.io.File
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.example.models.Config
import org.example.models.Waypoint
import org.yaml.snakeyaml.Yaml
import java.nio.charset.StandardCharsets

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