package com.akshansh.app

data class Component(
    val eventName: String,
    val referrer: Referrer?,
    val componentDetail: ComponentDetail?,
    val detail: String = ""
)

data class ComponentDetail(
    val name: String? = null,
    val value: String? = null,
    val state: String? = null,
)

data class Referrer(
    val source: String,
    val viewSource: String? = null,
    val user: UserDetails? = null
)
