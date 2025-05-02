/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort
import com.challenge.satellites.presentation.state.SatelliteCollectionUiState
import com.challenge.satellites.utils.NetworkConnectivityProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    var eccentricityOptionSelected: EccentricityFilter by mutableStateOf(EccentricityFilter.ANY)
    var inclinationOptionSelected: InclinationFilter by mutableStateOf(InclinationFilter.ANY)
    var sortOptionSelected: SatelliteSort by mutableStateOf(SatelliteSort.DEFAULT)

    @VisibleForTesting
    val networkStatus: StateFlow<Boolean> =
        NetworkConnectivityProvider.isConnected.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            true
        )

    init {
        viewModelScope.launch {
            _satelliteCollectionUiState.value = SatelliteCollectionUiState.Loading
            val isOnline = networkStatus.value
            if (isOnline) {
                getApiItems(ApiQueryParameters())
            } else {
                getDbItems()
            }
        }

        viewModelScope.launch {
            networkStatus.collect { status ->
                Log.d(TAG, "network status=$status")
            }
        }
    }

    fun getFilteredItems() {
        viewModelScope.launch {
            _satelliteCollectionUiState.value = SatelliteCollectionUiState.Loading

            val isOnline = networkStatus.value
            Log.d(TAG, "getFilteredItems | isOnline=$isOnline")

            if (isOnline) {
                val queryParameters = getQueryParameters()
                getApiItems(queryParameters)
            } else {
                getDbItems()
            }
        }
    }

    private fun getDbItems() {
        viewModelScope.launch {
            Log.d(TAG, "getDbFilteredItems")
            val satellites = satelliteUseCase.getDbSatellites(
                sortOptionSelected,
                inclinationOptionSelected,
                eccentricityOptionSelected
            )
            _satelliteCollectionUiState.value = if (satellites.isNotEmpty()) {
                SatelliteCollectionUiState.Success(satellites)
            } else {
                SatelliteCollectionUiState.Error
            }
        }
    }

    private fun getApiItems(queryParameters: ApiQueryParameters) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "getApiItems | queryParameters=$queryParameters")
                val satellites = satelliteUseCase.getApiSatellites(queryParameters)
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

    fun getQueryParameters(): ApiQueryParameters {
        val (eccentricityGreater, eccentricityLess) = getEccentricityFilter()
        val (inclinationGreater, inclinationLess) = getInclinationFilter()

        return ApiQueryParameters(
            sort = sortOptionSelected.label,
            eccentricityGreaterOrEqual = eccentricityGreater?.toString(),
            eccentricityLessOrEqual = eccentricityLess?.toString(),
            inclinationGreater = inclinationGreater?.toString(),
            inclinationLess = inclinationLess?.toString(),
        )
    }

    fun getEccentricityFilter(): Pair<Double?, Double?> {
        return when (val option = eccentricityOptionSelected) {
            EccentricityFilter.ANY -> null to null
            EccentricityFilter.LE_001 -> null to option.lessThanOrEqual
            EccentricityFilter.RANGE_001_029, EccentricityFilter.RANGE_03_069, EccentricityFilter.RANGE_07_099 -> option.range!!.start to option.range.endInclusive
        }
    }

    fun getInclinationFilter(): Pair<Double?, Double?> {
        return when (val option = inclinationOptionSelected) {
            InclinationFilter.ANY -> null to null
            InclinationFilter.LT_20 -> null to option.lessThan
            InclinationFilter.BETWEEN_20_60, InclinationFilter.BETWEEN_60_100 -> option.range!!.start to option.range.endInclusive
            InclinationFilter.GT_100 -> option.greaterThan to null
        }
    }

}