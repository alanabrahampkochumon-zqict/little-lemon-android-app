package com.littlelemon.application.auth.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.MemorySessionManager
import io.github.jan.supabase.createSupabaseClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRemoteDataSourceTest {
    private lateinit var client: SupabaseClient
    private lateinit var datasource: AuthRemoteDataSource

    private val EMAIL_ADDRESS = "test@email.com"

    @Before
    fun setUp() {
        val engine = MockEngine{ request ->
            respond(
                content = """{"message": "success"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        client = createSupabaseClient("url", "key") {
            httpEngine = engine
            install(Auth) {
                sessionManager = MemorySessionManager()
            }

        }
        datasource = AuthRemoteDataSource(client)
    }

    @Test
    fun onVerificationCode_noExceptionThrown() = runTest {

        datasource.sendVerificationCode(EMAIL_ADDRESS)
    }
}