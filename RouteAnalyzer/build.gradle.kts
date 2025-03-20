plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.uber:h3:3.7.2")
    implementation("org.yaml:snakeyaml:2.0")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}