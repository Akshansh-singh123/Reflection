package com.akshansh.app

import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*

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
                        val functionArgs = function.parameters.map { param ->
                            when (param.kind) {
                                KParameter.Kind.INSTANCE -> instance
                                KParameter.Kind.VALUE -> mockValueForType(
                                    param.type.classifier as? KClass<*> ?: Any::class
                                )

                                else -> null
                            }
                        }
                        val result = function.call(*functionArgs.toTypedArray())
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

fun mockValueForType(type: KClass<*>): Any? {
    mockPrimitive(type)?.let { return it }

    runCatching { return type.createInstance() }

    val constructor = type.primaryConstructor ?: return null

    val args: Map<KParameter, Any?> = constructor.parameters.associateWith { param ->
        val paramType = param.type.classifier as? KClass<*> ?: Any::class
        mockValueForType(paramType)
    }

    return runCatching { constructor.callBy(args) }.getOrElse {
        println("Failed to create instance of ${type.simpleName}: ${it.message}")
        null
    }
}

fun mockPrimitive(type: KClass<*>): Any? = when (type) {
    String::class -> "mock"
    Int::class -> 0
    Long::class -> 0L
    Double::class -> 0.0
    Float::class -> 0f
    Boolean::class -> false
    List::class -> emptyList<Any>()
    Set::class -> emptySet<Any>()
    Map::class -> emptyMap<Any, Any>()
    else -> null
}