package com.littlelemon.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.littlelemon.application.auth.presentation.VerificationScreen
import com.littlelemon.application.core.presentation.designsystem.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
//                LoginScreen()

                VerificationScreen()
            }
        }
    }
}