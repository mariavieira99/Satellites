/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import com.challenge.satellites.presentation.state.SatelliteDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val satelliteUseCase: GetSatelliteUseCase,
) : ViewModel() {

    private val _satelliteDetailUiState =
        MutableStateFlow<SatelliteDetailUiState>(SatelliteDetailUiState.Loading)
    val satelliteDetailUiState: StateFlow<SatelliteDetailUiState> = _satelliteDetailUiState

    fun getSatelliteInfo(satelliteId: Int) {
        viewModelScope.launch {
            _satelliteDetailUiState.value = SatelliteDetailUiState.Loading
            try {
                val result = satelliteUseCase.getSatelliteById(satelliteId) ?: run {
                    _satelliteDetailUiState.value = SatelliteDetailUiState.Error
                    return@launch
                }
                _satelliteDetailUiState.value = SatelliteDetailUiState.Success(result)
            } catch (e: Exception) {
                _satelliteDetailUiState.value = SatelliteDetailUiState.Error
            }
        }
    }
}