/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.repository

import com.challenge.satellites.data.api.QueryParameters
import com.challenge.satellites.domain.model.Satellite

interface SatelliteRepository {
    suspend fun getSatellites(queryParameters: QueryParameters): List<Satellite>

    suspend fun getSatelliteById(satelliteId: Int): Satellite?
}