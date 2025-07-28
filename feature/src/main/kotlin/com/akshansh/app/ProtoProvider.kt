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

    @ReflectFunctionImage
    fun getSendAuthSuccessEventProto(source: String, miniAppId: String): ProtoWrapper {
        return ProtoWrapper(
            message = Component(
                eventName = "Mini app auth success",
                referrer = Referrer(source = source),
                componentDetail = ComponentDetail(name = miniAppId),
                detail = miniAppId
            ),
            descriptionMap = mapOf("component.detail" to "This is the mini App ID")
        )
    }
}

data class ProtoWrapper(
    val message: Any,
    val descriptionMap: Map<String, Any> = mapOf()
)