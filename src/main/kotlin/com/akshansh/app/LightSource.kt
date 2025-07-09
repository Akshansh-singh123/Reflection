package com.akshansh.app

import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun main(args: Array<String>) {
    val baseDir = File("/Users/akshansh/IdeaProjects/ReflectionGeneration/feature/build/libs/feature.jar")
    val basePackage = "com.akshansh.app"

    val classLoader = URLClassLoader(
        arrayOf(baseDir.toURI().toURL()),
        Thread.currentThread().contextClassLoader
    )

    for (className in scanJarForClasses(baseDir, basePackage)) {
        try {
            val clazz = classLoader.loadClass(className)
            val kClass = clazz.kotlin

            if (kClass.hasAnnotation<ReflectLight>()) {
                val instance = kClass.createInstance()
                kClass.declaredFunctions
                    .filter { it.findAnnotation<ReflectFunctionImage>() != null }
                    .forEach { function ->
                        println(function.parameters.map { it.kind.name })
                        println("Found annotated function: ${function.name}")
                        val result = function.call(instance, "Akshansh", "8219048321")
                        println("Invoked ${function.name}, returned: $result")
                    }
            }
        } catch (e: Throwable) {
            println("Exception $className: ${e.message}")
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