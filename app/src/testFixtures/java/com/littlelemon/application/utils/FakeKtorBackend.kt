package com.littlelemon.application.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

object FakeKtorBackend {

    suspend fun getErrorResponse(
        url: String = "something.com",
        errorCode: HttpStatusCode = HttpStatusCode.BadRequest,
        content: String = """{"error":"Bad Request"}""".trimIndent()
    ): HttpResponse {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(content),
                status = errorCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val fakeClient = HttpClient(engine = mockEngine)
        return fakeClient.get(url)
    }
}