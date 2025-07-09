package com.akshansh.app

data class Component(
    val eventName: String,
    val referrer: Referrer,
    val componentDetail: ComponentDetail?
)

data class ComponentDetail(
    val name: String?,
    val value: String?,
    val state: String?
)

data class Referrer(
    val source: String,
    val viewSource: String,
    val user: UserDetails? = null
)
