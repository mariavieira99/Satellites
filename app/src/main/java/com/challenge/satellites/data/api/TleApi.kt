/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.api

import com.challenge.satellites.data.model.SatelliteCollection
import retrofit2.http.GET

interface TleApi {
    @GET("tle")
    suspend fun getCollection(): SatelliteCollection
}