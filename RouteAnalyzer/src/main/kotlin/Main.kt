package org.example

import org.example.models.AnalysisResult
import org.example.models.MostFrequentedArea
import org.example.utils.Utils
import java.io.File

fun main(args: Array<String>) {

    print("start application")

    if(args.size < 2 || args[0].isBlank() || args[1].isBlank()){
        throw Exception("The passed args are not correct")
    }
    if(!args[0].endsWith("waypoints.csv") || !File(args[0].replace("waypoints.csv", "")).isDirectory || !File(args[0]).exists()){
        throw Exception("The first args should be the waypoints csv file")
    }
    if(!args[1].endsWith("output.json") || !File(args[1].replace("output.json", "")).isDirectory){
        throw Exception("The second argument should be a path to a file named output.json")
    }

    val waypoints = Utils.parseWayPointFile(args[0])

    Utils.writeResultAsJson(
        AnalysisResult(
            mostFrequentedArea = MostFrequentedArea.computeMostFrequentedArea(waypoints)
        ),
        args[1])

    println("end application")

}

