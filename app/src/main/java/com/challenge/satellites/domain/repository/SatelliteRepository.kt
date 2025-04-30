/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.domain.repository

import com.challenge.satellites.data.model.SatelliteCollection

interface SatelliteRepository {
    suspend fun getSatellites(): SatelliteCollection?

    suspend fun getSatelliteById(satelliteId: Int): SatelliteCollection.Member?
}