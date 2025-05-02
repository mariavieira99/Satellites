/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.usecase

import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.domain.repository.SatelliteRepository
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort
import javax.inject.Inject

class GetSatelliteUseCase @Inject constructor(private val repository: SatelliteRepository) {

    suspend fun getApiSatellites(queryParameters: ApiQueryParameters): List<Satellite> {
        return repository.getApiSatellites(queryParameters)
    }

    suspend fun getApiSatelliteById(satelliteId: Int): Satellite? {
        return repository.getApiSatelliteById(satelliteId)
    }

    suspend fun getDbSatellites(
        sort: SatelliteSort,
        inclinationFilter: InclinationFilter,
        eccentricityFilter: EccentricityFilter,
    ): List<Satellite> {
        return repository.getDbSatellites(sort, inclinationFilter, eccentricityFilter)
    }

    suspend fun getDbSatelliteById(satelliteId: Int): Satellite? {
        return repository.getDbSatelliteById(satelliteId)
    }
}