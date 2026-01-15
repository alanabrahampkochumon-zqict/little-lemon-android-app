package com.littlelemon.application.auth.data.remote

import io.github.jan.supabase.SupabaseClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRemoteDataSourceTest {
    private lateinit var client: SupabaseClient

    @BeforeEach
    fun setUp() {
        TODO("Not yet implemented")
    }


    // { data: { user: null, session: null }, error: null }
    @Test
    fun onSendVerificationCode_exceptionThrown() {
    }

    @Test
    fun verifyVerificationCode() {
    }

    @Test
    fun savePersonalInformation() {
    }

    @Test
    fun getSessionToken() {
    }

    @Test
    fun validateSessionToken() {
    }

    @Test
    fun refreshToken() {
    }

}