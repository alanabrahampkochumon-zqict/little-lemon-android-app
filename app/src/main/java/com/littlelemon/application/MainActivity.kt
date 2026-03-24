package com.littlelemon.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.maps.MapsInitializer
import com.littlelemon.application.core.presentation.NavigationRoot
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            LittleLemonTheme {
                NavigationRoot()
                MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST) { }
            }
        }
    }
}