package com.akshansh.app

import com.akshansh.app.models.SheetsData
import com.akshansh.app.utils.FunctionResultParser
import com.akshansh.app.utils.FunctionInvoker
import com.akshansh.app.utils.GoogleSheetsUploader
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

const val SPREADSHEET_ID = "1ypTSCcUOH4qS4vUc2KorcrOM78WcjxHhJMyWzj5U3uw"
const val SHEET_NAME = "Sheet1"
const val BASE_PACKAGE = "com.akshansh.app"

class Viewer {
    private val functionInvoker = FunctionInvoker()
    private val functionResultParser = FunctionResultParser()
    private val googleSheetsUploader = GoogleSheetsUploader()

    fun view(baseDir: File) {
        val classLoader = URLClassLoader(
            arrayOf(baseDir.toURI().toURL()),
            Thread.currentThread().contextClassLoader
        )

        val rows = mutableListOf<SheetsData>()
        for (className in scanJarForClasses(baseDir)) {
            try {
                val clazz = classLoader.loadClass(className)
                val kClass = clazz.kotlin

                if (kClass.hasAnnotation<ReflectLight>()) {
                    val reflectedClassInstance = kClass.createInstance()

                    kClass.declaredFunctions
                        .filter { it.findAnnotation<ReflectFunctionImage>() != null }
                        .forEach { reflectedFunction ->
                            val result =
                                functionInvoker.invokeFunction(
                                    reflectedFunction = reflectedFunction,
                                    reflectedClassInstance = reflectedClassInstance
                                )

                            if (result != null) {
                                functionResultParser.parseFunctionResult(result)?.let { rows.add(it) }
                            }
                        }
                }
            } catch (e: Throwable) {
                println("Exception $className: ${e.message}")
            }
        }
        googleSheetsUploader.pushToGoogleSheets(
            rows = rows,
            spreadsheetId = SPREADSHEET_ID,
            sheetName = SHEET_NAME
        )
    }

    private fun scanJarForClasses(jarFile: File): List<String> {
        val jar = JarFile(jarFile)

        return jar.entries().asSequence()
            .filter { it.name.endsWith(".class") && !it.name.contains('$') } // avoid inner classes
            .map { it.name.replace('/', '.').removeSuffix(".class") }
            .filter { it.startsWith(BASE_PACKAGE) } // filter only your package
            .toList()
    }
}