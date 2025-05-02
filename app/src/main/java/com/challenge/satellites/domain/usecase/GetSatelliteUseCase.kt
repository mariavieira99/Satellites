/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.usecase

import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.domain.repository.SatelliteRepository
import javax.inject.Inject

class GetSatelliteUseCase @Inject constructor(private val repository: SatelliteRepository) {

    suspend fun getApiSatellites(queryParameters: ApiQueryParameters): List<Satellite> {
        return repository.getApiSatellites(queryParameters)
    }

    suspend fun getSatelliteById(satelliteId: Int): Satellite? {
        return repository.getSatelliteById(satelliteId)
    }
}