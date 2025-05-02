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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.satellites.R
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.FilterOption
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import com.challenge.satellites.presentation.viewmodel.SatelliteViewModel
import com.challenge.satellites.ui.theme.Blue
import com.challenge.satellites.ui.theme.Blue2
import com.challenge.satellites.ui.theme.BlueLight
import com.challenge.satellites.ui.theme.BlueLighter

@Composable
fun SatelliteScreen(
    innerPadding: PaddingValues,
    onItemClickedCallback: (Int) -> (Unit),
    viewModel: SatelliteViewModel = hiltViewModel()
) {
    val satellitesCollectionUiState = viewModel.satelliteCollectionUiState.collectAsState().value

    Column(
        modifier = Modifier
            .background(BlueLight)
            .padding(innerPadding)
    ) {
        FilterSection(
            onEccentricitySelect = { viewModel.eccentricityOptionSelected = it },
            eccentricityValue = viewModel.eccentricityOptionSelected,
            onInclinationSelect = { viewModel.inclinationOptionSelected = it },
            inclinationValue = viewModel.inclinationOptionSelected,
            onSortSelect = { viewModel.sortOptionSelected = it },
            sortValue = viewModel.sortOptionSelected,
            onSubmit = { viewModel.getFilteredItems() },
            uiState = satellitesCollectionUiState,
        )

        when (satellitesCollectionUiState) {
            is SatelliteCollectionUiState.Loading -> SatelliteLoadingState()
            is SatelliteCollectionUiState.Error -> SatelliteErrorState()

            is SatelliteCollectionUiState.Success -> SatelliteItemsList(satellitesCollectionUiState.satellites) {
                onItemClickedCallback(it)
            }
        }
    }
}

@Composable
fun SatelliteItemsList(satellites: List<Satellite>, onItemClickedCallback: (Int) -> (Unit)) {
    LazyColumn(Modifier.padding(start = 10.dp, end = 10.dp)) {
        items(satellites) { satellite ->
            SatelliteItem(satellite) {
                onItemClickedCallback(it)
            }
        }
    }
}

@Composable
fun SatelliteItem(satellite: Satellite, onItemClickedCallback: (Int) -> (Unit)) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable {
                onItemClickedCallback(satellite.satelliteId)
            },
        colors = CardColors(
            containerColor = Blue,
            contentColor = Color.White,
            disabledContainerColor = Color.Blue,
            disabledContentColor = Color.Blue
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = satellite.name,
                style = MaterialTheme.typography.titleLarge,
                color = BlueLighter,
            )

            Text(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .background(Blue2),
                text = satellite.line1,
                style = MaterialTheme.typography.bodyLarge,
                color = BlueLighter,
            )

            Text(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .background(Blue2),
                text = satellite.line2,
                style = MaterialTheme.typography.bodyLarge,
                color = BlueLighter,
            )

            Text(
                text = satellite.date,
                style = MaterialTheme.typography.bodyMedium,
                color = BlueLighter,
            )
        }
    }
}

@Composable
fun SatelliteLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueLight),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
fun SatelliteErrorState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueLight),
        contentAlignment = Alignment.Center
    ) {
        Text("Could not load the satellites info. Try again later!")
    }
}

@Composable
fun FilterSection(
    onEccentricitySelect: (EccentricityFilter) -> Unit,
    eccentricityValue: EccentricityFilter,
    onInclinationSelect: (InclinationFilter) -> Unit,
    inclinationValue: InclinationFilter,
    onSortSelect: (SatelliteSort) -> Unit,
    sortValue: SatelliteSort,
    onSubmit: () -> Unit,
    uiState: SatelliteCollectionUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .background(BlueLighter, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        SatelliteFilterDropdown(
            label = "Eccentricity",
            options = EccentricityFilter.entries.toTypedArray(),
            selectedOption = eccentricityValue,
            onSelect = onEccentricitySelect,
        )

        SatelliteFilterDropdown(
            label = "Inclination",
            options = InclinationFilter.entries.toTypedArray(),
            selectedOption = inclinationValue,
            onSelect = onInclinationSelect,
        )

        SatelliteFilterDropdown(
            label = "Sort Asc By",
            options = SatelliteSort.entries.toTypedArray(),
            selectedOption = sortValue,
            onSelect = onSortSelect,
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.End),
            enabled = uiState != SatelliteCollectionUiState.Loading,
            colors = ButtonColors(
                containerColor = Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White,
            )
        ) {
            Text("Filter")
        }
    }
}

@Composable
fun <T : FilterOption> SatelliteFilterDropdown(
    label: String,
    options: Array<T>,
    selectedOption: T?,
    onSelect: (T) -> Unit,
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    Spacer(Modifier.height(10.dp))

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
                Text(text = selectedOption?.label ?: options[0].label)
                Image(
                    painter = painterResource(id = if (isDropDownExpanded.value) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "DropDown Icon"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                },
                containerColor = BlueLighter
            ) {
                options.forEachIndexed { _, option ->
                    DropdownMenuItem(
                        text = {
                            Text(option.label)
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

@Preview(showBackground = true)
@Composable
fun PreviewSatelliteScreen() {
    val satellite = Satellite(
        date = "2025-04-29T04:34:06+00:00",
        id = "https://tle.ivanstanojevic.me/api/tle/25544",
        line2 = "2 25544  51.6352 189.7367 0002491  81.0639 279.0631 15.49383308507563",
        line1 = "1 25544U 98067A   25119.19035294  .00013779  00000+0  25440-3 0  9995",
        name = "ISS (ZARYA)",
        satelliteId = 25544,
        type = "Tle",
        inclination = 51.6352,
        eccentricity = 0.0002491,
    )

    val satelliteList = listOf(satellite, satellite, satellite, satellite, satellite)

    SatelliteItemsList(satellites = satelliteList) {
        // Do nothing
    }
}


@Preview
@Composable
fun PreviewFilterSection() {
    val satellite = Satellite(
        date = "2025-04-29T04:34:06+00:00",
        id = "https://tle.ivanstanojevic.me/api/tle/25544",
        line2 = "2 25544  51.6352 189.7367 0002491  81.0639 279.0631 15.49383308507563",
        line1 = "1 25544U 98067A   25119.19035294  .00013779  00000+0  25440-3 0  9995",
        name = "ISS (ZARYA)",
        satelliteId = 25544,
        type = "Tle",
        inclination = 51.6352,
        eccentricity = 0.0002491,
    )

    val satelliteList = listOf(satellite, satellite, satellite, satellite, satellite)

    FilterSection(
        { },
        EccentricityFilter.ANY,
        { },
        InclinationFilter.ANY,
        { },
        SatelliteSort.DEFAULT,
        { },
        SatelliteCollectionUiState.Success(satelliteList)
    )
}


