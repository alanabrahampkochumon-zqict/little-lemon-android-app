package com.littlelemon.application.auth.domain.models

import kotlinx.datetime.LocalDateTime


data class SessionToken(
    val accessToken: String,
    val refreshToken: String,
    val tokenExpiry: LocalDateTime,
    val user: User
)