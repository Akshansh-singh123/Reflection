package com.akshansh.app


@ReflectLight
class FeatureA {
    @ReflectFunctionImage
    fun getFeature(): String {
        return "Reflected feature"
    }

    @ReflectFunctionImage
    fun getAnotherFeature(): String {
        return "Reflected another feature"
    }

    fun getFeatureNotAnnotated(): String {
        return "Not annotated"
    }
}