/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.repository

import com.challenge.satellites.data.api.ApiQueryParameters
import com.challenge.satellites.domain.model.Satellite
import com.challenge.satellites.presentation.EccentricityFilter
import com.challenge.satellites.presentation.InclinationFilter
import com.challenge.satellites.presentation.SatelliteSort

interface SatelliteRepository {
    suspend fun getApiSatellites(queryParameters: ApiQueryParameters): List<Satellite>

    suspend fun getApiSatelliteById(satelliteId: Int): Satellite?

    suspend fun getDbSatellites(
        sort: SatelliteSort,
        inclinationFilter: InclinationFilter,
        eccentricityFilter: EccentricityFilter,
    ): List<Satellite>

    suspend fun getDbSatelliteById(satelliteId: Int): Satellite?
}