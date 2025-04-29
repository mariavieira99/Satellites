package com.challenge.satellites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.challenge.satellites.ui.theme.SatellitesTheme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SatellitesTheme {
                val api = remember {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://tle.ivanstanojevic.me/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    retrofit.create(TleApi::class.java)
                }

                var satellites by remember { mutableStateOf<List<SatelliteCollection.Member>>(emptyList()) }

                LaunchedEffect(Unit) {
                    satellites = api.getCollection().body()?.member.orEmpty() // FIXME: It seems that we are mixing data logic with UI logic. Can we fix it?
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> // FIXME: Can we fix this?
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) { // FIXME: Can we optimise this?
                        for (satellite in satellites) {
                            Text(
                                text = "${satellite.name}\n\n${satellite.line1}\n${satellite.line2}", // TODO: Can we improve this UI? Feel free to be creative. You can also include more properties like date
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 32.dp, vertical = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}