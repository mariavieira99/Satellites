/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.satellites.presentation.viewmodel.SatelliteViewModel

@Composable
fun SatelliteScreen(
    innerPadding: PaddingValues,
    viewModel: SatelliteViewModel = hiltViewModel()
) {
    val satellites = viewModel.satelliteData.collectAsState().value?.member.orEmpty()

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        items(satellites) { satellite ->
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