package com.littlelemon.application.auth.domain.models

data class User(
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
)
