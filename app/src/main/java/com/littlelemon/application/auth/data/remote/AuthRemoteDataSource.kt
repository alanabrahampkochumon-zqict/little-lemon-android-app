package com.littlelemon.application.auth.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.ktor.client.statement.HttpResponse

class AuthRemoteDataSource(
    private val client: SupabaseClient
) {

    suspend fun sendVerificationCode(emailAddress: String) {
        client.auth.signInWith(OTP) {
            email = emailAddress
        }
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
//
//    suspend fun validateSessionToken(token: RemoteSessionToken): HttpResponse {
//        TODO()
//    }
//
//    suspend fun refreshToken(token: RemoteSessionToken): HttpResponse {
//        TODO()
//    }

}