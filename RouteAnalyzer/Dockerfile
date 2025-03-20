# Use an official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build files into the container
COPY build/libs/RouteAnalyzer-1.0-SNAPSHOT-all.jar /app/app.jar

# Define the default command (we'll pass parameters at runtime)
CMD ["java", "-jar", "/app/app.jar"]
