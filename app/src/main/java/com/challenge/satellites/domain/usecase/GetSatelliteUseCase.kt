/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.usecase

import com.challenge.satellites.data.model.SatelliteCollection
import com.challenge.satellites.domain.repository.SatelliteRepository
import javax.inject.Inject

class GetSatelliteUseCase @Inject constructor(private val repository: SatelliteRepository) {

    suspend fun getSatellites(): SatelliteCollection? {
        return repository.getSatellites()
    }

    suspend fun getSatelliteById(satelliteId: Int): SatelliteCollection.Member? {
        return repository.getSatelliteById(satelliteId)
    }
}