package com.akshansh.app

data class ComponentDetail(
    val referrer: Referrer
)

data class Referrer(
    val source: String,
    val viewSource: String,
    val user: UserDetails? = null
)
