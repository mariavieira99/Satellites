/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.data.api.QueryParameters
import com.challenge.satellites.data.api.SatelliteSort
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import com.challenge.satellites.presentation.FilterOptions
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SatelliteViewModel"

@HiltViewModel
class SatelliteViewModel @Inject constructor(
    private val satelliteUseCase: GetSatelliteUseCase,
) : ViewModel() {

    private val _satelliteCollectionUiState =
        MutableStateFlow<SatelliteCollectionUiState>(SatelliteCollectionUiState.Loading)

    val satelliteCollectionUiState: StateFlow<SatelliteCollectionUiState> =
        _satelliteCollectionUiState

    var eccentricityOptionSelected: Pair<String, Pair<Double?, Double?>?> by mutableStateOf(
        FilterOptions.eccentricityOptions[0]
    )
    var inclinationOptionSelected: Pair<String, Pair<Double?, Double?>?> by mutableStateOf(
        FilterOptions.inclinationOptions[0]
    )
    var sortOptionSelected: String by mutableStateOf(SatelliteSort.DEFAULT.value)

    init {
        viewModelScope.launch {
            _satelliteCollectionUiState.value = SatelliteCollectionUiState.Loading
            try {
                val satellites = satelliteUseCase.getSatellites(QueryParameters())
                _satelliteCollectionUiState.value = if (satellites.isNotEmpty()) {
                    SatelliteCollectionUiState.Success(satellites)
                } else {
                    SatelliteCollectionUiState.Error
                }
            } catch (e: Exception) {
                _satelliteCollectionUiState.value = SatelliteCollectionUiState.Error
                Log.e(TAG, "e=$e")
            }
        }
    }

    fun getFilteredItems() {
        viewModelScope.launch {
            _satelliteCollectionUiState.value = SatelliteCollectionUiState.Loading
            try {
                val queryParameters = getQueryParameters()
                Log.d(TAG, "getFilteredItems | queryParameters=$queryParameters")
                val satellites = satelliteUseCase.getSatellites(queryParameters)
                _satelliteCollectionUiState.value = if (satellites.isNotEmpty()) {
                    SatelliteCollectionUiState.Success(satellites)
                } else {
                    SatelliteCollectionUiState.Error
                }
            } catch (e: Exception) {
                _satelliteCollectionUiState.value = SatelliteCollectionUiState.Error
                Log.e(TAG, "e=$e")
            }
        }
    }

    private fun getQueryParameters(): QueryParameters {
        val (eccentricityGreater, eccentricityLess) = eccentricityOptionSelected.second
            ?: (null to null)
        val (inclinationGreater, inclinationLess) = inclinationOptionSelected.second
            ?: (null to null)
        return QueryParameters(
            sort = sortOptionSelected,
            eccentricityGreaterOrEqual = eccentricityGreater?.toString(),
            eccentricityLessOrEqual = eccentricityLess?.toString(),
            inclinationGreater = inclinationGreater?.toString(),
            inclinationLess = inclinationLess?.toString(),
        )
    }

}