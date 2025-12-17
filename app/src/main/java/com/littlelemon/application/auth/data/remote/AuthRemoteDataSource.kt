package com.littlelemon.application.auth.data.remote

import com.littlelemon.application.auth.data.models.RemoteSessionToken
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

class AuthRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun sendVerificationCode(): HttpResponse {
        TODO()
    }

    suspend fun verifyVerificationCode(verificationCode: String): HttpResponse {
        TODO()
    }

    suspend fun savePersonalInformation(firstName: String, lastName: String): HttpResponse {
        TODO()
    }

    suspend fun getSessionToken(firstName: String, lastName: String): HttpResponse {
        TODO()
    }

    suspend fun validateSessionToken(token: RemoteSessionToken): HttpResponse {
        TODO()
    }

    suspend fun refreshToken(token: RemoteSessionToken): HttpResponse {
        TODO()
    }

}