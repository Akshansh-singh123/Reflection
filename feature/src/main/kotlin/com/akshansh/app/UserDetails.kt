package com.akshansh.app

data class UserDetails(
    val name: String?,
    val phone: String?,
    val token: Token?
)

data class Token(
    val value: String,
    val expiryTime: Long
)
