package com.littlelemon.application.auth.data.remote

import com.littlelemon.application.auth.data.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.put

class AuthRemoteDataSource(
    private val client: SupabaseClient
) {

    suspend fun sendVerificationCode(emailAddress: String) {
        client.auth.signInWith(OTP) {
            email = emailAddress
        }
    }

    // Send and Resend OTP uses similar methods since
    // Dedicated method for only signup or email change exists
    suspend fun resendVerificationCode(emailAddress: String) {
        client.auth.signInWith(OTP) {
            email = emailAddress
        }
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

    // TODO: Extract and abstract user session when extracting interface
    suspend fun getCurrentSession(): UserSession? {
        return client.auth.sessionManager.loadSession()
    }

    // TODO: Extract and remote session status when extracting interface
    fun getSessionStatus(): Flow<SessionStatus> {
        return client.auth.sessionStatus
    }
    // TODO: For future refactoring if needed
    /**
     *      @Nested
     *     inner class SessionStatusTests {
     *         // SPECIAL NOTE: Right now these tests are testing supabase's implementation or echoing it.
     *         // However, if we refactor out the class into interface, these tests are critically to knowing that our logic is correct.
     *         @Test
     *         fun getSessionStatus_statusInitializing_flowEmittedIsInitializing() = runTest {
     *
     *         }
     *
     *         @Test
     *         fun getSessionStatus_statusAuthenticated_flowEmittedIsAuthenticated() = runTest {
     *
     *         }
     *
     *         @Test
     *         fun getSessionStatus_statusNotAuthenticated_flowEmittedIsUnauthenticated() = runTest {
     *
     *         }
     *     }
     */

}