package com.littlelemon.application.address.data.remote
//
//import com.google.maps.GeocodingApi
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class GoogleGeocodingRemoteDataSourceTest {
//
//    val address = "Some Fake Address, Fake Street, Fake City, Fake State - 123456"
//
//    private lateinit var geocodingApi: GeocodingApi
//
//    @BeforeEach
//    fun setUp() {
//        geocodingApi = mockk()
//    }
//
//    @Test
//    fun geocoder_whenGeocoderReturnsNoResult_throwNoResultException() = runTest {
//        // Given that api return `NO_RESULT`
//        coEvery { geocodingApi.gecode }
//
//        // When, address is geocoded
//
//        // Then, it throws an `NoResultException`
//
//    }
//}