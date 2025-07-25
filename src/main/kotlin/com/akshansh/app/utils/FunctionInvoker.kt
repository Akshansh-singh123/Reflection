package com.akshansh.app.utils

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class FunctionInvoker {
    fun invokeFunction(reflectedFunction: KFunction<*>, reflectedClassInstance: Any): Any? {
        val functionArgs = reflectedFunction.parameters.map { param ->
            when (param.kind) {
                KParameter.Kind.INSTANCE -> reflectedClassInstance
                KParameter.Kind.VALUE -> mockValueForType(
                    param.type.classifier as? KClass<*> ?: Any::class
                )

                else -> null
            }
        }
        return reflectedFunction.call(*functionArgs.toTypedArray())
    }

    private fun mockValueForType(type: KClass<*>): Any? {
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

    private fun mockPrimitive(type: KClass<*>): Any? = when (type) {
        String::class -> "Variable value"
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
}