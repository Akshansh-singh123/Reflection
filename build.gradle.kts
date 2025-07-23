plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "com.akshansh.app.LightSourceKt"
            )
        }
        archiveBaseName.set("reflect-gen")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}