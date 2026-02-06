package com.littlelemon.application.auth.data.remote

import com.littlelemon.application.auth.data.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.json.put

class AuthRemoteDataSource(
    private val client: SupabaseClient
) {

    suspend fun sendVerificationCode(emailAddress: String) {
        client.auth.signInWith(OTP) {
            email = emailAddress
        }
    }

    suspend fun resendVerificationCode(emailAddress: String) {
        client.auth.resendEmail(OtpType.Email.SIGNUP, emailAddress)
    }

    suspend fun verifyVerificationCode(emailAddress: String, verificationCode: String) {
        client.auth.verifyEmailOtp(
            type = OtpType.Email.EMAIL,
            email = emailAddress,
            token = verificationCode
        )
    }

    suspend fun savePersonalInformation(firstName: String, lastName: String) {
        client.auth.updateUser {
            data {
                put(Constants.FIRST_NAME_KEY, firstName)
                put(Constants.LAST_NAME_KEY, lastName)
            }
        }
    }

    suspend fun getCurrentSession(): UserSession? {
        return client.auth.sessionManager.loadSession()
    }

}