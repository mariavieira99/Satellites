/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
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

    init {
        viewModelScope.launch {
            _satelliteCollectionUiState.value = SatelliteCollectionUiState.Loading
            try {
                val satellites = satelliteUseCase.getSatellites() ?: run {
                    _satelliteCollectionUiState.value = SatelliteCollectionUiState.Error
                    return@launch
                }
                _satelliteCollectionUiState.value = if (satellites.member.isNotEmpty()) {
                    SatelliteCollectionUiState.Success(satellites.member)
                } else {
                    SatelliteCollectionUiState.Error
                }
            } catch (e: Exception) {
                _satelliteCollectionUiState.value = SatelliteCollectionUiState.Error
                Log.e(TAG, "e=$e")
            }
        }
    }

}