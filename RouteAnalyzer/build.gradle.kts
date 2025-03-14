plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation ("com.uber:h3:3.7.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application{
    mainClass = "org.example.MainKt"
}