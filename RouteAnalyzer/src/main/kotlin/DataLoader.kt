package org.example

import java.io.File
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.yaml.snakeyaml.Yaml
import java.nio.charset.StandardCharsets

/**
 * Data Class per i parametri di configurazione
 */

data class Config (
    val earthRadiusKm: Double,
    val geofenceCenterLatitude: Double,
    val geofenceCenterLongitude: Double,
    val geofenceRadiusKm: Double,
    val mostFrequentedAreaRadiusKm: Double? = null
)

/**
 Data class per rappresentare un waypoint
 */

data class Waypoint(val timestamp: Long, val latitude: Double, val longitude: Double)

/**
 *Carica la configurazione di un file YAML
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
 carica i waypoint da un file CSV
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