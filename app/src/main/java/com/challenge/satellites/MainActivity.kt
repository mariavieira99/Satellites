package com.challenge.satellites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.challenge.satellites.presentation.screens.SatelliteDetailScreen
import com.challenge.satellites.presentation.screens.SatelliteScreen
import com.challenge.satellites.ui.theme.SatellitesTheme
import com.challenge.satellites.utils.NetworkConnectivityProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkConnectivityProvider.init(this)
        NetworkConnectivityProvider.registerCallback()

        enableEdgeToEdge()
        setContent {
            SatellitesTheme {
                SatelliteApp()
            }
        }
    }

    override fun onDestroy() {
        NetworkConnectivityProvider.unregisterCallback()
        super.onDestroy()
    }
}

@Composable
fun SatelliteApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(navController = navController, startDestination = "satellites_list") {
            composable(route = "satellites_list") {
                SatelliteScreen(innerPadding = innerPadding, onItemClickedCallback = {
                    navController.navigate("satellite_details/$it")
                })
            }

            composable(
                route = "satellite_details/{id}",
                arguments = listOf(navArgument(name = "id") {
                    type = NavType.IntType
                })
            ) { navBackStack ->
                val id = navBackStack.arguments?.getInt("id") ?: return@composable

                SatelliteDetailScreen(id, backClickCallback = {
                    navController.navigateUp()
                })
            }
        }
    }
}