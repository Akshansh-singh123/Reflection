package com.akshansh.app

import java.io.File
import kotlin.reflect.full.hasAnnotation

fun main(args: Array<String>) {
    val baseDir = File("/Users/akshansh/IdeaProjects/ReflectionGeneration/feature/build/classes/kotlin/main")
    val basePackage = "com.gojek.app"

    // Scan for all .class files
    val classFiles = baseDir.walkTopDown().filter { it.extension == "class" }

    for (file in classFiles) {
        val relativePath = file.relativeTo(baseDir).path
        val className = relativePath
            .removeSuffix(".class")
            .replace(File.separatorChar, '.')

        try {
            val clazz = Class.forName(className)

            if (clazz.kotlin.hasAnnotation<ReflectLight>()) {
                println("Found: ${clazz.name}")
            }
        } catch (e: Throwable) {
            println("Could not load class $className: ${e.message}")
        }
    }
}