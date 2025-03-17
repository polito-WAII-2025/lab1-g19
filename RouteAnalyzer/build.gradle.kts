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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation(kotlin("stdlib"))
}

tasks {
    jar {
        manifest {
            attributes(
                "Main-Class" to "org.example.MainKt" // Classe principale con il metodo main()
            )
        }
        // Includi i file di runtimeClasspath nel JAR
        from({
            configurations.runtimeClasspath.get().filter { it.exists() }.map { file ->
                if (file.isDirectory) file else zipTree(file)
            }
        })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE // Evita conflitti di duplicati
    }

    test {
        useJUnitPlatform()
    }
}



kotlin {
    jvmToolchain(17)
}

application{
    mainClass = "org.example.MainKt"
}