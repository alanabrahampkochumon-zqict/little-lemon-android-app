package com.littlelemon.application.auth.domain.usecase.params

data class VerificationParams(
    val emailAddress: String,
    val verificationCode: String
)
