package com.challenge.satellites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.challenge.satellites.presentation.screens.SatelliteScreen
import com.challenge.satellites.ui.theme.SatellitesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatellitesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SatelliteScreen(innerPadding)
                }
            }
        }
    }
}