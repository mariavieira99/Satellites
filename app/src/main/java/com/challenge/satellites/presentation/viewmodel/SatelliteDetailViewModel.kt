/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.satellites.domain.usecase.GetSatelliteUseCase
import com.challenge.satellites.presentation.state.SatelliteDetailUiState
import com.challenge.satellites.utils.NetworkConnectivityProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SatelliteDetailViewModel @Inject constructor(
    private val satelliteUseCase: GetSatelliteUseCase,
) : ViewModel() {

    private val _satelliteDetailUiState =
        MutableStateFlow<SatelliteDetailUiState>(SatelliteDetailUiState.Loading)
    val satelliteDetailUiState: StateFlow<SatelliteDetailUiState> = _satelliteDetailUiState

    private val networkStatus: StateFlow<Boolean> =
        NetworkConnectivityProvider.isConnected.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            true
        )

    fun getSatelliteInfo(satelliteId: Int) {
        viewModelScope.launch {
            _satelliteDetailUiState.value = SatelliteDetailUiState.Loading
            if (networkStatus.value) {
                try {
                    val result = satelliteUseCase.getApiSatelliteById(satelliteId) ?: run {
                        _satelliteDetailUiState.value = SatelliteDetailUiState.Error
                        return@launch
                    }
                    _satelliteDetailUiState.value = SatelliteDetailUiState.Success(result)
                } catch (e: Exception) {
                    _satelliteDetailUiState.value = SatelliteDetailUiState.Error
                }
            } else {
                val result = satelliteUseCase.getDbSatelliteById(satelliteId) ?: run {
                    _satelliteDetailUiState.value = SatelliteDetailUiState.Error
                    return@launch
                }
                _satelliteDetailUiState.value = SatelliteDetailUiState.Success(result)
            }

        }
    }
}