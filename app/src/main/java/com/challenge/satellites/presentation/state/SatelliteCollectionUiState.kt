/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.presentation.state

import com.challenge.satellites.data.model.SatelliteCollection

sealed class SatelliteCollectionUiState {
    data object Loading : SatelliteCollectionUiState()
    data class Success(val satellites: List<SatelliteCollection.Member>) :
        SatelliteCollectionUiState()

    data object Error : SatelliteCollectionUiState()
}