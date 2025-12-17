package com.littlelemon.application.auth.domain.models

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val verificationCode: String? = null,
)
