FROM gradle:8.3-jdk17 AS build

WORKDIR /app
COPY . .

RUN gradle clean jar --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]