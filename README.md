[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/vlo9idtn)
# lab1-wa2025

## RouteAnalyzer - Microservices-Based Car Rental System
This is a **Kotlin-based backend application** that processes **route data** for a microservices-based car rental system. It reads **waypoints from a CSV file**, computes analytics, and outputs results in JSON format.

---

## Features
-  Calculate max distance from the start
-  Identify the most frequented area using H3 indexing
-  Detect waypoints outside a geofence
-  Compare H3-based vs. Haversine-based geofence detection
-  Run as a Docker container

---

## Setup & Running Locally
### 1️ Prerequisites
- JDK 17+
- Gradle
- Docker (optional, for running in a container)

---

## Running in Docker

### 1️ Build the Docker Image
```bash
docker build -t route-analyzer .
```

### 2️ Run the Container with Mounted Volumes
```bash
docker run --rm `
    -v "${PWD}/src/custom-parameters.yml:/app/custom-parameters.yml" `
    -v "${PWD}/src/main/resources/waypoints.csv:/app/waypoints.csv" `
    -v "${PWD}/output:/app/output" `
    route-analyzer java -jar /app/app.jar /app/custom-parameters.yml /app/waypoints.csv
```

---

## Input Files
### 1️ `custom-parameters.yml` (Configuration)
Example:
```yaml
earthRadiusKm: 6371.0
geofenceCenterLatitude: 45.0
geofenceCenterLongitude: 7.0
geofenceRadiusKm: 1.5
mostFrequentedAreaRadiusKm: 0.5
```

### 2️ `waypoints.csv` (Route Data)
Format:
```
timestamp;latitude;longitude
1741862253426;45.07087;7.67603
1741862253675.15;45.07216;7.69001
```

---

## Output
### 1️`output_h3.json` (Analysis Results)
Example:
```json
{
  "maxDistanceFromStart": {
    "waypoint": {
      "timestamp": 1742377734343,
      "latitude": 45.99472,
      "longitude": 6.12624
    },
    "distanceKm": 157.9201723646025
  },
  "mostFrequentedArea": {
    "centralWaypoint": {
      "timestamp": 1742377715848,
      "latitude": 45.25019,
      "longitude": 9.04498
    },
    "areaRadiusKm": 0.174375668,
    "entriesCount": 3
  },
  "waypointsOutsideGeofence": {
    "centralWaypoint": {
      "timestamp": 0,
      "latitude": 45.07081,
      "longitude": 7.66609
    },
    "areaRadiusKm": 100.0,
    "count": 270,
    "waypoints": [
      {
        "timestamp": 0,
        "latitude": 45.07081,
        "longitude": 7.66609
      }
    ]
  }
}
```

