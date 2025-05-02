/*
 * Copyright (c) 2024 Sensormatic Electronics, LLC.  All rights reserved.
 * Reproduction is forbidden without written approval of Sensormatic Electronics, LLC.
 */

package com.challenge.satellites.data.api

import com.challenge.satellites.presentation.SatelliteSort
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val PAGE_SIZE = 35

interface TleApi {
    @GET("tle")
    suspend fun getCollection(
        @Query("page-size") pageSize: Int = PAGE_SIZE,
        @Query("sort-dir") sortDir: String = "asc",
        @Query("sort") satelliteQuery: String = SatelliteSort.DEFAULT.label,
        @Query("eccentricity[gte]") eccentricityGreaterOrEqual: String? = null,
        @Query("eccentricity[lte]") eccentricityLessOrEqual: String? = null,
        @Query("inclination[gt]") inclinationGreater: String? = null,
        @Query("inclination[lt]") inclinationLess: String? = null,
    ): SatelliteCollection

    @GET("tle/{id}")
    suspend fun getSatelliteById(@Path("id") id: Int): SatelliteCollection.Member
}