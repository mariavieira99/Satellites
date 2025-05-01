/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.satellites.R
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.presentation.FilterOptions
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import com.challenge.satellites.presentation.viewmodel.SatelliteViewModel

@Composable
fun SatelliteScreen(
    innerPadding: PaddingValues,
    onItemClickedCallback: (Int) -> (Unit),
    viewModel: SatelliteViewModel = hiltViewModel()
) {
    val satellitesCollectionUiState = viewModel.satelliteCollectionUiState.collectAsState().value

    Column(modifier = Modifier.padding(innerPadding)) {
        FilterSection(
            onEccentricitySelect = { viewModel.eccentricityOptionSelected = it },
            eccentricityValue = viewModel.eccentricityOptionSelected.first,
            onInclinationSelect = { viewModel.inclinationOptionSelected = it },
            inclinationValue = viewModel.inclinationOptionSelected.first,
            onSortChange = { viewModel.sortOptionSelected = it },
            sortValue = viewModel.sortOptionSelected,
            onSubmit = { viewModel.getFilteredItems() },
            uiState = satellitesCollectionUiState,
        )

        when (satellitesCollectionUiState) {
            is SatelliteCollectionUiState.Loading -> SatelliteLoadingState()
            is SatelliteCollectionUiState.Error -> SatelliteErrorState()

            is SatelliteCollectionUiState.Success -> {
                LazyColumn {
                    items(satellitesCollectionUiState.satellites) { satellite ->
                        SatelliteItem(satellite) {
                            onItemClickedCallback(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SatelliteItem(satellite: Satellite, onItemClickedCallback: (Int) -> (Unit)) {
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

@Composable
fun FilterSection(
    onEccentricitySelect: (Pair<String, Pair<Double?, Double?>?>) -> Unit,
    eccentricityValue: String,
    onInclinationSelect: (Pair<String, Pair<Double?, Double?>?>) -> Unit,
    inclinationValue: String,
    onSortChange: (String) -> Unit,
    sortValue: String,
    onSubmit: () -> Unit,
    uiState: SatelliteCollectionUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        SatelliteFilterDropdown(
            label = "Eccentricity",
            options = FilterOptions.eccentricityOptions,
            value = eccentricityValue,
            onSelect = onEccentricitySelect,
        )

        Spacer(Modifier.height(12.dp))

        SatelliteFilterDropdown(
            label = "Inclination",
            options = FilterOptions.inclinationOptions,
            value = inclinationValue,
            onSelect = onInclinationSelect,
        )

        Spacer(Modifier.height(16.dp))

        SatelliteFilterDropdown(
            label = "Sort Asc By",
            options = FilterOptions.sortOptions,
            value = sortValue,
            onSelect = {
                onSortChange(it.first)
            },
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.End),
            enabled = uiState != SatelliteCollectionUiState.Loading
        ) {
            Text("Filter")
        }
    }
}

@Composable
fun SatelliteFilterDropdown(
    label: String,
    options: List<Pair<String, Pair<Double?, Double?>?>>,
    value: String?,
    onSelect: (Pair<String, Pair<Double?, Double?>?>) -> Unit
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = value ?: options[0].first)
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "DropDown Icon"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                options.forEachIndexed { _, option ->
                    DropdownMenuItem(
                        text = {
                            Text(option.first)
                        },
                        onClick = {
                            onSelect(option)
                            isDropDownExpanded.value = false
                        })
                }
            }
        }
    }

}
