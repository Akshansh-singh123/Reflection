package com.akshansh.app

class APlusDailyRewardAnalytics(private val protoProvider: ProtoProvider) {
    fun trackLoginEvent(userDetails: UserDetails) {
        trackEvent(protoProvider.getSendLoginEventProto(userDetails).message)
    }

    fun sendAuthSuccessEvent(source: String, miniAppId: String) {
        trackEvent(protoProvider.getSendAuthSuccessEventProto(source, miniAppId))
    }

    private fun trackEvent(message: Any) {
        // logic to send CS event
    }
}