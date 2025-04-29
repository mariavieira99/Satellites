/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites

import retrofit2.Response
import retrofit2.http.GET

interface TleApi {
    @GET("tle")
    suspend fun getCollection(): Response<SatelliteCollection>
}