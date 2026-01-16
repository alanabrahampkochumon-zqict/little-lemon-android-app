//package com.littlelemon.application.auth.data
//
//import android.location.Location
//import com.littlelemon.application.auth.data.local.AuthLocalDataSource
//import com.littlelemon.application.auth.data.remote.AuthRemoteDataSource
//import com.littlelemon.application.core.domain.utils.Resource
//import io.github.jan.supabase.auth.user.UserInfo
//import io.github.jan.supabase.auth.user.UserSession
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import kotlinx.serialization.json.JsonPrimitive
//import kotlinx.serialization.json.buildJsonObject
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertNotNull
//import org.junit.jupiter.api.assertNull
//
//class AuthRepositoryImplTest {
//
//    private companion object {
//        const val EMAIL_ADDRESS = "test@email.com"
//        const val VERIFICATION_CODE = "316316"
//
//        const val FIRST_NAME = "first name"
//        const val LAST_NAME = "last name"
//
//        const val ACCESS_TOKEN = "access_token"
//        const val REFRESH_TOKEN = "refresh_token"
//        const val EXPIRES_IN = 3600L
//
//        const val LATITUDE = 1.2345
//        const val LONGITUDE = 3.2354
//    }
//
//    private val user = UserInfo(
//        aud = "",
//        email = EMAIL_ADDRESS,
//        id = "101",
//        userMetadata = buildJsonObject {
//            put(Constants.FIRST_NAME_KEY, JsonPrimitive(FIRST_NAME))
//            put(Constants.LAST_NAME_KEY, JsonPrimitive(LAST_NAME))
//        },
//    )
//
//    private val userSession = UserSession(
//        accessToken = ACCESS_TOKEN,
//        refreshToken = REFRESH_TOKEN,
//        expiresIn = EXPIRES_IN,
//        tokenType = "bearer",
//        user = user
//    )
//    private val remoteDataSource = mockk<AuthRemoteDataSource>()
//    private val localDataSource = mockk<AuthLocalDataSource>()
//
//    private val repository = AuthRepositoryImpl(
//        remoteDataSource,
//        localDataSource
//    )
//
//    @Test
//    fun sendVerificationCode_remoteSuccess_returnsResourceSuccess() = runTest {
//        // Arrange
//        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } returns Unit
//
//        // Act
//        val result = repository.sendVerificationCode(EMAIL_ADDRESS)
//
//        // Assert
//        assertTrue(result is Resource.Success)
//
//        // Verify Remote Function is called
//        coVerify(exactly = 1) { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) }
//    }
//
//    @Test
//    fun sendVerificationCode_remoteFailure_returnResourceError() = runTest {
//        // Arrange
//        val expectedError = "Network Error"
//        coEvery { remoteDataSource.sendVerificationCode(EMAIL_ADDRESS) } throws Exception(
//            expectedError
//        )
//
//        // Act
//        val result = repository.sendVerificationCode(EMAIL_ADDRESS)
//
//        // Assert
//        assertTrue(result is Resource.Failure)
//        result as Resource.Failure
//        assertEquals(expectedError, result.errorMessage)
//    }
//
//    @Test
//    fun verifyVerificationCode_remoteSuccess_returnsSuccessResource() = runTest {
//        // Arrange
//        coEvery {
//            remoteDataSource.verifyVerificationCode(
//                EMAIL_ADDRESS,
//                VERIFICATION_CODE
//            )
//        } returns Unit
//
//        // Act
//        val result = repository.verifyVerificationCode(EMAIL_ADDRESS, VERIFICATION_CODE)
//
//        // Assert
//        assertTrue(result is Resource.Success)
//
//        // Verify Remote Function is called
//        coVerify(exactly = 1) {
//            remoteDataSource.verifyVerificationCode(
//                EMAIL_ADDRESS,
//                VERIFICATION_CODE
//            )
//        }
//    }
//
//
//    @Test
//    fun verifyVerificationCode_remoteFailure_returnResourceError() = runTest {
//        // Arrange
//        val expectedError = "Network Error"
//        coEvery {
//            remoteDataSource.verifyVerificationCode(
//                EMAIL_ADDRESS,
//                VERIFICATION_CODE
//            )
//        } throws Exception(expectedError)
//
//        // Act
//        val result = repository.verifyVerificationCode(EMAIL_ADDRESS, VERIFICATION_CODE)
//
//        // Assert
//        assertTrue(result is Resource.Failure)
//        result as Resource.Failure
//        assertEquals(expectedError, result.errorMessage)
//    }
//
//    @Test
//    fun resendVerificationCode_remoteSuccess_returnsSuccessResource() = runTest {
//        // Arrange
//        coEvery { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) } returns Unit
//
//        // Act
//        val result = repository.resendVerificationCode(EMAIL_ADDRESS)
//
//        // Assert
//        assertTrue(result is Resource.Success)
//
//        // Verify Remote Function is called
//        coVerify(exactly = 1) { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) }
//    }
//
//
//    @Test
//    fun resendVerificationCode_remoteFailure_returnResourceError() = runTest {
//        // Arrange
//        val expectedError = "Network Error"
//        coEvery { remoteDataSource.resendVerificationCode(EMAIL_ADDRESS) } throws Exception(
//            expectedError
//        )
//
//        // Act
//        val result = repository.resendVerificationCode(EMAIL_ADDRESS)
//
//        // Assert
//        assertTrue(result is Resource.Failure)
//        result as Resource.Failure
//        assertEquals(expectedError, result.errorMessage)
//    }
//
//    @Test
//    fun saveUserInformation_remoteSuccess_returnsSuccessResource() = runTest {
//        // Arrange
//        coEvery { remoteDataSource.savePersonalInformation(FIRST_NAME, LAST_NAME) } returns Unit
//
//        // Act
//        val result = repository.saveUserInformation(FIRST_NAME, LAST_NAME)
//
//        // Assert
//        assertTrue(result is Resource.Success)
//
//        // Verify Remote Function is called
//        coVerify(exactly = 1) { remoteDataSource.savePersonalInformation(FIRST_NAME, LAST_NAME) }
//    }
//
//
//    @Test
//    fun saveUserInformation_remoteFailure_returnResourceError() = runTest {
//        // Arrange
//        val expectedError = "Network Error"
//        coEvery {
//            remoteDataSource.savePersonalInformation(
//                FIRST_NAME,
//                LAST_NAME
//            )
//        } throws Exception(expectedError)
//
//        // Act
//        val result = repository.saveUserInformation(FIRST_NAME, LAST_NAME)
//
//        // Assert
//        assertTrue(result is Resource.Failure)
//        result as Resource.Failure
//        assertEquals(expectedError, result.errorMessage)
//    }
//
//    @Test
//    fun getSession_userHasValidSession_returnsResourceSuccessSession() = runTest {
//        // Arrange
//        coEvery { remoteDataSource.getCurrentSession() } returns userSession
//
//        // Act
//        val result = repository.getUserSession()
//
//        // Assert
//        assertTrue(result is Resource.Success)
//        result as Resource.Success
//        val data = result.data
//
//        assertNotNull(data)
//        assertEquals(ACCESS_TOKEN, data.accessToken)
//        assertEquals(REFRESH_TOKEN, data.refreshToken)
//        assertEquals(FIRST_NAME, data.user.firstName)
//        assertEquals(LAST_NAME, data.user.lastName)
//        assertEquals(EMAIL_ADDRESS, data.user.email)
//
//
//        // Verify Remote Function is called
//        coVerify(exactly = 1) { remoteDataSource.getCurrentSession() }
//    }
//
//
//    @Test
//    fun getSession_userHasNoValidSession_returnsResourceSuccessWithNull() = runTest {
//        // Arrange
//        coEvery { remoteDataSource.getCurrentSession() } returns null
//
//        // Act
//        val result = repository.getUserSession()
//
//        // Assert
//        assertTrue(result is Resource.Success)
//        result as Resource.Success
//        assertNull(result.data)
//    }
//
//    @Test
//    fun getSession_remoteError_returnsErrorResource() = runTest {
//        // Arrange
//        val expectedError = "Network Error"
//        coEvery { remoteDataSource.getCurrentSession() } throws Exception(expectedError)
//
//        // Act
//        val result = repository.getUserSession()
//
//        // Assert
//        assertTrue(result is Resource.Failure)
//        result as Resource.Failure
//        assertEquals(expectedError, result.errorMessage)
//    }
//
//    @Test
//    fun getLocation_locationReturnedFromDatasource_returnsSuccessResource() = runTest {
//        // Arrange
//        val location = mockk<Location>()
//        every { location.latitude } returns LATITUDE
//        every { location.longitude } returns LONGITUDE
//        every { location.accuracy } returns 5.0f
//
//        coEvery { localDataSource.getLocation() } returns location
//
//        // Act
//        val result = repository.getLocation()
//
//        // Assert
//        assertTrue(result is Resource.Success)
//        val data = (result as Resource.Success).data
//        assertNotNull(data)
//        assertEquals(LATITUDE, data.latitude)
//        assertEquals(LONGITUDE, data.longitude)
//    }
//
////    @Test
////    fun getLocation_nullFromDatasource_returnsErrorResource() = runTest {
////        // Arrange
////
////        coEvery { localDataSource.getLocation() } returns null
////
////        // Act
////        val result = repository.getLocation()
////
////        // Assert
////        assertTrue(result is Resource.Failure)
////    }
//
//}