package com.littlelemon.application.auth.domain.models

import java.time.LocalDateTime

data class SessionToken(
    val accessToken: String,
    val accessTokenExpiry: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpiry: LocalDateTime
)