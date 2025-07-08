package com.akshansh.app

import java.io.File

fun main(args: Array<String>){
    val baseDir = File("build/classes/kotlin/main")
    val basePackage = "com.example"

    // Scan for all .class files
    val classFiles = baseDir.walkTopDown().filter { it.extension == "class" }

    for (file in classFiles) {
        println(file.name)
        val relativePath = file.relativeTo(baseDir).path
        val className = relativePath
            .removeSuffix(".class")
            .replace(File.separatorChar, '.')

        try {
            val clazz = Class.forName("$basePackage.$className")
            val kClass = clazz.kotlin


        } catch (e: Throwable) {
            println("Could not load class $className: ${e.message}")
        }
    }
}