plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.0.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("org.routeanalyzer.MainKt")
}

tasks {
    jar {
        manifest {
            attributes(
                "Main-Class" to "org.routeanalyzer.MainKt"
            )
        }
        from({
            configurations.runtimeClasspath.get().filter { it.exists() }.map { file ->
                if (file.isDirectory) file else zipTree(file)
            }
        })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.uber:h3:3.7.2")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}