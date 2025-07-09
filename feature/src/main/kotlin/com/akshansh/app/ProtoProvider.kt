package com.akshansh.app


@ReflectLight
class ProtoProvider {
    @ReflectFunctionImage
    fun getSendLoginEventProto(userDetails: UserDetails): ProtoWrapper {
        return ProtoWrapper(
            message = Component(
                eventName = "Login event",
                Referrer(source = "Source", viewSource = "View Source", user = userDetails),
                componentDetail = ComponentDetail(name = userDetails.name, state = null, value = userDetails.phone)
            ),
            descriptionMap = mapOf("component.componentDetail.name" to "Name of user")
        )
    }

    @ReflectFunctionImage
    fun getSendLoginPageViewedEvent(): ProtoWrapper {
        return ProtoWrapper(
            message = Component(
                eventName = "Login page viewed event",
                Referrer(source = "Source", viewSource = "View Source"),
                componentDetail = null
            )
        )
    }

    fun getFeatureNotAnnotated(): String {
        return "Not annotated"
    }
}

data class ProtoWrapper(
    val message: Any,
    val descriptionMap: Map<String, Any> = mapOf()
)