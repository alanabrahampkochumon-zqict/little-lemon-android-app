package com.littlelemon.application.utils

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData

// Taken from supabase-kt test-common
// https://github.com/supabase-community/supabase-kt/blob/master/test-common/src/commonMain/kotlin/io/github/jan/supabase/testing/SupabaseClientMock.kt

fun createFakeSupabaseClient(
    supabaseURL: String = "https://supabase.com/",
    supabaseKey: String = "fake-supabase-key",
    configuration: SupabaseClientBuilder.() -> Unit = {},
    requestHandler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData = {
        respond(
            ""
        )
    },
): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = supabaseURL,
        supabaseKey = supabaseKey,
    ) {
        install(Postgrest)
        install(Storage)
        httpEngine = MockEngine { request ->
            requestHandler(request)
        }
        configuration()
    }
}