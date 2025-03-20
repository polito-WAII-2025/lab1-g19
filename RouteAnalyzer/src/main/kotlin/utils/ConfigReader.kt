package org.routeanalyzer.utils


import org.yaml.snakeyaml.Yaml
import java.io.File

data class Config(
    val earthRadiusKm: Double,
    val geofenceCenterLatitude: Double,
    val geofenceCenterLongitude: Double,
    val geofenceRadiusKm: Double,
    val mostFrequentedAreaRadiusKm: Double? = null
)

object ConfigReader {
    fun readConfig(filePath: String): Config {
        val yaml = Yaml()
        val inputStream = File(filePath).inputStream()
        val data: Map<String, Any> = yaml.load(inputStream)

        return Config(
            earthRadiusKm = (data["earthRadiusKm"] as Number).toDouble(),
            geofenceCenterLatitude = (data["geofenceCenterLatitude"] as Number).toDouble(),
            geofenceCenterLongitude = (data["geofenceCenterLongitude"] as Number).toDouble(),
            geofenceRadiusKm = (data["geofenceRadiusKm"] as Number).toDouble(),
            mostFrequentedAreaRadiusKm = (data["mostFrequentedAreaRadiusKm"] as? Number)?.toDouble()
        )
    }
}
