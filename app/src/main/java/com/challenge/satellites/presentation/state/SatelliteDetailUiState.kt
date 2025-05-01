/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.state

import com.challenge.satellites.domain.model.Satellite

sealed class SatelliteDetailUiState {
    data object Loading : SatelliteDetailUiState()
    data class Success(val satellite: Satellite) : SatelliteDetailUiState()
    data object Error : SatelliteDetailUiState()
}