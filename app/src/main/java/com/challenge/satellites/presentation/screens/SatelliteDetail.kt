/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.satellites.R
import com.challenge.satellites.data.model.SatelliteCollection
import com.challenge.satellites.presentation.state.SatelliteDetailUiState
import com.challenge.satellites.presentation.viewmodel.SatelliteDetailViewModel

@Composable
fun SatelliteDetailScreen(
    id: Int,
    backClickCallback: () -> Unit,
    viewModel: SatelliteDetailViewModel = hiltViewModel(),
) {

    val satelliteDetailUiState = viewModel.satelliteDetailUiState.collectAsState().value

    LaunchedEffect(id) {
        viewModel.getSatelliteInfo(id)
    }

    when (satelliteDetailUiState) {
        is SatelliteDetailUiState.Loading -> SatelliteLoadingState()
        is SatelliteDetailUiState.Error -> SatelliteErrorState()
        is SatelliteDetailUiState.Success -> {
            SatelliteDetail(satelliteDetailUiState.satellite, backClickCallback)
        }
    }
}

@Composable
fun SatelliteDetail(satellite: SatelliteCollection.Member, backClickCallback: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackItem(backClickCallback)
        SatelliteInfo(satellite)
    }
}

@Composable
fun SatelliteInfo(satellite: SatelliteCollection.Member) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = satellite.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "Satellite Id: ${satellite.satelliteId}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.padding(12.dp))
            DetailItems("Line 1", satellite.line1)

            Spacer(modifier = Modifier.padding(6.dp))
            DetailItems("Line 2", satellite.line2)

            Spacer(modifier = Modifier.padding(6.dp))
            DetailItems("Date", satellite.date)
        }
    }
}

@Composable
fun BackItem(backClickCallback: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 50.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .clickable {
                    backClickCallback()
                }
        )
    }
}

@Composable
fun DetailItems(title: String, description: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(color = Color.DarkGray),
    )
    Card(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(8.dp),
            ),
        colors = CardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.Gray,
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp, max = 200.dp)
                .verticalScroll(rememberScrollState())
                .alpha(0.8f)
                .padding(8.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSatelliteDetail() {
    val satellite = SatelliteCollection.Member(
        date = "2025-04-29T04:34:06+00:00",
        id = "https://tle.ivanstanojevic.me/api/tle/25544",
        line2 = "2 25544  51.6352 189.7367 0002491  81.0639 279.0631 15.49383308507563",
        line1 = "1 25544U 98067A   25119.19035294  .00013779  00000+0  25440-3 0  9995",
        name = "ISS (ZARYA)",
        satelliteId = 25544,
        type = "Tle",
    )

    SatelliteDetail(satellite = satellite) {
        // Do nothing
    }
}