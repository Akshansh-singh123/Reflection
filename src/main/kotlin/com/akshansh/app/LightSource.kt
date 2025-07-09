package com.akshansh.app

import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.full.hasAnnotation

fun main(args: Array<String>) {
    val baseDir = File("/Users/akshansh/IdeaProjects/ReflectionGeneration/feature/build/libs/feature.jar")
    val basePackage = "com.akshansh.app"

    val classLoader = URLClassLoader(
        arrayOf(baseDir.toURI().toURL()),
        Thread.currentThread().contextClassLoader
    )

    val classNames = scanJarForClasses(baseDir, basePackage)
    println(classNames)

    for (className in classNames) {
        try {
            val clazz = classLoader.loadClass(className)
            val kClass = clazz.kotlin

            if (kClass.hasAnnotation<ReflectLight>()) {
                println("Found annotated class: ${clazz.name}")
            }
        } catch (e: Throwable) {
            println("Failed to load class $className: ${e.message}")
        }
    }
}

fun scanJarForClasses(jarFile: File, packagePrefix: String): List<String> {
    val jar = JarFile(jarFile)

    return jar.entries().asSequence()
        .filter { it.name.endsWith(".class") && !it.name.contains('$') } // avoid inner classes
        .map { it.name.replace('/', '.').removeSuffix(".class") }
        .filter { it.startsWith(packagePrefix) } // filter only your package
        .toList()
}