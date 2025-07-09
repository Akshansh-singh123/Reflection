package com.akshansh.app

class EventTracker(private val protoProvider: ProtoProvider) {
    fun trackLoginEvent(userDetails: UserDetails) {
        trackEvent(protoProvider.getSendLoginEventProto(userDetails))
    }

    private fun trackEvent(message: Any) {
        // logic to send CS event
    }
}