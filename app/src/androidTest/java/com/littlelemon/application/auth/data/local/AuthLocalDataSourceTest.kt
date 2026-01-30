package com.littlelemon.application.auth.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class AuthLocalDataSourceTest {


    private companion object {
        const val LATITUDE = 1.2343
        const val NEW_LATITUDE = 2.344343
        const val LONGITUDE = 3.23452
        const val NEW_LONGITUDE = 4.234234

        const val STALE_TIME = 10 * 24 * 60 * 60 * 1000L
    }

//    @get:Rule
//    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
//        android.Manifest.permission.ACCESS_FINE_LOCATION,
//        android.Manifest.permission.ACCESS_COARSE_LOCATION,
//    )
//
//
//    private lateinit var context: Context
//    private lateinit var client: FusedLocationProviderClient
//    private lateinit var dataSource: AuthLocalDataSource
//    private lateinit var location: Location
//    private lateinit var staleLocation: Location

    @Before
    fun setUp() = runTest {

//        context = ApplicationProvider.getApplicationContext<Context>()
//
//        // 1. Force-Grant the permission programmatically
//        val packageName = context.packageName
//        val command = "appops set $packageName android:mock_location allow"
//
//        // Execute the shell command as the "Shell" user (which has higher privileges)
//        InstrumentationRegistry.getInstrumentation()
//            .uiAutomation
//            .executeShellCommand(command)
//            .close() // Important: Close the stream to avoid leaks
//
//
//        client = LocationServices.getFusedLocationProviderClient(context)
//        dataSource = AuthLocalDataSource(client)
//
//        location = Location("gps").apply {
//            isMock = true
//            latitude = LATITUDE
//            longitude = LONGITUDE
//            time = System.currentTimeMillis()
//            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
//            accuracy = 1.0f
//        }
//
//        staleLocation = Location("gps").apply {
//            isMock = true
//            latitude = NEW_LATITUDE
//            longitude = NEW_LONGITUDE
//            time = System.currentTimeMillis() - STALE_TIME
//            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos() - (STALE_TIME * 1_000_000)
//            accuracy = 1.0f
//        }
//
//        client.setMockMode(true).await()
    }

//    @After
//    fun tearDown() {
//        client.setMockMode(false)
//    }
    

    @Test
    fun getLastLocation_null_getNewLocation() = runTest {
//        // Arrange
//        client.lastLocation.addOnSuccessListener { location ->
//            // Assert 1
//            assertNull(location)
//        }.await()
//
//        val deferredCall = async { dataSource.getLocation() }
//
//        location.time = System.currentTimeMillis()
//        location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
//
//        client.setMockLocation(location).await()
////        delay(1000)
//
//        // Act
//        val result = deferredCall.await()
//
//        // Assert
//        assertNotNull(result)
//        assertEquals(LATITUDE, result.latitude)
//        assertEquals(LONGITUDE, result.longitude)
    }


    @Test
    fun getLastLocation_stale_getNewLocation() = runTest {
//        // Arrange
//        client.setMockLocation(staleLocation)
//        delay(1000)
//
//        client.lastLocation.addOnSuccessListener { location ->
//            assertNotNull(location)
//            assertEquals(staleLocation.time, location.time)
//        }
//
//        val deferredCall = async { dataSource.getLocation() }
//        delay(1000)
//
//        client.setMockLocation(location)
//        delay(1000)
//
//        // Act
//        val result = deferredCall.await()
//
//        // Assert
//        assertNotNull(result)
//        assertEquals(location.latitude, result.latitude)
//        assertEquals(location.longitude, result.longitude)

    }

    @Test
    fun getLastLocation_fresh_getsLastLocation() = runTest {
//        // Arrange
//        client.setMockLocation(location)
//        delay(1000)
//        // Act
//        val result = dataSource.getLocation()
//
//        // Assert
//        assertNotNull(result)
//        assertEquals(LATITUDE, result.latitude)
//        assertEquals(LONGITUDE, result.longitude)
//        assertTrue { result.time > System.currentTimeMillis() - STALE_TIME }
    }
}