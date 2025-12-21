package com.littlelemon.application.auth.data.models

data class LocalSessionToken(
    val accessToken: String,
    val accessExpiry: Long,
    val refreshToken: String,
    val refreshExpiry: Long,
)