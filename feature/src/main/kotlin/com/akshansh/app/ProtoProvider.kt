package com.akshansh.app


@ReflectLight
class ProtoProvider {
    @ReflectFunctionImage
    fun getSendLoginEventProto(name: String?, phone: String?): UserDetails {
        return UserDetails(name, phone)
    }

    fun getAnotherFeature(): String {
        return "Reflected another feature"
    }

    fun getFeatureNotAnnotated(): String {
        return "Not annotated"
    }
}