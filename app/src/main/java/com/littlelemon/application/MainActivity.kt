package com.littlelemon.application

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.littlelemon.application.core.presentation.NavigationRoot
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
                NavigationRoot()
                runBlocking {
                    try {
                        val context by inject<GeoApiContext>()
                        val geocoderResult = GeocodingApi.geocode(
                            context,
                            "asdfasdfasdfasdfasfdasdfasfdasdfasdfasfa423142afsfdaf"
                        ).await()
//                        Log.d("Location", geocoderResult)
                        Log.d("Location", geocoderResult.contentDeepToString())

                    } catch (e: Exception) {
                        Log.d("Location Exception", e.message.toString())
                        Log.d("Location", e.toString())
                    }
                }
            }
        }
    }
}