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
    implementation ("com.google.api-client:google-api-client:2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.41.5")
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