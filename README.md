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

### 2️ Clone the Repository
```bash
git clone https://github.com/..../lab1-g19.git
cd lab1-g19/RouteAnalyzer
```

### 3️ Build the Kotlin Application
```bash
./gradlew clean build
```

### 4️ Run the Application Locally
```bash
java -jar build/libs/RouteAnalyzer-1.0-SNAPSHOT-all.jar src/custom-parameters.yml src/main/resources/waypoints.csv
```

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
    "maxDistanceFromStartH3": {
        "waypoint": {
            "timestamp": 1741862253675,
            "latitude": 45.07216,
            "longitude": 7.69001
        },
        "distanceKm": 5.77
    },
    "mostFrequentedAreaH3": {
        "centralWaypoint": {
            "timestamp": 1741862253675,
            "latitude": 45.07216,
            "longitude": 7.69001
        },
        "areaRadiusKm": 0.69,
        "entriesCount": 5
    },
    "waypointsOutsideGeofence": {
        "count": 183
    }
}
```

