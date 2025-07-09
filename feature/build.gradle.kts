plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Jar>("featureJar") {
    archiveBaseName.set("feature")
    archiveVersion.set("")
    from(sourceSets.main.get().output)
}

kotlin {
    jvmToolchain(21)
}