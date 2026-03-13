package com.littlelemon.application.address.data.remote
//
//import com.google.maps.GeoApiContext
//import com.google.maps.GeocodingApiRequest
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class GoogleGeocodingClientTest {
//
//    private lateinit var geocodingClient: GeocodingClient
//    private lateinit var geocodingRequest: GeocodingApiRequest
//    private lateinit var context: GeoApiContext
//
//    @BeforeEach
//    fun setUp() {
//        context = mockk()
//        geocodingClient = GoogleGeocodingClient(context)
//
//        mockkStatic("com.google.maps.GeocodingApi")
//    }
//
//
//    @Test
//    fun onGeocode_onApiSuccess_returnsGeocodingDTO() = runTest {
//        // When, the api returns success
//    }
//
//    @Test
//    fun onGeocode_onApiZeroResults_throwsZeroResultException() = runTest { }
//
//    @Test
//    fun onGeocode_onApiDailyLimitExceeded_throwsDailyLimitException() = runTest { }
//
//    @Test
//    fun onGeocode_onApiQueryLimitExceeded_throwsQueryLimitException() = runTest { }
//
//    @Test
//    fun onGeocode_onApiRequestDenied_throwsRequestDeniedException() = runTest { }
//
//    @Test
//    fun onGeocode_onApiInvalidRequest_throwsInvalidRequestException() = runTest { }
//
//    @Test
//    fun onGeocode_onApiUnknownError_throwsUnknownException() = runTest { }
//}