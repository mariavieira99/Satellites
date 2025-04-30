/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.satellites.data.model.SatelliteCollection
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import com.challenge.satellites.presentation.viewmodel.SatelliteViewModel

@Composable
fun SatelliteScreen(
    innerPadding: PaddingValues,
    onItemClickedCallback: (Int) -> (Unit),
    viewModel: SatelliteViewModel = hiltViewModel()
) {
    val satellitesCollectionUiState = viewModel.satelliteCollectionUiState.collectAsState().value

    when (satellitesCollectionUiState) {
        is SatelliteCollectionUiState.Loading -> SatelliteLoadingState()
        is SatelliteCollectionUiState.Error -> SatelliteErrorState()

        is SatelliteCollectionUiState.Success -> {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(satellitesCollectionUiState.satellites) { satellite ->
                    SatelliteItem(satellite) {
                        onItemClickedCallback(it)
                    }
                }
            }
        }
    }
}

@Composable
fun SatelliteItem(satellite: SatelliteCollection.Member, onItemClickedCallback: (Int) -> (Unit)) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable {
                onItemClickedCallback(satellite.satelliteId)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = satellite.name,
                style = MaterialTheme.typography.titleLarge
            )

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Text(
                    text = satellite.line1,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Text(
                    text = satellite.line2,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row {
                Text(
                    text = satellite.date,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun SatelliteLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
fun SatelliteErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Could not load the satellites info. Try again later!")
    }
}
