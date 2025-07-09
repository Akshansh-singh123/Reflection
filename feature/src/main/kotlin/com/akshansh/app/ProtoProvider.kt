package com.akshansh.app


@ReflectLight
class ProtoProvider {
    @ReflectFunctionImage
    fun getSendLoginEventProto(userDetails: UserDetails): ComponentDetail {
        return ComponentDetail(Referrer(source = "Source", viewSource = "View Source", user = userDetails))
    }

    @ReflectFunctionImage
    fun getAnotherFeature(): String {
        return "Reflected another feature"
    }

    fun getFeatureNotAnnotated(): String {
        return "Not annotated"
    }
}