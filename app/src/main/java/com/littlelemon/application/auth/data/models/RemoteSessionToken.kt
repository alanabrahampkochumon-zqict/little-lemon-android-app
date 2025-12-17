package com.littlelemon.application.auth.data.models

data class RemoteSessionToken(
    val accessToken: String,
    val refreshToken: String
)