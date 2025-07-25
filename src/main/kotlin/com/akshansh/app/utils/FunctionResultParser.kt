package com.akshansh.app.utils

import com.akshansh.app.models.SheetsData
import kotlin.reflect.full.memberProperties

class FunctionResultParser {
    fun parseFunctionResult(result: Any): SheetsData? {
        val props = extractNonNullPropertiesRecursive(result)
        var eventName = ""
        val descriptionMap = (props["descriptionMap"] as? Map<*, *>)?.map {
            "${it.key} - ${it.value}"
        }?.joinToString(separator = "\n") ?: ""

        val properties =
            props.entries.filter { !(it.key == "descriptionMap" || it.key.endsWith("eventName")) }
                .joinToString("\n") {
                    "${it.key} : ${it.value}"
                }

        props.entries.forEach {
            if (it.key.endsWith("eventName")) {
                eventName = it.value.toString()
            }
        }
        return if (eventName.isNotEmpty()) {
            SheetsData(
                eventName = eventName,
                comments = descriptionMap,
                protoDetails = properties
            )
        } else null
    }

    private fun extractNonNullPropertiesRecursive(obj: Any, prefix: String = ""): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        obj::class.memberProperties.forEach { prop ->
            val value = prop.getter.call(obj)

            if (value != null) {
                val keyName = if (prefix.isNotEmpty()) "$prefix.${prop.name}" else prop.name

                when {
                    isPrimitive(value) -> {
                        result[keyName] = value
                    }

                    value::class.isData -> {
                        result.putAll(extractNonNullPropertiesRecursive(value, keyName))
                    }

                    value is Collection<*> -> {
                        value.forEachIndexed { idx, elem ->
                            if (elem != null) {
                                val elemKey = "$keyName[$idx]"
                                if (isPrimitive(elem)) {
                                    result[elemKey] = elem
                                } else if (elem::class.isData) {
                                    result.putAll(extractNonNullPropertiesRecursive(elem, elemKey))
                                } else {
                                    result[elemKey] = elem
                                }
                            }
                        }
                    }

                    else -> {
                        result[keyName] = value
                    }
                }
            }
        }

        return result
    }

    private fun isPrimitive(value: Any): Boolean {
        return when (value) {
            is String, is Number, is Boolean, is Char -> true
            else -> false
        }
    }
}